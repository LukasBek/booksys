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

public class NewReservation extends NewBookingImp
{
  private Customer customer ;
  private Time     arrivalTime ;
  private boolean confirmed;
  
  public NewReservation(int c, Date d, Time t, Table tab, Customer cust, Time arr)
  {
    super(c, d, t, tab) ;
    customer    = cust ;
    arrivalTime = arr ;
    confirmed = false;
  }

  public java.sql.Time getArrivalTime() {
    return arrivalTime ;
  }

  public Customer getCustomer() {
    return customer ;
  }

  public String getDetails()
  {
    StringBuffer details = new StringBuffer(64) ;
    details.append(customer.getName()) ;
    details.append(" ") ;
    details.append(customer.getPhoneNumber()) ;
    details.append(" (") ;
    details.append(covers) ;
    details.append(")") ;
    if (arrivalTime != null) {
      details.append(" [") ;
      details.append(arrivalTime) ;
      details.append("]") ;
    }
    return details.toString() ;
  }

  public void setArrivalTime(Time t) {
    arrivalTime = t ;
  }

  public void setCustomer(Customer c) {
    customer = c ;
  }

@Override
public boolean getConfirm() {
	return confirmed;
}

@Override
public void setConfirm(boolean c) {
	this.confirmed = c;
}
}
