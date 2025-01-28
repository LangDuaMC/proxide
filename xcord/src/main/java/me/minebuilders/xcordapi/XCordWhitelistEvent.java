package me.minebuilders.xcordapi;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

/**
 * Represents an event triggered when a player is added to or removed from the whitelist.
 * This event is cancellable, meaning it can be prevented from proceeding by calling {@link #setCancelled(boolean)}.
 */
public class XCordWhitelistEvent extends Event implements Cancellable {
   private String player; // The name of the player involved in the event
   private String ip;     // The IP address of the player
   private long time;     // The timestamp when the event occurred
   private boolean cancelled = false; // Whether the event is cancelled

   /**
    * Constructs a new XCordWhitelistEvent.
    *
    * @param player The name of the player.
    * @param ip     The IP address of the player.
    * @param time   The timestamp when the event occurred.
    */
   public XCordWhitelistEvent(String player, String ip, long time) {
      this.player = player;
      this.ip = ip;
      this.time = time;
   }

   /**
    * Gets the name of the player involved in the event.
    *
    * @return The player's name.
    */
   public String getPlayer() {
      return this.player;
   }

   /**
    * Sets the name of the player involved in the event.
    *
    * @param player The player's name.
    */
   public void setPlayer(String player) {
      this.player = player;
   }

   /**
    * Gets the IP address of the player.
    *
    * @return The player's IP address.
    */
   public String getIp() {
      return this.ip;
   }

   /**
    * Sets the IP address of the player.
    *
    * @param ip The player's IP address.
    */
   public void setIp(String ip) {
      this.ip = ip;
   }

   /**
    * Gets the timestamp when the event occurred.
    *
    * @return The event timestamp.
    */
   public long getTime() {
      return this.time;
   }

   /**
    * Sets the timestamp when the event occurred.
    *
    * @param time The event timestamp.
    */
   public void setTime(long time) {
      this.time = time;
   }

   /**
    * Checks if the event is cancelled.
    *
    * @return True if the event is cancelled, false otherwise.
    */
   @Override
   public boolean isCancelled() {
      return this.cancelled;
   }

   /**
    * Sets whether the event is cancelled.
    *
    * @param cancelled True to cancel the event, false to allow it to proceed.
    */
   @Override
   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   /**
    * Returns a string representation of the event.
    *
    * @return A string containing the player, IP, timestamp, and cancellation status.
    */
   @Override
   public String toString() {
      return "XCordWhitelistEvent(player=" + this.getPlayer() + ", ip=" + this.getIp() + ", time=" + this.getTime() + ", cancelled=" + this.isCancelled() + ")";
   }

   /**
    * Checks if this event is equal to another object.
    *
    * @param obj The object to compare with.
    * @return True if the objects are equal, false otherwise.
    */
   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof XCordWhitelistEvent)) {
         return false;
      }
      XCordWhitelistEvent other = (XCordWhitelistEvent) obj;
      if (!other.canEqual(this)) {
         return false;
      }
      if (this.getTime() != other.getTime()) {
         return false;
      }
      if (this.isCancelled() != other.isCancelled()) {
         return false;
      }
      String thisPlayer = this.getPlayer();
      String otherPlayer = other.getPlayer();
      if (thisPlayer == null ? otherPlayer != null : !thisPlayer.equals(otherPlayer)) {
         return false;
      }
      String thisIp = this.getIp();
      String otherIp = other.getIp();
      return thisIp == null ? otherIp == null : thisIp.equals(otherIp);
   }

   /**
    * Checks if the given object can be compared with this event.
    *
    * @param obj The object to check.
    * @return True if the object is an instance of XCordWhitelistEvent, false otherwise.
    */
   protected boolean canEqual(Object obj) {
      return obj instanceof XCordWhitelistEvent;
   }

   /**
    * Returns the hash code for this event.
    *
    * @return The hash code.
    */
   @Override
   public int hashCode() {
      final int prime = 59;
      int result = 1;
      long time = this.getTime();
      result = result * prime + (int) (time >>> 32 ^ time);
      result = result * prime + (this.isCancelled() ? 79 : 97);
      String player = this.getPlayer();
      result = result * prime + (player == null ? 43 : player.hashCode());
      String ip = this.getIp();
      result = result * prime + (ip == null ? 43 : ip.hashCode());
      return result;
   }
}