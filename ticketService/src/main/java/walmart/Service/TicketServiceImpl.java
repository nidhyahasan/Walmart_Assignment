package walmart.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import walmart.DAO.TicketsDAO;
import walmart.Model.LevelInfo;
import walmart.Model.SeatHold;

public class TicketServiceImpl implements TicketService {

	public static LinkedList<SeatHold> orchestra_seatsHeld = new LinkedList<SeatHold>();
	public static LinkedList<SeatHold> main_seatsHeld = new LinkedList<SeatHold>();
	public static LinkedList<SeatHold> balcony1_seatsHeld = new LinkedList<SeatHold>();
	public static LinkedList<SeatHold> balcony2_seatsHeld = new LinkedList<SeatHold>();

	public static LinkedList<Integer> orchestra_seatsReleased = new LinkedList<Integer>();
	public static LinkedList<Integer> main_seatsReleased = new LinkedList<Integer>();
	public static LinkedList<Integer> balcony1_seatsReleased = new LinkedList<Integer>();
	public static LinkedList<Integer> balcony2_seatsReleased = new LinkedList<Integer>();
	TicketsDAO ticketsDAO = new TicketsDAO();

	/*
	 * (non-Javadoc)
	 * 
	 * @see myproject.service.TicketService#numSeatsAvailable(java.lang.Integer)
	 */
	public int numSeatsAvailable(Optional<Integer> venueLevel) {

		if (!venueLevel.isPresent()) {
			return TicketsDAO.level1.get("available").intValue() + TicketsDAO.level2.get("available").intValue()
					+ TicketsDAO.level3.get("available").intValue() + TicketsDAO.level4.get("available").intValue();
		}
		if (venueLevel.get() == 1) {
			return TicketsDAO.level1.get("available").intValue();
		} else if (venueLevel.get() == 2) {
			return TicketsDAO.level2.get("available").intValue();
		} else if (venueLevel.get() == 3) {
			return TicketsDAO.level3.get("available").intValue();
		} else if (venueLevel.get() == 4) {
			return TicketsDAO.level4.get("available").intValue();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see myproject.service.TicketService#findAndHoldSeats(int, int, int,
	 * java.lang.String)
	 */
	public synchronized SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevelOp,
			Optional<Integer> maxLevelOp, String customerEmail) {
		Integer minLevel, maxLevel;
		if (minLevelOp.isPresent() && maxLevelOp.isPresent()) {
			minLevel = minLevelOp.get();
			maxLevel = maxLevelOp.get();
		} else {
			minLevel = 1;
			maxLevel = 4;
		}
		SeatHold seatHold = holdSeats(numSeats, minLevel, maxLevel, customerEmail);

		if (seatHold != null) {
			synchronized (this) {
				if (seatHold.getLevelID() == 1) {
					TicketServiceImpl.orchestra_seatsHeld.add(seatHold);
					TicketsDAO.level1.put("available", TicketsDAO.level1.get("available") - seatHold.getSeats().size());
					TicketsDAO.level1.put("hold", TicketsDAO.level1.get("hold") + seatHold.getSeats().size());
				} else if (seatHold.getLevelID() == 2) {
					TicketServiceImpl.main_seatsHeld.add(seatHold);
					TicketsDAO.level2.put("available", TicketsDAO.level2.get("available") - seatHold.getSeats().size());
					TicketsDAO.level2.put("hold", TicketsDAO.level2.get("hold") + seatHold.getSeats().size());
				} else if (seatHold.getLevelID() == 3) {
					TicketServiceImpl.balcony1_seatsHeld.add(seatHold);
					TicketsDAO.level3.put("available", TicketsDAO.level3.get("available") - seatHold.getSeats().size());
					TicketsDAO.level3.put("hold", TicketsDAO.level3.get("hold") + seatHold.getSeats().size());
				} else {
					TicketServiceImpl.balcony2_seatsHeld.add(seatHold);
					TicketsDAO.level4.put("available", TicketsDAO.level4.get("available") - seatHold.getSeats().size());
					TicketsDAO.level4.put("hold", TicketsDAO.level4.get("hold") + seatHold.getSeats().size());
				}
			}
		}

		return seatHold;
	}

