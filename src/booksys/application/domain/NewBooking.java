/*
 * Restaurant Booking System: example code to accompany
 *
 * "Practical Object-oriented Design with UML"
 * Mark Priestley
 * McGraw-Hill (2004)
 */

package booksys.application.domain ;

import java.sql.Date ;
import java.sql.Time ;

/* This needs to be an interface so that PersistentBooking can be
   an interface, and so implemented by PersistentReservation and
   PersistentWalkin which already extend Reservation and Walkin.
*/

public interface NewBooking extends Booking
{
	  public boolean getConfirm();
	  public void setConfirm(boolean c);

}
