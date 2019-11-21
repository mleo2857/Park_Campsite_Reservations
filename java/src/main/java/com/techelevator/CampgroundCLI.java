package com.techelevator;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.JDBC.JDBCCampgroundDAO;
import com.techelevator.JDBC.JDBCParkDAO;
import com.techelevator.JDBC.JDBCReservationDAO;
import com.techelevator.JDBC.JDBCSiteDAO;
import com.techelevator.model.Campground;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;
import com.techelevator.model.Site;
import com.techelevator.view.Menu;

import DAO.CampgroundDAO;
import DAO.ParkDAO;
import DAO.ReservationDAO;
import DAO.SiteDAO;
import Draw.StdDraw;

public class CampgroundCLI {

	private CampgroundDAO campgroundDAO;
	private ParkDAO parkDAO;
	private ReservationDAO reservationDAO;
	private SiteDAO siteDAO;
	private Menu menu;
	private static final Object[] campgroundOptions = { "View Campgrounds", "Search for Reservation",
			"Return to Previous Screen", "Search Reservations for Next 30 Days" };
	private static final Object[] searchCampgroundOptions = { "Search for Available Reservation",
			"Advanced Search",
			"Return to Previous Screen" };
	private static final DecimalFormat df = new DecimalFormat("###.00");

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		// create your DAOs here
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		parkDAO = new JDBCParkDAO(datasource);
		reservationDAO = new JDBCReservationDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
	}

	public void run() {
		System.out.println(campgroundDAO);
		displayBanner();
		List<Park> parks = parkDAO.viewAllParks();
		ArrayList<String> parkOptions = new ArrayList<String>();

		for (Park park : parks) {
			parkOptions.add(park.getName());
		}
		parkOptions.add("Quit");
		System.out.println("View Parks Interface");
		String parkMenuChoice;
		String choice = "";
		do {
			choice = (String) menu.getChoiceFromOptions(parkOptions.toArray());
			if (choice.equals("Quit")) {
				StdDraw.setScale(1,1);
				StdDraw.picture(0,0,"smoky.jpg");
				System.out.println("Goodbye");
				break;
			}

			do {
				System.out.println("Park Information");
				System.out.println();
				Park chosenPark = parkDAO.getParkByName(choice);
				chosenPark.printParkInfo();
				parkMenuChoice = (String) menu.getChoiceFromOptions(campgroundOptions);
				if (parkMenuChoice.equals("Return to Previous Screen")) {
					break;
				}
				switch (parkMenuChoice) {
				case "View Campgrounds":
					handleViewCampgrounds(chosenPark);
					break;

				case "Search for Reservation":
					handleSearchForReservation(chosenPark);
					break;
					
				case "Search Reservations for Next 30 Days":
					handleSearchForReservationsforNext30Days(chosenPark);
					break;
				}
			} while (true);
			System.out.println();
		} while (true);

	}

	private void handleSearchForReservation(Park park) {
		List<Campground> campgrounds = campgroundDAO.getCampgroundsByParkId(park);
		Date arrivalDate = getArrivalDate();
		Date departureDate = getDepartureDate();
		
		for (Campground campg : campgrounds) {
			Object[] reservationInfo = new Object[3];
			reservationInfo[0] = campg.getCampground_id();
			reservationInfo[1] = arrivalDate;
			reservationInfo[2] = departureDate;
			
			while (((Date) reservationInfo[1]).before(new Date())
					|| (((Date) reservationInfo[2]).before(((Date) reservationInfo[1])))) {
				reservationInfo[1] = getArrivalDate();
				reservationInfo[2] = getDepartureDate();

			}
			
			System.out.println();
			System.out.println(campg.getName());
			System.out.println();
			
			if (campgroundDAO.isOpen(reservationInfo)) {
				List<Site> availableSites = siteDAO.findAvailableSites(reservationInfo);
				int daysStaying = (int) daysBetween((Date) reservationInfo[1], (Date) reservationInfo[2]) + 1;
	
				BigDecimal dailyCost = campgroundDAO.getCostByCampgroundId((int) reservationInfo[0]);
				BigDecimal totalCost = dailyCost.multiply(new BigDecimal(String.valueOf(daysStaying)));

				showAvailableSites(availableSites, totalCost);
			} else {
				System.out.println("Campground not open for given date range");
			}
		}
	}

	private void handleViewCampgrounds(Park park) {
		displayParkCampgrounds(park);
		String searchChoice;
		do {
			searchChoice = (String) menu.getChoiceFromOptions(searchCampgroundOptions);
			if (searchChoice.equals("Return to Previous Screen")) {
				break;
			} else if (searchChoice.equals("Advanced Search")) {
				handleAdvancedSearch(park);
				break;
			}

			Object[] reservationInfo = getReservationSearch(park);
			List<Site> availableSites = new ArrayList<Site>();
			
			while (availableSites.size() == 0) {
				if (campgroundDAO.isOpen(reservationInfo)) {
					availableSites = siteDAO.findAvailableSites(reservationInfo);
				}
	
				if (availableSites.size() == 0) {
					System.out.println("No sites available.");
					reservationInfo = getReservationSearch(park);
				}
			}
			
			int daysStaying = (int) daysBetween((Date) reservationInfo[1], (Date) reservationInfo[2]) + 1;

			BigDecimal dailyCost = campgroundDAO.getCostByCampgroundId((int) reservationInfo[0]);
			BigDecimal totalCost = dailyCost.multiply(new BigDecimal(String.valueOf(daysStaying)));
			showAvailableSites(availableSites, totalCost);

			Object[] siteChoiceAndName = getReservationDetails(availableSites, park);
			List<Object> reservationData = new ArrayList<Object>();
			int siteId = siteDAO.getSiteIdFromCampgroundIdAndSiteNumber((int) reservationInfo[0],
					(int) siteChoiceAndName[0]);
			reservationData.add(siteId); // siteNumber Choice
			reservationData.add(siteChoiceAndName[1]); // name on reservation
			reservationData.add(reservationInfo[1]); // start date
			reservationData.add(reservationInfo[2]); // to date
			reservationDAO.addReservation(reservationData);
		} while (true);
	}

	private void displayBanner() {
		System.out.println("        ,                                     , _                      ");
		System.out.println("       /|/\\    _, _|_ o  _         _,  |\\    /|/ \\ _,   ,_  |)   ,          ");
		System.out.println("        |  |  / |  |  | / \\_/|/|  / |  |/     |__// |  /  | |/) / \\_  ");
		System.out.println("        |  |_/\\/|_/|_/|/\\_/  | |_/\\/|_/|_/    |   \\/|_/   |/| \\/ \\/    ");
		System.out.println("  , _                                                                        ");
		System.out.println(" /|/ \\  _  ,   _  ,_        _, _|_ o  _            ()       , _|_  _         ");
		System.out.println("  |__/ |/ / \\_|/ /  | |  |_/ |  |  | / \\_/|/|      /\\ |  | / \\_|  |/ /|/|/|  ");
		System.out.println("  | \\_/|_/ \\/ |_/   |/ \\/  \\/|_/|_/|/\\_/  | |_/   /(_) \\/|/ \\/ |_/|_/ | | |_/");
		System.out.println();
		System.out.println();

	}

	private static long daysBetween(Date one, Date two) {
		long difference = (one.getTime() - two.getTime()) / 86400000;
		return Math.abs(difference);
	}

	private List<Integer> displayParkCampgrounds(Park park) {
		List<Campground> campgroundList = campgroundDAO.getCampgroundsByParkId(park);
		String format = "%1$-20s%2$-30s%3$-20s%4$-20s%5$-20s\n";
		System.out.println(park.getName() + " National Park Campgrounds");
		System.out.println();
		System.out.printf(format, "Campground Id","Name", "Open", "Close", "Daily Fee");
		
		List<Integer> campgroundIdList = new ArrayList<Integer>();
		for (Campground campground : campgroundList) {
			System.out.printf(format, campground.getCampground_id(), campground.getName(),
					campground.displayMonth(campground.getOpen_from_mm()),
					campground.displayMonth(campground.getOpen_to_mm()), "$" + df.format(campground.getDaily_fee()));
			campgroundIdList.add(campground.getCampground_id());
		}
		return campgroundIdList;
	}

	private Object[] getReservationSearch(Park park) {
		Object[] reservationInfo = new Object[7];

		List<Integer> campgroundIds = displayParkCampgrounds(park);
		int campgroundNumber = getCampgroundChoice();
		if (!campgroundIds.contains(campgroundNumber)) {
			System.out.println("Please enter a valid campground number");
			getCampgroundChoice();
		} else if (campgroundNumber == 0) {
			handleViewCampgrounds(park);
		}
		reservationInfo[0] = campgroundNumber;
		reservationInfo[1] = getArrivalDate();
		reservationInfo[2] = getDepartureDate();
		while (((Date) reservationInfo[1]).before(new Date())
				|| (((Date) reservationInfo[2]).before(((Date) reservationInfo[1])))) {
			reservationInfo[1] = getArrivalDate();
			reservationInfo[2] = getDepartureDate();

		}
		return reservationInfo;
	}
	
	public int getCampgroundChoice() {
		Scanner userInput = new Scanner(System.in);
		System.out.println("Which campground (enter 0 to cancel)?");
		String campground = userInput.nextLine();
		int campgroundNumber = 0;
		try {
			campgroundNumber = Integer.parseInt(campground);
		} catch (NumberFormatException e) {
			System.out.println("Please enter a valid number");
			getCampgroundChoice();
		}
		return campgroundNumber;
	}

	public Date getDepartureDate() {
		Date departure = new Date();
		System.out.println("What is the departure date? 'MM/DD/YYYY'");
		Scanner userInput = new Scanner(System.in);
		String departureDate = userInput.nextLine();
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			df.setLenient(false);
			departure = df.parse(departureDate);
		} catch (ParseException e) {
			System.out.println("Invalid date");
			getDepartureDate();
		}
		return departure;
	}

	public Date getArrivalDate() {
		Scanner userInput = new Scanner(System.in);
		// Date arrival = new Date();
		System.out.println("What is the arrival date? MM/DD/YYYY");
		String arrivalDate = userInput.nextLine();
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			df.setLenient(false);
			Date arrival = df.parse(arrivalDate);
//			if (arrival.before(new Date())) {
//				System.out.println("Invalid arrival date");
//				getArrivalDate(new Scanner(System.in));
//			}
			Date guestArrival = arrival;
			return guestArrival;
		} catch (ParseException e) {
			System.out.println("Invalid date");
			getArrivalDate();
		}
		return new Date();
	}

	public void showAvailableSites(List<Site> siteList, BigDecimal totalCost) {
		int numberOfSites = siteList.size() >= 5 ? 5 : siteList.size();
		String format = "%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n";
		System.out.printf(format, "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utility", "Cost");
		System.out.println();
		for (int i = 0; i < numberOfSites; i++) {
			siteList.get(i).printSiteInfo(totalCost);
		}
	}

	public Object[] getReservationDetails(List<Site> availableSites, Park park) {
		int siteNumber = getSiteNumber();
		List<Integer> siteNumbers = new ArrayList<Integer>();
		for (Site aSite : availableSites) {
			siteNumbers.add(aSite.getSite_number());
		}

		if (siteNumber == 0) { // if they cancel
			handleViewCampgrounds(park);
		}

		while (!siteNumbers.contains(siteNumber)) {
			System.out.println("Please enter a valid site number");
			siteNumber = getSiteNumber();
		}
		String name = getReservationName();
		return new Object[] { siteNumber, name };
	}

	public int getSiteNumber() {

		Scanner userInput = new Scanner(System.in);
		System.out.println("Which site should be reserved (enter 0 to cancel)?");
		String site = userInput.nextLine();
		int siteNumber = 0;
		try {
			siteNumber = Integer.parseInt(site);
			return siteNumber;
		} catch (NumberFormatException e) {
			System.out.println("Please enter a valid number");
		}
		return siteNumber;
	}

	public String getReservationName() {
		Scanner userInput = new Scanner(System.in);
		System.out.println("What name should the reservation be made under?");
		String name = userInput.nextLine();
		return name;
	}
	
	public void handleSearchForReservationsforNext30Days(Park chosenPark) {
		List<Reservation> reservations = reservationDAO.getReservationsFor30DaysByPark(chosenPark);
		String format = "%1$-20s%2$-20s%3$-35s%4$-20s%5$-20s%6$-20s\n";
		System.out.printf(format, "Reservation Id","Site Id","Name","Start Date","End Date","Created");
		for (Reservation res : reservations) {
			res.displayReservation();
		}
	}
	
	public void handleAdvancedSearch(Park park) {
		Object[] reservationInfo = getReservationSearch(park);
		List<Site> availableSites = new ArrayList<Site>();
		
		while (availableSites.size() == 0) {
			if (campgroundDAO.isOpen(reservationInfo)) {
				availableSites = siteDAO.findAvailableSites(reservationInfo);
			}

			if (availableSites.size() == 0) {
				System.out.println("No sites available.");
				reservationInfo = getReservationSearch(park);
			}
		}
		
		reservationInfo[3] = getMaxOccupancy();
		reservationInfo[4] = getAccessible();
		reservationInfo[5] = getMaxRVLength();
		reservationInfo[6] = getUtilities();
		
		List<Site> advancedSearchList = siteDAO.advancedSearchSites(reservationInfo);
		
		int daysStaying = (int) daysBetween((Date) reservationInfo[1], (Date) reservationInfo[2]) + 1;

		BigDecimal dailyCost = campgroundDAO.getCostByCampgroundId((int) reservationInfo[0]);
		BigDecimal totalCost = dailyCost.multiply(new BigDecimal(String.valueOf(daysStaying)));
		showAvailableSites(advancedSearchList, totalCost);
		
		
	}
	
	public int getMaxRVLength() {
		Scanner userInput = new Scanner(System.in);
		// Date arrival = new Date();
		System.out.println("What RV length do you need?");
		String maxRVLength = userInput.nextLine();
		try {
			int rvLength = Integer.parseInt(maxRVLength);
			return rvLength;
		} catch (NumberFormatException e) {
			System.out.println("Invalid RV Length");
			getMaxRVLength();
		}
		return 0;
	}
	
	public boolean getUtilities() {
		Scanner userInput = new Scanner(System.in);
		// Date arrival = new Date();
		System.out.println("Do you need utilities (y/n)");
		String yesOrNo = userInput.nextLine().toLowerCase();
		try {
			if(yesOrNo.equals("y")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Invalid input");
			getUtilities();
		}
		return false;
	}
	
	public boolean getAccessible() {
		System.out.println("Do you want handicap accessibility?");
		try {
			return Boolean.parseBoolean(new Scanner(System.in).nextLine());
		} catch(Exception e) {
			getAccessible();
		}
		return false;
	}
	
	public int getMaxOccupancy() {
	    System.out.println("input max number of occupants.");
	    try {
	        return Integer.parseInt(new Scanner(System.in).nextLine());
	    }
	    catch(Exception e) {
	        getMaxOccupancy();
	    }
	    return 0;
	    
	}
}
