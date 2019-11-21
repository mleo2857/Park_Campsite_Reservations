package DAO;

import java.util.List;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;

public interface ParkDAO {
	public List<Park> viewAllParks();
	
	public Park selectPark(int parkId);
	
	public Park getParkByName(String name);
}
