package com.techelevator.JDBC;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;
import com.techelevator.model.Site;

import DAO.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Campground selectCampground(int campgroundId) {
		String sqlCommand = "SELECT * FROM campground WHERE campground_id = " + campgroundId;
		
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlCommand);
		Campground campground = new Campground();
		while(result.next()) {
			campground = mapRowToCampgrounds(result);
		}
		
		return campground;
	}

	@Override
	public boolean isOpen(Object[] reservationInfo) {
		Campground thisCampground = selectCampground((int)reservationInfo[0]);
		LocalDate reservationStart = convertToLocalDateViaInstant((Date)reservationInfo[1]);
		LocalDate reservationEnd = convertToLocalDateViaInstant((Date)reservationInfo[2]);
		int reservationStartMonth = reservationStart.getMonthValue();
		int reservationEndMonth = reservationEnd.getMonthValue();
		
		int campgroundOpenMonth = Integer.parseInt(thisCampground.getOpen_from_mm());
		int campgroundCloseMonth = Integer.parseInt(thisCampground.getOpen_to_mm());
		
		if (reservationStartMonth < campgroundOpenMonth) {
			return false;
		} else if (reservationEndMonth > campgroundCloseMonth) {
			return false;
		}else {
			return true;
		}
	
	}

	@Override
	public List<Site> getAvailableSites(Campground campground, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Site> getAllSitesForCampground(Campground campground) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Campground> getCampgroundsByParkId(Park park) {
		String sqlCommand = "SELECT * FROM campground where park_id = " + park.getPark_id();
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlCommand);

		List<Campground> thisCampground = new ArrayList<Campground>();
		while (result.next()) {
			thisCampground.add(mapRowToCampgrounds(result));
		}

		return thisCampground;
	}
	
	@Override
	public BigDecimal getCostByCampgroundId(int id) {
		SqlRowSet result = jdbcTemplate.queryForRowSet("Select * FROM campground where campground.campground_id="+id);
		Campground siteCost = new Campground();
		while(result.next()) {
			siteCost = mapRowToCampgrounds(result);
		}
		return siteCost.getDaily_fee();
	}

	public Campground mapRowToCampgrounds(SqlRowSet result) {
		Campground theCampground = new Campground();

		theCampground.setCampground_id(result.getInt("campground_id"));
		theCampground.setPark_id(result.getInt("park_id"));
		theCampground.setName(result.getString("name"));
		theCampground.setOpen_from_mm(result.getString("open_from_mm"));
		theCampground.setOpen_to_mm(result.getString("open_to_mm"));
		theCampground.setDaily_fee(result.getBigDecimal(("daily_fee")));
		return theCampground;

	}
	
	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
