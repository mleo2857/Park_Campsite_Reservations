package com.techelevator.JDBC;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;

import DAO.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> viewAllParks() {
		List<Park> theParks = new ArrayList<Park>();

		String listAllParksQuery = "select * from park";
		// Execute the SELECT using JdbcTemplate - result store in SqlRowset
		SqlRowSet results = jdbcTemplate.queryForRowSet(listAllParksQuery);

		while (results.next()) {
			Park aPark = mapRowToParks(results);
			theParks.add(aPark);
		}
		return theParks;
	}

	@Override
	public Park getParkByName(String name) {
		String sqlCommand = "SELECT * FROM park where name = '" + name + "'";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlCommand);

		Park thisPark = new Park();
		while (result.next()) {
			thisPark = mapRowToParks(result);
		}

		return thisPark;
	}

	@Override
	public Park selectPark(int parkId) {
		// TODO Auto-generated method stub
		return null;
	}

	private Park mapRowToParks(SqlRowSet results) {
		Park thePark = new Park();

		thePark.setPark_id(results.getInt("park_id"));
		thePark.setName(results.getString("name"));
		thePark.setLocation(results.getString("location"));
		thePark.setEstablish_date(results.getDate("establish_date"));
		thePark.setArea(results.getInt("area"));
		thePark.setVisitors(results.getInt("visitors"));
		thePark.setDescription(results.getString("description"));

		return thePark;
	}

}
