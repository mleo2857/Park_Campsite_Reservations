package DAO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;
import com.techelevator.model.Site;

public interface CampgroundDAO {
	
	
	public Campground selectCampground(int campgroundId);
	
	public boolean isOpen(Object[] reservationInfo);
	
	public List<Site> getAvailableSites(Campground campground, Date startDate, Date endDate);
	
	public List<Site> getAllSitesForCampground(Campground campground);
	
	public List<Campground> getCampgroundsByParkId(Park park);
	
	public BigDecimal getCostByCampgroundId(int id);
	
	
}
