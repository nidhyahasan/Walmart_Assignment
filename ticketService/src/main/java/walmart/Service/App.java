
// implement for multi users - multi threading
//readme text

package walmart.Service;

import java.util.Optional;
import java.util.Scanner;

import walmart.Model.SeatHold;

public class App {
	private static Scanner scanner;

	public static void main(String[] args) {
		TicketService service1 = new TicketServiceImpl();
		int option, numOfSeats, seatsCount;
		Integer levelID = null, minLevel = null, maxLevel = null;
		String email, seatHoldID;
		scanner = new Scanner(System.in);
		while (true) {
			System.out.println("-------------------------------------------------");
			System.out.println("1. Enter 1 to view number of seats available per Level");
			System.out.println("5. Enter 5 to view TOTAL number of seats");
			System.out.println("2. Enter 2 to view the seats available and hold the seats based on min/max levels");
			System.out.println("6. Enter 6 to view and hold seats on any available level");
			System.out.println("3. Enter 3 to reserve the seats");
			System.out.println("4. Enter 0 to exit");
			System.out.println("-------------------------------------------------");
			try {
				option = scanner.nextInt();
				if (option != 1 && option != 2 && option != 3 && option != 0 && option != 5 && option != 6) {
					continue;
				}
			} catch (Exception e) {
				scanner.next();
				continue;
			}

			// Terminate
			if (option == 0) {
				System.out.println("-------------------------------------------------");
				System.out.println("Thanks for showing interest in booking tickets");
				System.out.println("-------------------------------------------------");
				System.exit(0);
			} else if (option == 5) {
				levelID = null;
				// levelIDop = Optional.ofNullable(levelID);
				numOfSeats = service1.numSeatsAvailable(Optional.ofNullable(levelID));
				System.out.println("-------------------------------------------------");
				System.out.println("TOTAL Number of Seats Available :: " + numOfSeats);
				System.out.println("-------------------------------------------------");
				// view available seats
			} else if (option == 1) {
				while (true) {
					System.out.println("-------------------------------------------------");
					System.out.println("Enter level ID to view number of available seats");
					System.out.println("1. Orchestra");
					System.out.println("2. Main");
					System.out.println("3. Balcony 1");
					System.out.println("4. Balcony 2");
					System.out.println("0. Main Menu");
					System.out.println("-------------------------------------------------");
					try {

						// waiting to get LEVEL ID
						levelID = scanner.nextInt();
						// levelIDop = Optional.ofNullable(levelID);
						if (levelID != 1 && levelID != 2 && levelID != 3 && levelID != 4 && levelID != 0) {
							continue;
						}
					} catch (Exception e) {
						scanner.next();
						continue;
					}

					// GO TO MAIN MENU
					if (levelID == 0)
						break;

					// available seats based on LEVEL ID
					else {
						// levelIDop = Optional.ofNullable(levelID);
						numOfSeats = service1.numSeatsAvailable(Optional.ofNullable(levelID));
						System.out.println("-------------------------------------------------");
						System.out.println("Number of Seats Available for Level ID " + levelID + ": " + numOfSeats);
						System.out.println("-------------------------------------------------");
					}
				}

				// OPTION TO HOLD THE SEATS
			} else if (option == 2 || option == 6) {

				System.out.println("-------------------------------------------------");
				System.out.println("Enter number of Seats to Hold");
				seatsCount = scanner.nextInt();
				if (option == 2) {
					System.out.println("Enter Minimum LevelID to hold the seats");
					minLevel = scanner.nextInt();
					System.out.println("Enter Maximum LevelID to hold the seats");
					maxLevel = scanner.nextInt();
				}
				System.out.println("Enter your Email ID to send you notification");
				System.out.println("-------------------------------------------------");
				email = scanner.next();

				SeatHold seatsHeld = service1.findAndHoldSeats(seatsCount, Optional.ofNullable(minLevel),
						Optional.ofNullable(maxLevel), email);
				if (seatsHeld != null) {
					System.out.println("-------------------------------------------------");
					System.out.println("Seats Held::");
					System.out.println("ID:" + seatsHeld.getSeatHoldId());
					System.out.println("LEVEL ID:" + seatsHeld.getLevelID());
					System.out.println("Price:" + seatsHeld.getPurchasePrice());
					System.out.print("Seats:");
					for (Integer i : seatsHeld.getSeats()) {
						System.out.print(i + " , ");
					}

					System.out.println("-------------------------------------------------");
					System.out.println("Please save the ID for reserving the seats");
					System.out.println("-------------------------------------------------");

					// THIS THREAD IS CREATED TO HOLD THE SEATS FOR 1 MINUTE
					// before
					// releasing to release POOL
					FindandHoldSeats findthread = new FindandHoldSeats(seatsHeld);
					Thread objThread = new Thread(findthread);
					objThread.setName(seatsHeld.getSeatHoldId());
					objThread.start();
				} else {
					System.out.println("Sorry seats not available, Please rehold and book");
				}

				// THIS IS TO RESERVE THE SEATS
			} else if (option == 3) {

				System.out.println("-------------------------------------------------");
				System.out.println("Enter SeatHold ID to reserve your seats");
				seatHoldID = scanner.next();
				System.out.println("Enter your Email ID to send you notification");
				System.out.println("-------------------------------------------------");
				email = scanner.next();
				String reservationCode = service1.reserveSeats(seatHoldID, email);
				if (reservationCode != null) {
					System.out.println("Please show this ReservationCode to print the tickets :: " + reservationCode);
				} else {
					System.out.println("Sorry...Please hold the tickets again and book within the time limit");
				}
			}

		}
	}

}
