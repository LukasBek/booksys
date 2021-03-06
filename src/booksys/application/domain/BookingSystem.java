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
import java.util.* ;

public class BookingSystem
{
	// Attributes:

	Date currentDate ;
	Date today ;

	// Associations:

	Restaurant restaurant = null ;
	Vector currentBookings ;
	Booking selectedBooking ;

	// Singleton:

	private static BookingSystem uniqueInstance ;

	public static BookingSystem getInstance()
	{
		if (uniqueInstance == null) {
			uniqueInstance = new BookingSystem() ;
		}
		return uniqueInstance ;
	}

	private BookingSystem()
	{
		today = new Date(Calendar.getInstance().getTimeInMillis()) ;
		restaurant = new Restaurant() ;
	}

	// Observer: this is `Subject/ConcreteSubject'

	Vector observers = new Vector() ;

	public void addObserver(BookingObserver o)
	{
		observers.addElement(o) ;
	}

	public void notifyObservers()
	{
		Enumeration enume = observers.elements() ;
		while (enume.hasMoreElements()) {
			BookingObserver bo = (BookingObserver) enume.nextElement() ;
			bo.update() ;
		}
	}

	public boolean observerMessage(String message, boolean confirm)
	{
		BookingObserver bo = (BookingObserver) observers.elementAt(0) ;
		return bo.message(message, confirm) ;
	}

	// System messages:

	public void display(Date date)
	{
		currentDate = date ;
		currentBookings = restaurant.getBookings(currentDate) ;
		selectedBooking = null ;
		notifyObservers() ;
	}

	public void makeReservation(int covers, Date date, Time time, String name, String phone){
		// TODO: THIS
		int tno = getTable(time, covers, null);
		if(tno < 11){
			Booking b = restaurant.makeReservation(covers, date, time, tno, name, phone) ;
			currentBookings.addElement(b) ;
			notifyObservers() ;
		}
		else{
			observerMessage("No tables at currently selected time", false);
		}
			
	}
	

	public void makeWalkIn(int covers, Date date, Time time, int tno)
	{
		// TODO: THIS
		if (!doubleBooked(time, tno, null) && !overflow(tno, covers)) {
			Booking b = restaurant.makeWalkIn(covers, date, time, tno) ;
			currentBookings.addElement(b) ;
			notifyObservers() ;
		}
	}

	public void selectBooking(int tno, Time time)
	{
		selectedBooking = null ;
		Enumeration enume = currentBookings.elements() ;
		while (enume.hasMoreElements()) {
			Booking b = (Booking) enume.nextElement() ;
			if (b.getTableNumber() == tno) {
				if (b.getTime().before(time)
						&& b.getEndTime().after(time)) {
					selectedBooking = b ;
				}
			}
		}
		notifyObservers() ;
	}

	public void cancel()
	{
		if (selectedBooking != null) {
			if (observerMessage("Are you sure?", true)) {
				currentBookings.remove(selectedBooking) ;
				restaurant.removeBooking(selectedBooking) ;
				selectedBooking = null ;
				notifyObservers() ;
			}
		}
	}

	public void recordArrival(Time time)
	{
		if (selectedBooking != null) {
			if (selectedBooking.getArrivalTime() != null) {
				observerMessage("Arrival already recorded", false) ;
			}
			else {
				selectedBooking.setArrivalTime(time) ;
				restaurant.updateBooking(selectedBooking) ;
				notifyObservers() ;
			}
		}
	}

	public void transfer(Time time, int tno)
	{
		if (selectedBooking != null) {
			if (selectedBooking.getTableNumber() != tno) {
				if (!doubleBooked(selectedBooking.getTime(), tno, selectedBooking)
						&& !overflow(tno, selectedBooking.getCovers())) {
					selectedBooking.setTable(restaurant.getTable(tno)) ;
					restaurant.updateBooking(selectedBooking) ;
				}
			}
			notifyObservers() ;
		}
	}

	private int getTable(Time startTime, int covers, Booking ignore)
	{
		boolean booked = true ;
		int tno = 1;
		if (covers > 2){
			tno = 5;
		}

		Time endTime = (Time) startTime.clone() ;
		endTime.setHours(endTime.getHours() + 2);
		
		while (booked && tno < 11){
			Enumeration enume = currentBookings.elements();
			int sum = currentBookings.size();
			int tal = 0;
						
			while(enume.hasMoreElements()){
				
				Booking b = (Booking) enume.nextElement() ;
				if (b != ignore && b.getTableNumber() == tno
						&& startTime.before(b.getEndTime())
						&& endTime.after(b.getTime())) 
				{
					break;
				} else {
					tal++;
				}
				
			}
			if(sum == tal){
				return tno;
			}
			tno++;
		}
		return tno ;
	}
	
	private boolean doubleBooked(Time startTime, int tno, Booking ignore)
	{
		boolean doubleBooked = false ;

		Time endTime = (Time) startTime.clone() ;
		endTime.setHours(endTime.getHours() + 2) ;

		Enumeration enume = currentBookings.elements() ;
		while (!doubleBooked && enume.hasMoreElements()) {
			Booking b = (Booking) enume.nextElement() ;
			if (b != ignore && b.getTableNumber() == tno
					&& startTime.before(b.getEndTime())
					&& endTime.after(b.getTime())) {
				doubleBooked = true ;
				observerMessage("Double booking!", false) ;
			}
		}
		return doubleBooked ;
	}

	private boolean overflow(int tno, int covers)
	{
		boolean overflow = false ;
		Table t = restaurant.getTable(tno) ;

		if (t.getPlaces() < covers) {
			overflow = !observerMessage("Ok to overfill table?", true) ;
		}

		return overflow ;
	}

	// Other Operations:

	public Date getCurrentDate()
	{
		return currentDate ;
	}

	public Enumeration getBookings()
	{
		return currentBookings.elements() ;
	}

	public Booking getSelectedBooking()
	{
		return selectedBooking ;
	}

	public static Vector getTableNumbers()
	{
		return Restaurant.getTableNumbers() ;
	}
}
