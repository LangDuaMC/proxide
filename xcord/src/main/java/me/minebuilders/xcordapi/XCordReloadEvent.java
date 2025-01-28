package me.minebuilders.xcordapi;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Event;

public class XCordReloadEvent extends Event {
   private final CommandSender reloader;

   public XCordReloadEvent(CommandSender var1) {
      this.reloader = var1;
   }

   public CommandSender getReloader() {
      return this.reloader;
   }

   @Override
   public String toString() {
      return "XCordReloadEvent(reloader=" + this.getReloader() + ")";
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof XCordReloadEvent)) {
         return false;
      } else {
         XCordReloadEvent var2 = (XCordReloadEvent)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            CommandSender var3 = this.getReloader();
            CommandSender var4 = var2.getReloader();
            return var3 == null ? var4 == null : var3.equals(var4);
         }
      }
   }

   protected boolean canEqual(Object var1) {
      return var1 instanceof XCordReloadEvent;
   }

   @Override
   public int hashCode() {
      byte var1 = 59;
      byte var2 = 1;
      CommandSender var3 = this.getReloader();
      return var2 * 59 + (var3 == null ? 43 : var3.hashCode());
   }
}
