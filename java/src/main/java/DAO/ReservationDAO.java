package DAO;

import java.util.List;

import com.techelevator.model.Park;
import com.techelevator.model.Reservation;

public interface ReservationDAO {
	public void addReservation(List<Object> reservationInfo);
	
	public void cancelReservation(Reservation reservation);
	
	public List<Reservation> getReservationsBySiteId(int id);
	
	public List<Reservation> getReservationsFor30DaysByPark(Park park);
}
