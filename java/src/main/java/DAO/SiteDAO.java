package DAO;

import java.util.List;

import com.techelevator.model.Site;

public interface SiteDAO {
	public List<Site> getSitesByCampgroundId(int id);
	
	public List<Site> findAvailableSites(Object[] reservationInfo);
	
	public int getSiteIdFromCampgroundIdAndSiteNumber(int campground_id, int siteNumber);
	
	public List<Site> advancedSearchSites(Object[] reservationInfo);
}
