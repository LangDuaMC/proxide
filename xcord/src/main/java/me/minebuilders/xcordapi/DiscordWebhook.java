package me.minebuilders.xcordapi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

public class DiscordWebhook {
   private final transient Map<String, String> replacers = new HashMap<>();
   private String url;
   private String content;
   private String username;
   private String avatarUrl;
   private boolean tts;
   private List<EmbedObject> embeds = new ArrayList<>();

   public DiscordWebhook setReplacer(String key, String value) {
      return this;
   }

   public Map<String, String> getReplacers() {
      return replacers;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getContent() {
      return content;
   }

   public String getUsername() {
      return username;
   }

   public String getAvatarUrl() {
      return avatarUrl;
   }

   public boolean isTts() {
      return tts;
   }

   public List<EmbedObject> getEmbeds() {
      return embeds;
   }

   public void setEmbeds(List<EmbedObject> embeds) {
      this.embeds = embeds;
   }

   public void loadAndExecute(File file) throws Exception {}

   public DiscordWebhook(String url) {
      this.url = url;
   }

   public DiscordWebhook() {}

   public void setContent(String content) {
      this.content = content;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setAvatarUrl(String avatarUrl) {
      this.avatarUrl = avatarUrl;
   }

   public void setTts(boolean tts) {
      this.tts = tts;
   }

   public void addEmbed(EmbedObject embed) {
      this.embeds.add(embed);
   }

   public void execute() throws IOException {}

   public static class Color {
      public int red;
      public int green;
      public int blue;

      public Color(int red, int green, int blue) {
         this.red = red;
         this.green = green;
         this.blue = blue;
      }
   }

   public static class EmbedObject {
      private String title;
      private String description;
      private String url;
      private Color color;
      private Footer footer;
      private Thumbnail thumbnail;
      private Image image;
      private Author author;
      private List<Field> fields = new ArrayList<>();

      public String getTitle() {
         return title;
      }

      public String getDescription() {
         return description;
      }

      public String getUrl() {
         return url;
      }

      public Color getColor() {
         return color;
      }

      public Footer getFooter() {
         return footer;
      }

      public Thumbnail getThumbnail() {
         return thumbnail;
      }

      public Image getImage() {
         return image;
      }

      public Author getAuthor() {
         return author;
      }

      public List<Field> getFields() {
         return fields;
      }

      public EmbedObject setTitle(String title) {
         this.title = title;
         return this;
      }

      public EmbedObject setDescription(String description) {
         this.description = description;
         return this;
      }

      public EmbedObject setUrl(String url) {
         this.url = url;
         return this;
      }

      public EmbedObject setColor(Color color) {
         this.color = color;
         return this;
      }

      public EmbedObject setFooter(String text, String iconUrl) {
         this.footer = new Footer(text, iconUrl);
         return this;
      }

      public EmbedObject setThumbnail(String url) {
         this.thumbnail = new Thumbnail(url);
         return this;
      }

      public EmbedObject setImage(String url) {
         this.image = new Image(url);
         return this;
      }

      public EmbedObject setAuthor(String name, String url, String iconUrl) {
         this.author = new Author(name, url, iconUrl);
         return this;
      }

      public EmbedObject addField(String name, String value, boolean inline) {
         this.fields.add(new Field(name, value, inline));
         return this;
      }

      private class Author {
         private String name;
         private String url;
         private String iconUrl;

         private Author(String name, String url, String iconUrl) {
            this.name = name;
            this.url = url;
            this.iconUrl = iconUrl;
         }

         private String getName() {
            return name;
         }

         private String getUrl() {
            return url;
         }

         private String getIconUrl() {
            return iconUrl;
         }
      }

      private class Field {
         private String name;
         private String value;
         private boolean inline;

         private Field(String name, String value, boolean inline) {
            this.name = name;
            this.value = value;
            this.inline = inline;
         }

         private String getName() {
            return name;
         }

         private String getValue() {
            return value;
         }

         private boolean isInline() {
            return inline;
         }
      }

      private class Footer {
         private String text;
         private String iconUrl;

         private Footer(String text, String iconUrl) {
            this.text = text;
            this.iconUrl = iconUrl;
         }

         private String getText() {
            return text;
         }

         private String getIconUrl() {
            return iconUrl;
         }
      }

      private class Image {
         private String url;

         private Image(String url) {
            this.url = url;
         }

         private String getUrl() {
            return url;
         }
      }

      private class Thumbnail {
         private String url;

         private Thumbnail(String url) {
            this.url = url;
         }

         private String getUrl() {
            return url;
         }
      }
   }

   private class JSONObject {
      private final HashMap<String, Object> map = new HashMap<>();

      private JSONObject() {}

      void put(String key, Object value) {}

      @Override
      public String toString() {
         return "";
      }

      private String quote(String input) {
         return "";
      }
   }
}