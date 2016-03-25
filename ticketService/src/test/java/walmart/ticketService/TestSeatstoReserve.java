package walmart.ticketService;

import static org.junit.Assert.*;

import java.util.Optional;

import walmart.Model.SeatHold;
import walmart.Service.TicketService;
import walmart.Service.TicketServiceImpl;

import org.junit.Test;

public class TestSeatstoReserve {

	@Test
	public void reserveSeats() {
		TicketService service1 = new TicketServiceImpl();
		SeatHold s = service1.findAndHoldSeats(5, Optional.ofNullable(4), Optional.ofNullable(4), "xxx@yyy.com");
		String reserveCode = service1.reserveSeats(s.getSeatHoldId(), s.getEmail());
		assertNotNull("reserveCode is null", reserveCode);
	}
}
