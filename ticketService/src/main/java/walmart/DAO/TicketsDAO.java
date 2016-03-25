package walmart.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import walmart.Model.SeatHold;

public class TicketsDAO {

	public static Map<String, Integer> level1 = new HashMap<String, Integer>();
	static {
		level1.put("available", 1250);
		level1.put("hold", 0);
		level1.put("reserved", 0);
	}
	public static Map<String, Integer> level2 = new HashMap<String, Integer>();
	static {
		level2.put("available", 2000);
		level2.put("hold", 0);
		level2.put("reserved", 0);
	}
	public static Map<String, Integer> level3 = new HashMap<String, Integer>();
	static {
		level3.put("available", 1500);
		level3.put("hold", 0);
		level3.put("reserved", 0);
	}
	public static Map<String, Integer> level4 = new HashMap<String, Integer>();
	static {
		level4.put("available", 1500);
		level4.put("hold", 0);
		level4.put("reserved", 0);
	}

	public static ArrayList<Integer> orchestra_reserved = new ArrayList<Integer>();
	public static ArrayList<Integer> main_reserved = new ArrayList<Integer>();
	public static ArrayList<Integer> balcony1_reserved = new ArrayList<Integer>();
	public static ArrayList<Integer> balcony2_reserved = new ArrayList<Integer>();

	/*
	 * Method to reserve the seats and return the ReservationCode
	 */
	public String reserveSeats(SeatHold s, ArrayList<Integer> reserved,
			Map<String, Integer> level, LinkedList<SeatHold> seatsHeld) {

		String reservationCode = s.getSeatHoldId();
		synchronized (this) {
			for (Integer i : s.getSeats()) {
				reserved.add(i);
			}
			System.out.println("-------------------------------------------------");
			System.out.println("Seats Reserved::");
			System.out.println("ID:" + s.getSeatHoldId());
			System.out.println("LEVEL ID:" + s.getLevelID());
			System.out.println("Price:" + s.getPurchasePrice());
			System.out.print("Seats:");
			for (Integer j : s.getSeats()) {
				System.out.println(j);
			}
			System.out.println("-------------------------------------------------");
			/*
			 * After reservation, decrease the count of hold array and increase
			 * the count of reserved array
			 */
			level.put("hold", level.get("hold") - s.getSeats().size());
			level.put("reserved", level.get("reserved") + s.getSeats().size());

			seatsHeld.remove(s);

		}
		return reservationCode;
	}

	/*
	 * Before holding the seats, Make sure the seats are not available in hold
	 * array and reserved array
	 */
	public SeatHold getSeatsToHold(int numSeats, String customerEmail,
			LinkedList<Integer> seatsReleased, int price, int rows, int seats,
			ArrayList<Integer> seatsReserved, LinkedList<SeatHold> seatsHeld, int levelID) {
		SeatHold obj = new SeatHold();
		boolean available = false;
		ArrayList<Integer> seatstoHold = new ArrayList<Integer>();
		UUID id = UUID.randomUUID();

		/*
		 * If the number of seats requested to hold is less than the release
		 * array, then return seats from release array
		 */
		if (!seatsReleased.isEmpty() && numSeats <= seatsReleased.size()) {
			for (int i = 0; i < numSeats; i++) {
				seatstoHold.add(seatsReleased.get(0));
				seatsReleased.remove(0);
			}
			obj.setSeatHoldId(id.toString());
			obj.setEmail(customerEmail);
			obj.setLevelID(levelID);
			obj.setPurchasePrice(price * numSeats);
			obj.setSeats(seatstoHold);

			/*
			 * If number of seats requested is greater than the seats available
			 * in release array
			 */
		} else {
			// iterate every seat in that LEVEL
			for (int seat = 1; seat <= (rows * seats); seat++) {
				available = false;
				/*
				 * Iterate reserved array and check if the seat to be held is
				 * available in reserved array
				 */
				for (Integer i : seatsReserved) {
					if (seat == seatsReserved.get(i - 1)) {
						available = true;
						break;
					}
				} // end of for loop for reserved array Iteration
				if (!available) {
					ArrayList<Integer> totalSeatsHeld = new ArrayList<Integer>();
					for (SeatHold s : seatsHeld) {
						totalSeatsHeld.addAll(s.getSeats());
					}
					/*
					 * Iterate Hold array and check if the seat to be held is
					 * available in Hold array
					 */
					for (Integer j : totalSeatsHeld) {
						if (seat == j) {
							available = true;
							break;
						}
					} // end of for loop for hold array Iteration

				}
				if (!available) {
					/*
					 * Iterate release array and check if the seat to be held is
					 * available in release array
					 */
					for (Integer k : seatsReleased) {
						if (seat == k) {
							available = true;
							break;
						}
					}
				}
				if (!available) {
					seatstoHold.add(seat);
					if (seatstoHold.size() == numSeats) {
						break;
					}
				}
			}
			if (seatstoHold.size() != numSeats) {

			} else {
				obj.setSeatHoldId(id.toString());
				obj.setEmail(customerEmail);
				obj.setLevelID(levelID);
				obj.setPurchasePrice(price * numSeats);
				obj.setSeats(seatstoHold);
			}
		}
		return obj;
	}

	public List<Integer> getSeatsReservedinOrchestra() {

		// Fetch from DB, return the list of seats reserved in Orchestra
		return orchestra_reserved;
	}

	public List<Integer> getSeatsReservedinMain() {
		// Fetch from DB, return the list of seats reserved in Main
		return main_reserved;

	}

	public List<Integer> getSeatsReservedinBalcony1() {
		// Fetch from DB, return the list of seats reserved in Balcony1
		return balcony1_reserved;

	}

	public List<Integer> getSeatsReservedinBalcony2() {
		// Fetch from DB, return the list of seats reserved in Balcony2
		return balcony2_reserved;

	}
}