	public SeatHold holdSeats(int numSeats, int minLevel, int maxLevel, String customerEmail) {

		int seatsAvailable;
		if (maxLevel - minLevel >= 0) {
			for (int level = minLevel; level <= maxLevel; level++) {
				seatsAvailable = numSeatsAvailable(Optional.ofNullable(level));
				if (numSeats <= seatsAvailable) {
					SeatHold seatHold = getSeatsToHold(numSeats, level, customerEmail);
					if (seatHold != null)
						return seatHold;
				}

			}
		}
		return null;
	}

	SeatHold getSeatsToHold(int numSeats, int level, String customerEmail) {

		if (level == 1) {
			SeatHold seatHold = ticketsDAO.getSeatsToHold(numSeats, customerEmail, orchestra_seatsReleased,
					LevelInfo.ORCHESTRA.getPrice(), LevelInfo.ORCHESTRA.getRows(), LevelInfo.ORCHESTRA.getSeats(),
					TicketsDAO.orchestra_reserved, orchestra_seatsHeld, 1);
			return seatHold;
		}

		if (level == 2) {
			SeatHold seatHold = ticketsDAO.getSeatsToHold(numSeats, customerEmail, main_seatsReleased,
					LevelInfo.MAIN.getPrice(), LevelInfo.MAIN.getRows(), LevelInfo.MAIN.getSeats(),
					TicketsDAO.main_reserved, main_seatsHeld, 2);
			return seatHold;
		}
		if (level == 3) {
			SeatHold seatHold = ticketsDAO.getSeatsToHold(numSeats, customerEmail, balcony1_seatsReleased,
					LevelInfo.BALCONY1.getPrice(), LevelInfo.BALCONY1.getRows(), LevelInfo.BALCONY1.getSeats(),
					TicketsDAO.balcony1_reserved, balcony1_seatsHeld, 3);
			return seatHold;
		}
		if (level == 4) {
			SeatHold seatHold = ticketsDAO.getSeatsToHold(numSeats, customerEmail, balcony2_seatsReleased,
					LevelInfo.BALCONY2.getPrice(), LevelInfo.BALCONY2.getRows(), LevelInfo.BALCONY2.getSeats(),
					TicketsDAO.balcony2_reserved, balcony2_seatsHeld, 4);
			return seatHold;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see myproject.service.TicketService#reserveSeats(java.lang.String,
	 * java.lang.String)
	 */
	public String reserveSeats(String seatHoldId, String customerEmail) {

		for (SeatHold s : orchestra_seatsHeld) {
			if (seatHoldId.equalsIgnoreCase(s.getSeatHoldId()) && customerEmail.equalsIgnoreCase(s.getEmail())) {
				String reservationCode = ticketsDAO.reserveSeats(s, TicketsDAO.orchestra_reserved, TicketsDAO.level1,
						orchestra_seatsHeld);
				return reservationCode;
			}
		} // end for loop

		for (SeatHold s : main_seatsHeld) {
			if (seatHoldId.equalsIgnoreCase(s.getSeatHoldId()) && customerEmail.equalsIgnoreCase(s.getEmail())) {
				String reservationCode = ticketsDAO.reserveSeats(s, TicketsDAO.main_reserved, TicketsDAO.level2,
						main_seatsHeld);
				return reservationCode;
			}
		} // end for loop

		for (SeatHold s : balcony1_seatsHeld) {
			if (seatHoldId.equalsIgnoreCase(s.getSeatHoldId()) && customerEmail.equalsIgnoreCase(s.getEmail())) {

				String reservationCode = ticketsDAO.reserveSeats(s, TicketsDAO.balcony1_reserved, TicketsDAO.level3,
						balcony1_seatsHeld);
				return reservationCode;
			}
		} // end for loop

		for (SeatHold s : balcony2_seatsHeld) {
			if (seatHoldId.equalsIgnoreCase(s.getSeatHoldId()) && customerEmail.equalsIgnoreCase(s.getEmail())) {

				String reservationCode = ticketsDAO.reserveSeats(s, TicketsDAO.balcony2_reserved, TicketsDAO.level4,
						balcony2_seatsHeld);
				return reservationCode;
			}
		} // end for loop
		return null;
	}
}
