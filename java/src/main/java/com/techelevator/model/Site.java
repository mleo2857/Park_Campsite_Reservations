package com.techelevator.model;

import java.math.BigDecimal;

public class Site {
	private int site_id;
	private int campground_id;
	private int site_number;
	private int max_occupancy;
	private boolean accessible;
	private int max_rv_length;
	private boolean utilities;
	
	public int getSite_id() {
		return site_id;
	}
	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}
	public int getCampground_id() {
		return campground_id;
	}
	public void setCampground_id(int campground_id) {
		this.campground_id = campground_id;
	}
	public int getSite_number() {
		return site_number;
	}
	public void setSite_number(int site_number) {
		this.site_number = site_number;
	}
	public int getMax_occupancy() {
		return max_occupancy;
	}
	public void setMax_occupancy(int max_occupancy) {
		this.max_occupancy = max_occupancy;
	}
	public boolean isAccessible() {
		return accessible;
	}
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	public int getMax_rv_length() {
		return max_rv_length;
	}
	public void setMax_rv_length(int max_rv_length) {
		this.max_rv_length = max_rv_length;
	}
	public boolean isUtilities() {
		return utilities;
	}
	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (accessible ? 1231 : 1237);
		result = prime * result + campground_id;
		result = prime * result + max_occupancy;
		result = prime * result + max_rv_length;
		result = prime * result + site_id;
		result = prime * result + site_number;
		result = prime * result + (utilities ? 1231 : 1237);
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
		Site other = (Site) obj;
		if (accessible != other.accessible)
			return false;
		if (campground_id != other.campground_id)
			return false;
		if (max_occupancy != other.max_occupancy)
			return false;
		if (max_rv_length != other.max_rv_length)
			return false;
		if (site_id != other.site_id)
			return false;
		if (site_number != other.site_number)
			return false;
		if (utilities != other.utilities)
			return false;
		return true;
	}
	
	public void printSiteInfo(BigDecimal totalCost) {
		String format = "%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n";
		System.out.printf(format, this.site_number, this.max_occupancy, this.accessible, this.max_rv_length,this.utilities,"$" + totalCost);
		}
	
	

}
