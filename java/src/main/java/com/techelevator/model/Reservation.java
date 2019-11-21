package com.techelevator.model;

import java.util.Date;

public class Reservation {
	private int reservation_id;
	private int site_id;
	private String name;
	private Date from_date;
	private Date to_date;
	private Date create_date;
	
	public int getReservation_id() {
		return reservation_id;
	}
	public void setReservation_id(int reservation_id) {
		this.reservation_id = reservation_id;
	}
	public int getSite_id() {
		return site_id;
	}
	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getFrom_date() {
		return from_date;
	}
	public void setFrom_date(Date from_date) {
		this.from_date = from_date;
	}
	public Date getTo_date() {
		return to_date;
	}
	public void setTo_date(Date to_date) {
		this.to_date = to_date;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((create_date == null) ? 0 : create_date.hashCode());
		result = prime * result + ((from_date == null) ? 0 : from_date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + reservation_id;
		result = prime * result + site_id;
		result = prime * result + ((to_date == null) ? 0 : to_date.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reservation other = (Reservation) obj;
		if (create_date == null) {
			if (other.create_date != null)
				return false;
		} else if (!create_date.equals(other.create_date))
			return false;
		if (from_date == null) {
			if (other.from_date != null)
				return false;
		} else if (!from_date.equals(other.from_date))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (reservation_id != other.reservation_id)
			return false;
		if (site_id != other.site_id)
			return false;
		if (to_date == null) {
			if (other.to_date != null)
				return false;
		} else if (!to_date.equals(other.to_date))
			return false;
		return true;
	}
	
	public void displayReservation() {
		String format = "%1$-20s%2$-20s%3$-35s%4$-20s%5$-20s%6$-20s\n";
		System.out.printf(format, this.reservation_id,this.site_id,this.name,this.from_date,this.to_date,this.create_date);
	}

}
