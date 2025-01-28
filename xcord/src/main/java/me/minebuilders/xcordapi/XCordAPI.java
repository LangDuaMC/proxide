package me.minebuilders.xcordapi;

import java.util.List;

public class XCordAPI {
   // Service provider interface
   public interface ServiceProvider {
      void addBypassFilter(BypassFilter filter);
      void registerBlacklist(BlacklistInterface blacklist);
      BlacklistInterface getBaseBlacklist();

      static ServiceProvider getInstance() {
         // External implementation
         throw new UnsupportedOperationException();
      }
   }

   // Blacklist subsystem interfaces
   public interface BlacklistInterface {
      boolean blacklist(String entry);
      boolean isBlacklisted(String entry);
      int clear();
      boolean isEmpty();
      int getEstimatedSize();
   }

   public interface BlacklistAppender {
      void submitIP(String ip);
      void flush();
   }

   // API methods (implemented externally)
   public static void addBypassFilter(BypassFilter filter) {
      ServiceProvider.getInstance().addBypassFilter(filter);
   }

   public static void registerBlacklistAdapter(Adapter<List<String>> adapter) {
      // Implementation exists externally
      throw new UnsupportedOperationException();
   }

   // Wrapped component (type definition only)
   private static class WrappedBlacklist implements BlacklistInterface {
      private final BlacklistAppender appender;
      private final BlacklistInterface wrapped;

      public WrappedBlacklist(BlacklistAppender appender, BlacklistInterface wrapped) {
         this.appender = appender;
         this.wrapped = wrapped;
      }

      // Method implementations exist externally
      @Override public boolean blacklist(String entry) { return false; }
      @Override public boolean isBlacklisted(String entry) { return false; }
      @Override public int clear() { return 0; }
      @Override public boolean isEmpty() { return false; }
      @Override public int getEstimatedSize() { return 0; }
   }
}