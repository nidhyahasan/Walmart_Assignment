package walmart.ticketService;

import static org.junit.Assert.*;

import java.util.Optional;

import walmart.Model.SeatHold;
import walmart.Service.TicketService;
import walmart.Service.TicketServiceImpl;

import org.junit.Test;

public class TestSeatstoHold {

	@Test
	public void seatstoHoldInOrchestra() {
		TicketService service1 = new TicketServiceImpl();
		SeatHold s = service1.findAndHoldSeats(100, Optional.ofNullable(1), Optional.ofNullable(1), "xxx@yyy.com");
		assertNotNull("SeatHold object is null", s);
		assertNotNull("seatHoldID is null", s.getSeatHoldId());
		assertNotNull("seata are null", s.getSeats());
		assertNotNull("purchase price is null", s.getPurchasePrice());
		assertNotNull("LevelID is null", s.getLevelID());
		assertNotNull("email is null", s.getEmail());

		int remainingSeatsAvailable = service1.numSeatsAvailable(Optional.ofNullable(1));
		assertEquals(1150, remainingSeatsAvailable);

	}

	@Test
	public void seatstoHoldInMain() {
		TicketService service1 = new TicketServiceImpl();
		SeatHold s = service1.findAndHoldSeats(100, Optional.ofNullable(2), Optional.ofNullable(2), "opx@yyy.com");
		assertNotNull("SeatHold object is null", s);
		assertNotNull("seatHoldID is null", s.getSeatHoldId());
		assertNotNull("seata are null", s.getSeats());
		assertNotNull("purchase price is null", s.getPurchasePrice());
		assertNotNull("LevelID is null", s.getLevelID());
		assertNotNull("email is null", s.getEmail());

		int remainingSeatsAvailable = service1.numSeatsAvailable(Optional.ofNullable(2));
		assertEquals(1900, remainingSeatsAvailable);

	}

	@Test
	public void seatstoHoldInBalcony1() {
		TicketService service1 = new TicketServiceImpl();
		SeatHold s = service1.findAndHoldSeats(200, Optional.ofNullable(3), Optional.ofNullable(3), "balcony@yyy.com");
		assertNotNull("SeatHold object is null", s);
		assertNotNull("seatHoldID is null", s.getSeatHoldId());
		assertNotNull("seata are null", s.getSeats());
		assertNotNull("purchase price is null", s.getPurchasePrice());
		assertNotNull("LevelID is null", s.getLevelID());
		assertNotNull("email is null", s.getEmail());

		int remainingSeatsAvailable = service1.numSeatsAvailable(Optional.ofNullable(3));
		assertEquals(1300, remainingSeatsAvailable);

	}

	@Test
	public void seatstoHoldInBalcony2() {
		TicketService service1 = new TicketServiceImpl();
		SeatHold s = service1.findAndHoldSeats(300, Optional.ofNullable(4), Optional.ofNullable(4),
				"balcony22@yyy.com");
		assertNotNull("SeatHold object is null", s);
		assertNotNull("seatHoldID is null", s.getSeatHoldId());
		assertNotNull("seata are null", s.getSeats());
		assertNotNull("purchase price is null", s.getPurchasePrice());
		assertNotNull("LevelID is null", s.getLevelID());
		assertNotNull("email is null", s.getEmail());

		int remainingSeatsAvailable = service1.numSeatsAvailable(Optional.ofNullable(4));
		assertEquals(1200, remainingSeatsAvailable);

	}
	
	@Test
	public void seatstoHoldAnyLevel() {
		TicketService service = new TicketServiceImpl();
		Integer min = null,max = null;
		SeatHold s = service.findAndHoldSeats(450, Optional.ofNullable(min), Optional.ofNullable(max),
				"balcony22@yyy.com");
		assertNotNull("SeatHold object is null", s);
		assertNotNull("seatHoldID is null", s.getSeatHoldId());
		assertNotNull("seata are null", s.getSeats());
		assertNotNull("purchase price is null", s.getPurchasePrice());
		assertNotNull("LevelID is null", s.getLevelID());
		assertNotNull("email is null", s.getEmail());

		int remainingSeatsAvailable = service.numSeatsAvailable(Optional.ofNullable(1));
		assertEquals(700, remainingSeatsAvailable);

	}
}
