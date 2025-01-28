package me.minebuilders.xcordapi;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

/**
 * Represents an event triggered when the anti-bot system is toggled on or off.
 * This event is cancellable, meaning it can be prevented from proceeding by calling {@link #setCancelled(boolean)}.
 * It provides information about the current state of the anti-bot system, including connection metrics and blacklist data.
 */
public class XCordToggleAntibotEvent extends Event implements Cancellable {
   private boolean toggleState; // Whether the anti-bot system is toggled on or off
   private int connectionsPerSecond; // The number of connections per second during the event
   private boolean cancelled = false; // Whether the event is cancelled
   private int blacklists; // The number of blacklists triggered during the event
   private int totalConnectionsDuringAttack; // The total number of connections during the attack

   /**
    * Constructs a new XCordToggleAntibotEvent.
    *
    * @param toggleState                  Whether the anti-bot system is toggled on or off.
    * @param connectionsPerSecond         The number of connections per second during the event.
    * @param blacklists                   The number of blacklists triggered during the event.
    * @param totalConnectionsDuringAttack The total number of connections during the attack.
    */
   public XCordToggleAntibotEvent(boolean toggleState, int connectionsPerSecond, int blacklists, int totalConnectionsDuringAttack) {
      this.toggleState = toggleState;
      this.connectionsPerSecond = connectionsPerSecond;
      this.blacklists = blacklists;
      this.totalConnectionsDuringAttack = totalConnectionsDuringAttack;
   }

   /**
    * Checks if the anti-bot system is toggled on.
    *
    * @return True if the anti-bot system is toggled on, false otherwise.
    */
   public boolean isAntiBotToggledOn() {
      return this.toggleState;
   }

   /**
    * Sets the toggle state of the anti-bot system.
    *
    * @param toggleState True to toggle the anti-bot system on, false to toggle it off.
    */
   public void setToggleState(boolean toggleState) {
      this.toggleState = toggleState;
   }

   /**
    * Gets the number of connections per second during the event.
    *
    * @return The number of connections per second.
    */
   public int getConnectionsPerSecond() {
      return this.connectionsPerSecond;
   }

   /**
    * Sets the number of connections per second during the event.
    *
    * @param connectionsPerSecond The number of connections per second.
    */
   public void setConnectionsPerSecond(int connectionsPerSecond) {
      this.connectionsPerSecond = connectionsPerSecond;
   }

   /**
    * Checks the current toggle state of the anti-bot system.
    *
    * @return True if the anti-bot system is toggled on, false otherwise.
    */
   public boolean isToggleState() {
      return this.toggleState;
   }

   /**
    * Gets the number of blacklists triggered during the event.
    *
    * @return The number of blacklists.
    */
   public int getBlacklists() {
      return this.blacklists;
   }

   /**
    * Gets the total number of connections during the attack.
    *
    * @return The total number of connections.
    */
   public int getTotalConnectionsDuringAttack() {
      return this.totalConnectionsDuringAttack;
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
    * Sets the number of blacklists triggered during the event.
    *
    * @param blacklists The number of blacklists.
    */
   public void setBlacklists(int blacklists) {
      this.blacklists = blacklists;
   }

   /**
    * Sets the total number of connections during the attack.
    *
    * @param totalConnectionsDuringAttack The total number of connections.
    */
   public void setTotalConnectionsDuringAttack(int totalConnectionsDuringAttack) {
      this.totalConnectionsDuringAttack = totalConnectionsDuringAttack;
   }

   /**
    * Returns a string representation of the event.
    *
    * @return A string containing the toggle state, connections per second, cancellation status,
    *         blacklists, and total connections during the attack.
    */
   @Override
   public String toString() {
      return "XCordToggleAntibotEvent(toggleState="
              + this.isToggleState()
              + ", connectionsPerSecond="
              + this.getConnectionsPerSecond()
              + ", cancelled="
              + this.isCancelled()
              + ", blacklists="
              + this.getBlacklists()
              + ", totalConnectionsDuringAttack="
              + this.getTotalConnectionsDuringAttack()
              + ")";
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
      if (!(obj instanceof XCordToggleAntibotEvent)) {
         return false;
      }
      XCordToggleAntibotEvent other = (XCordToggleAntibotEvent) obj;
      if (!other.canEqual(this)) {
         return false;
      }
      if (this.isToggleState() != other.isToggleState()) {
         return false;
      }
      if (this.getConnectionsPerSecond() != other.getConnectionsPerSecond()) {
         return false;
      }
      if (this.isCancelled() != other.isCancelled()) {
         return false;
      }
      if (this.getBlacklists() != other.getBlacklists()) {
         return false;
      }
      return this.getTotalConnectionsDuringAttack() == other.getTotalConnectionsDuringAttack();
   }

   /**
    * Checks if the given object can be compared with this event.
    *
    * @param obj The object to check.
    * @return True if the object is an instance of XCordToggleAntibotEvent, false otherwise.
    */
   protected boolean canEqual(Object obj) {
      return obj instanceof XCordToggleAntibotEvent;
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
      result = result * prime + (this.isToggleState() ? 79 : 97);
      result = result * prime + this.getConnectionsPerSecond();
      result = result * prime + (this.isCancelled() ? 79 : 97);
      result = result * prime + this.getBlacklists();
      result = result * prime + this.getTotalConnectionsDuringAttack();
      return result;
   }
}