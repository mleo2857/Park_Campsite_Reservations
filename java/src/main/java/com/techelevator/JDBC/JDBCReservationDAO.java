package com.techelevator.JDBC;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.Reservation;


import DAO.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO() {
		
	}

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void setJDBCTemplate(JdbcTemplate template) {
		this.jdbcTemplate = template;
	}

	@Override
	public void addReservation(List<Object> reservationInfo) {
		
		String sqlCommand = "INSERT INTO reservation(site_id, name, from_date, to_date, create_date)VALUES(?,?,?,?,?);";
		jdbcTemplate.update(sqlCommand, reservationInfo.get(0),reservationInfo.get(1),reservationInfo.get(2), reservationInfo.get(3),new Date());
		SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM reservation ORDER BY reservation_id DESC LIMIT 1");
		Reservation reservation = new Reservation();
		while(results.next()) {
		reservation = mapRowToReservation(results);
		}
		System.out.println("The reservation has been made and the confirmation id is "+reservation.getReservation_id());
	}

	@Override
	public void cancelReservation(Reservation reservation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Reservation> getReservationsBySiteId(int id) {
		List<Reservation> siteReservations = new ArrayList<Reservation>();
		String sqlCommand = "SELECT * FROM reservation where reservation.site_id = " + id;

		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlCommand);
		
		while(result.next()) {
			Reservation aReservation = mapRowToReservation(result);
			siteReservations.add(aReservation);
		}
		
		return siteReservations;
	}
	
	@Override
	public List<Reservation> getReservationsFor30DaysByPark(Park park){
		List<Reservation> reservations = new ArrayList<Reservation>();
		String sqlCommand = "SELECT * FROM reservation JOIN site on reservation.site_id = site.site_id " +
							"JOIN campground on site.campground_id = campground.campground_id " +
							"JOIN park on campground.park_id = park.park_id " +
							"WHERE park.park_id = " + park.getPark_id() + 
							"AND reservation.from_date < (current_date + interval '30 days') " +
							"AND reservation.to_date > current_date";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlCommand);
		
		while(results.next()) {
			reservations.add(mapRowToReservation(results));
		}
		
		
		return reservations;
	}
	
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation theReservation = new Reservation();

		theReservation.setReservation_id(results.getInt("reservation_id"));
		theReservation.setSite_id(results.getInt("site_id"));
		theReservation.setName(results.getString("name"));
		theReservation.setFrom_date(results.getDate("from_date"));
		theReservation.setTo_date(results.getDate("to_date"));
		theReservation.setCreate_date(results.getDate("create_date"));

		return theReservation;
	}

}
