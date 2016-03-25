package walmart.Service;

import walmart.DAO.TicketsDAO;
import walmart.Model.SeatHold;

/*
 * This class handles the ticket to hold for 1 minute 
 * and moves to release array if the ticket is not 
 * reserved within the time frame
 * 
 * */
public class FindandHoldSeats implements Runnable {

	SeatHold seatHold;

	protected FindandHoldSeats(SeatHold seatHold) {
		this.seatHold = seatHold;
	}

	public void run() {
		Thread thread = Thread.currentThread();
		try {
			// Thread sleeps for 1 minute to hold the ticket
			thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * Even After a minute, if the ticket is still not reserved, then remove
		 * seatHold object from Hold array and move to release array
		 */
		synchronized (this) {
			if (seatHold.getLevelID() == 1) {
				if (TicketServiceImpl.orchestra_seatsHeld.contains(seatHold)) {
					System.out.println("I'm sorry...The seats held for seatHoldID " + Thread.currentThread().getName()
							+ " is timed out. You cannot reserve those seats now!!!");
					System.out.println("-------------------------------------------------");
					TicketServiceImpl.orchestra_seatsReleased.addAll(seatHold.getSeats());
					TicketServiceImpl.orchestra_seatsHeld.remove(seatHold);
					TicketsDAO.level1.put("available", TicketsDAO.level1.get("available") + seatHold.getSeats().size());
					TicketsDAO.level1.put("hold", TicketsDAO.level1.get("hold") - seatHold.getSeats().size());
				}
			} else if (seatHold.getLevelID() == 2) {
				if (TicketServiceImpl.main_seatsHeld.contains(seatHold)) {
					System.out.println("I'm sorry...The seats held for seatHoldID " + Thread.currentThread().getName()
							+ " is timed out. You cannot reserve those seats now!!!");
					System.out.println("-------------------------------------------------");
					TicketServiceImpl.main_seatsReleased.addAll(seatHold.getSeats());
					TicketServiceImpl.main_seatsHeld.remove(seatHold);
					TicketsDAO.level2.put("available", TicketsDAO.level2.get("available") + seatHold.getSeats().size());
					TicketsDAO.level2.put("hold", TicketsDAO.level2.get("hold") - seatHold.getSeats().size());
				}
			} else if (seatHold.getLevelID() == 3) {
				if (TicketServiceImpl.balcony1_seatsHeld.contains(seatHold)) {
					System.out.println("I'm sorry...The seats held for seatHoldID " + Thread.currentThread().getName()
							+ " is timed out. You cannot reserve those seats now!!!");
					System.out.println("-------------------------------------------------");
					TicketServiceImpl.balcony1_seatsReleased.addAll(seatHold.getSeats());
					TicketServiceImpl.balcony1_seatsHeld.remove(seatHold);
					TicketsDAO.level3.put("available", TicketsDAO.level3.get("available") + seatHold.getSeats().size());
					TicketsDAO.level3.put("hold", TicketsDAO.level3.get("hold") - seatHold.getSeats().size());
				}
			} else {
				if (TicketServiceImpl.balcony2_seatsHeld.contains(seatHold)) {
					System.out.println("I'm sorry...The seats held for seatHoldID " + Thread.currentThread().getName()
							+ " is timed out. You cannot reserve those seats now!!!");
					System.out.println("-------------------------------------------------");
					TicketServiceImpl.balcony2_seatsReleased.addAll(seatHold.getSeats());
					TicketServiceImpl.balcony2_seatsHeld.remove(seatHold);
					TicketsDAO.level4.put("available", TicketsDAO.level4.get("available") + seatHold.getSeats().size());
					TicketsDAO.level4.put("hold", TicketsDAO.level4.get("hold") - seatHold.getSeats().size());
				}
			}
		}

	}
}
