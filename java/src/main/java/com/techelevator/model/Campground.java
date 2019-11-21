package com.techelevator.model;

import java.math.BigDecimal;

public class Campground {
	private int campground_id;
	private int park_id;
	private String name;
	private String open_from_mm;
	private String open_to_mm;
	private BigDecimal daily_fee;
	
	public int getCampground_id() {
		return campground_id;
	}
	public void setCampground_id(int campground_id) {
		this.campground_id = campground_id;
	}
	public int getPark_id() {
		return park_id;
	}
	public void setPark_id(int park_id) {
		this.park_id = park_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpen_from_mm() {
		return open_from_mm;
	}
	public void setOpen_from_mm(String open_from_mm) {
		this.open_from_mm = open_from_mm;
	}
	public String getOpen_to_mm() {
		return open_to_mm;
	}
	public void setOpen_to_mm(String open_to_mm) {
		this.open_to_mm = open_to_mm;
	}
	public BigDecimal getDaily_fee() {
		return daily_fee;
	}
	public void setDaily_fee(BigDecimal daily_fee) {
		this.daily_fee = daily_fee;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + campground_id;
		result = prime * result + ((daily_fee == null) ? 0 : daily_fee.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((open_from_mm == null) ? 0 : open_from_mm.hashCode());
		result = prime * result + ((open_to_mm == null) ? 0 : open_to_mm.hashCode());
		result = prime * result + park_id;
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
		Campground other = (Campground) obj;
		if (campground_id != other.campground_id)
			return false;
		if (daily_fee == null) {
			if (other.daily_fee != null)
				return false;
		} else if (!daily_fee.equals(other.daily_fee))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (open_from_mm == null) {
			if (other.open_from_mm != null)
				return false;
		} else if (!open_from_mm.equals(other.open_from_mm))
			return false;
		if (open_to_mm == null) {
			if (other.open_to_mm != null)
				return false;
		} else if (!open_to_mm.equals(other.open_to_mm))
			return false;
		if (park_id != other.park_id)
			return false;
		return true;
	}
	
	public void print() {
		System.out.println("Park Campgrounds");
		System.out.println("Park Campgrounds");
	}
	
	public String displayMonth(String month) {
		switch(month) {
		case "01":
			return "January";
		case "02":
			return "February";
		case "03":
			return "March";
		case "04":
			return "April";
		case "05":
			return "May";
		case "06":
			return "June";
		case "07":
			return "July";
		case "08":
			return "August";
		case "09":
			return "September";
		case "10":
			return "October";
		case "11":
			return "November";
		case "12":
			return "December";
		default:
			return "Error";
		}
	}
	
	
	

}
