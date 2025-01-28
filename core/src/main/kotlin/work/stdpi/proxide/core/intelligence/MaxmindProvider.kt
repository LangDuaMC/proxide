package work.stdpi.proxide.core.intelligence

import com.maxmind.db.CHMCache
import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.model.AsnResponse
import com.maxmind.geoip2.model.CityResponse
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import work.stdpi.proxide.core.ExpositionTag
import work.stdpi.proxide.core.ITags
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.net.InetAddress
import java.nio.file.Files
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.math.pow

class MaxmindProvider(
    private val accountId: String,
    private val licenseKey: String,
    private val databaseDir: File = File("maxmind_databases")
) {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val credential = Credentials.basic(accountId, licenseKey)
            chain.proceed(
                chain.request().newBuilder()
                    .header("Authorization", credential)
                    .build()
            )
        }
        .build()

    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(2)
    private var cityReader: DatabaseReader? = null
    private var asnReader: DatabaseReader? = null

    private val cityDatabaseFile = File(databaseDir, "GeoLite2-City.mmdb")
    private val asnDatabaseFile = File(databaseDir, "GeoLite2-ASN.mmdb")

    fun onEnable() {
        databaseDir.mkdirs()
        scheduleCityUpdates()
        scheduleAsnUpdates()
        loadExistingDatabases()
    }

    fun onDisable() {
        executor.shutdownNow()
        cityReader?.close()
        asnReader?.close()
    }

    fun getLocation(ipAddress: InetAddress) =
        cityReader?.tryQuery<CityResponse>(ipAddress)?.let { MaxmindCityResponse.from(it) }

    fun getAsn(ipAddress: InetAddress) = asnReader?.tryQuery<AsnResponse>(ipAddress)?.let {
        MaxmindASNResponse.from(it)
    }

    class MaxmindASNResponse(
        val autonomousSystemNumber: Long,
        val autonomousSystemOrganization: String,
    ) : ITags {
        override fun toTags(): Iterable<ExpositionTag<*>> = listOf(
            ExpositionTag("ip_asn", autonomousSystemNumber),
            ExpositionTag("ip_isp", autonomousSystemOrganization),
        )

        companion object {
            fun from(r: AsnResponse): MaxmindASNResponse {
                return MaxmindASNResponse(
                    r.autonomousSystemNumber,
                    r.autonomousSystemOrganization,
                )
            }
        }
    }

    class MaxmindCityResponse(
        val cityName: String?,       // City name in English
        val countryIsoCode: String?, // Country ISO code (e.g., "US")
        val latitude: Double?,       // Latitude of the location
        val longitude: Double?,      // Longitude of the location
    ) : ITags {
        override fun toTags(): Iterable<ExpositionTag<*>> = listOf(
            ExpositionTag("ip_country", countryIsoCode),
            ExpositionTag("ip_city", cityName),
            ExpositionTag("ip_lat", latitude),
            ExpositionTag("ip_long", longitude)
        )

        companion object {
            fun from(r: CityResponse): MaxmindCityResponse {
                return MaxmindCityResponse(
                    cityName = r.city?.names?.get("en"),
                    countryIsoCode = r.country?.isoCode,
                    latitude = r.location?.latitude,
                    longitude = r.location?.longitude,
                )
            }
        }
    }

    private fun loadExistingDatabases() {
        cityReader = tryCreateReader(cityDatabaseFile)
        asnReader = tryCreateReader(asnDatabaseFile)
    }

    private fun tryCreateReader(file: File): DatabaseReader? {
        return try {
            DatabaseReader.Builder(file).withCache(CHMCache()).build()
        } catch (e: Exception) {
            null
        }
    }

    private fun scheduleCityUpdates() {
        executor.scheduleWithFixedDelay({
            tryUpdateDatabase(
                databaseFile = cityDatabaseFile,
                downloadUrl = "https://download.maxmind.com/geoip/databases/GeoLite2-City/download?suffix=tar.gz",
                type = "City"
            )
        }, calculateCityInitialDelay(), 7, TimeUnit.DAYS)
    }

    private fun scheduleAsnUpdates() {
        executor.scheduleAtFixedRate({
            tryUpdateDatabase(
                databaseFile = asnDatabaseFile,
                downloadUrl = "https://download.maxmind.com/geoip/databases/GeoLite2-ASN/download?suffix=tar.gz",
                type = "ASN"
            )
        }, calculateAsnInitialDelay(), 1, TimeUnit.DAYS)
    }

    private fun calculateCityInitialDelay(): Long {
        val utcMinus5 = ZoneId.of("UTC-05:00")
        val utcPlus8 = ZoneId.of("UTC+08:00")

        // Current time in UTC-5
        val now = ZonedDateTime.now(utcMinus5)

        // Determine the next Tuesday or Friday (whichever comes first)
        val nextDay = if (now.dayOfWeek == DayOfWeek.MONDAY || now.dayOfWeek < DayOfWeek.TUESDAY) {
            DayOfWeek.TUESDAY
        } else if (now.dayOfWeek == DayOfWeek.TUESDAY || now.dayOfWeek < DayOfWeek.FRIDAY) {
            DayOfWeek.FRIDAY
        } else {
            DayOfWeek.TUESDAY
        }

        // Find the next Tuesday or Friday 23:59:59 in UTC-5
        val nextTargetDay = now.with(TemporalAdjusters.next(nextDay))
            .with(LocalTime.of(23, 59, 59))

        // Convert to UTC+8 and calculate target time (3 AM next day in UTC+8)
        val targetTime = nextTargetDay.withZoneSameInstant(utcPlus8)
            .plusDays(1)
            .withHour(3)
            .withMinute(0)
            .withSecond(0)

        // Return the delay in milliseconds
        return Duration.between(Instant.now(), targetTime.toInstant()).toMillis()
    }


    private fun calculateAsnInitialDelay(): Long {
        val zonePlus8 = ZoneId.of("UTC+08:00")
        val now = ZonedDateTime.now(zonePlus8)
        val target = now.withHour(3).withMinute(0).withSecond(0)

        return if (now.isBefore(target)) {
            Duration.between(now, target).toMillis()
        } else {
            Duration.between(now, target.plusDays(1)).toMillis()
        }
    }

    private fun tryUpdateDatabase(databaseFile: File, downloadUrl: String, type: String) {
        var success = false
        var attempts = 0
        val maxAttempts = 5

        while (!success && attempts < maxAttempts) {
            attempts++
            try {
                val tempDir = Files.createTempDirectory("maxmind").toFile()
                try {
                    val tempFile = File(tempDir, "temp.tar.gz")
                    downloadFile(downloadUrl, tempFile)
                    val mmdbFile = extractMmdbFile(tempFile, tempDir)
                    updateDatabaseReader(mmdbFile, databaseFile, type)
                    success = true
                } finally {
                    tempDir.deleteRecursively()
                }
            } catch (e: Exception) {
                if (attempts >= maxAttempts) {
                    logError("Final update attempt failed for $type: ${e.message}")
                }
                Thread.sleep((2.0.pow(attempts.toDouble()) * 60 * 1000).toLong())
            }
        }
    }

    private fun downloadFile(url: String, destination: File) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            response.body?.byteStream()?.use { input ->
                destination.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    private fun extractMmdbFile(archive: File, destDir: File): File {
        BufferedInputStream(archive.inputStream()).use { bis ->
            GzipCompressorInputStream(bis).use { gzi ->
                TarArchiveInputStream(gzi).use { tar ->
                    generateSequence { tar.nextEntry }
                        .filter { it.name.endsWith(".mmdb") }
                        .firstOrNull()
                        ?.let {
                            val outputFile = File(destDir, it.name.substringAfterLast('/'))
                            outputFile.outputStream().use { out ->
                                tar.copyTo(out)
                            }
                            return outputFile
                        } ?: throw IOException("No .mmdb file found")
                }
            }
        }
    }

    private fun updateDatabaseReader(source: File, target: File, type: String) {
        val tempTarget = File(target.parentFile, "${target.name}.tmp")
        source.copyTo(tempTarget, overwrite = true)
        tempTarget.renameTo(target)

        val newReader = DatabaseReader.Builder(target).withCache(CHMCache()).build()
        when (type) {
            "City" -> {
                cityReader?.close()
                cityReader = newReader
            }

            "ASN" -> {
                asnReader?.close()
                asnReader = newReader
            }
        }
    }

    // private inline fun <reified T> DatabaseReader.tryQueryString(ipAddress: String) =
    //     this.tryQuery<T>(InetAddress.getByName(ipAddress))

    private inline fun <reified T> DatabaseReader.tryQuery(address: InetAddress): T? {
        return try {
            when (T::class) {
                CityResponse::class -> city(address) as T
                AsnResponse::class -> asn(address) as T
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun logError(message: String) {
        System.err.println("${Instant.now()} [Maxmind] ERROR: $message")
    }
}