package walmart.ticketService;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;

import walmart.Service.TicketService;
import walmart.Service.TicketServiceImpl;

public class TESTSeatsAvailable {

	@Test
	public void seatsAvailableforOrchestra() {

		TicketService service1 = new TicketServiceImpl();
		int seats = service1.numSeatsAvailable(Optional.ofNullable(1));
		assertNotNull("Orchestra seats are null", seats);
		assertEquals(1250, seats);

	}

	@Test
	public void seatsAvailableforMain() {

		TicketService service1 = new TicketServiceImpl();
		int seats = service1.numSeatsAvailable(Optional.ofNullable(2));
		assertNotNull("Main seats are null", seats);
		assertEquals(2000, seats);

	}

	@Test
	public void seatsAvailableforBalcony1() {

		TicketService service1 = new TicketServiceImpl();
		int seats = service1.numSeatsAvailable(Optional.ofNullable(3));
		assertNotNull("Balcony 1 seats are null", seats);
		assertEquals(1500, seats);

	}

	@Test
	public void seatsAvailableforBalcony2() {

		TicketService service1 = new TicketServiceImpl();
		int seats = service1.numSeatsAvailable(Optional.ofNullable(4));
		assertNotNull("Balcony 2 seats are null", seats);
		assertEquals(1500, seats);

	}

	@Test
	public void testfailureSeatsAvailable() {

		TicketService service1 = new TicketServiceImpl();
		int seats = service1.numSeatsAvailable(Optional.ofNullable(5));
		assertEquals(0, seats);

	}
}
