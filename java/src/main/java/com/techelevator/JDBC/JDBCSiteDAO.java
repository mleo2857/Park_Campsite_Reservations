package com.techelevator.JDBC;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.Reservation;
import com.techelevator.model.Site;

import DAO.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Site> getSitesByCampgroundId(int id) {
		List<Site> sites = new ArrayList<Site>();
		String sqlCommand = "SELECT * FROM site where campground_id = " + id;

		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlCommand);

		while (result.next()) {
			Site aSite = mapRowToSites(result);
			sites.add(aSite);
		}
		return sites;
	}
	

	@Override
	public List<Site> findAvailableSites(Object[] reservationInfo) {
		Date arrivalDate = (Date) reservationInfo[1];
		Date departureDate = (Date) reservationInfo[2];
		
		
		
		List<Site> allSites = getSitesByCampgroundId((int) reservationInfo[0]);
		JDBCReservationDAO reservationDAO = new JDBCReservationDAO();
		reservationDAO.setJDBCTemplate(this.jdbcTemplate);
		List<Site> availableSites = new ArrayList<Site>();
		for (Site site : allSites) {
			List<Reservation> reservationsForSite = reservationDAO.getReservationsBySiteId(site.getSite_id());
			
			if (reservationsForSite.size()==0) {
				availableSites.add(site);
			} else {
				boolean siteAvailable = true;
				for (Reservation reservation : reservationsForSite) {
					
					
					boolean arrivalDateSame = (arrivalDate.equals(reservation.getFrom_date()));

					boolean departureDateSame = (departureDate.equals(reservation.getTo_date()));
					boolean arrivalDateValid = (!(arrivalDate.after(reservation.getFrom_date())&&arrivalDate.before(reservation.getTo_date()))&&!arrivalDateSame);
					
					boolean departureDateValid = (!(departureDate.after(reservation.getFrom_date())&&departureDate.before(reservation.getTo_date()))&&!departureDateSame);
					if(!arrivalDateValid||!departureDateValid) {
//						if (!availableSites.contains(site)) {
//							availableSites.add(site);
//						}
						siteAvailable = false;
					}
				}
				if (siteAvailable == true) {

					availableSites.add(site);
				}
			}
		}
		return availableSites;
	}
	
	public int getSiteIdFromCampgroundIdAndSiteNumber(int campground_id, int siteNumber) {
		SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM site WHERE campground_id = "+campground_id+" AND site_number = "+siteNumber);
		Site site = new Site();
		while(results.next()) {
			site =mapRowToSites(results);
		}
		return site.getSite_id();
	}
	
	@Override
	public List<Site> advancedSearchSites(Object[] reservationInfo){
		List<Site> sites = new ArrayList<Site>();
		List<Site> availableSites = findAvailableSites(reservationInfo);
		
		for (Site site : availableSites) {
			boolean hasMaxOccupancy = site.getMax_occupancy() >= (int)reservationInfo[3];
			boolean hasAccessibility = (boolean)reservationInfo[4] ? (site.isAccessible() == true) : true;
			boolean hasMaxRVLength = site.getMax_rv_length() >= (int)reservationInfo[5];
			boolean hasUtilities = (boolean)reservationInfo[6] ? (site.isAccessible() == true) : true;
			
			if (hasMaxOccupancy && hasAccessibility && hasMaxRVLength && hasUtilities) {
				sites.add(site);
			}
		}
		
		
		return sites;
	}

	private Site mapRowToSites(SqlRowSet results) {
		Site theSite = new Site();

		theSite.setSite_id(results.getInt("site_id"));
		theSite.setCampground_id(results.getInt("campground_id"));
		theSite.setSite_number(results.getInt("site_number"));
		theSite.setMax_occupancy(results.getInt("max_occupancy"));
		theSite.setAccessible(results.getBoolean("accessible"));
		theSite.setMax_rv_length(results.getInt("max_rv_length"));
		theSite.setUtilities(results.getBoolean("utilities"));

		return theSite;
	}

}
