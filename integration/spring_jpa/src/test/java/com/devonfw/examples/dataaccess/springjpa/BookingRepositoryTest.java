package com.devonfw.examples.dataaccess.springjpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devonfw.examples.dataaccess.springjpa.domain.model.BookingEntity;
import com.devonfw.examples.dataaccess.springjpa.domain.model.BookingStatus;
import com.devonfw.examples.dataaccess.springjpa.domain.model.InvitedGuestEntity;
import com.devonfw.examples.dataaccess.springjpa.domain.repository.BookingRepository;

/**
 * Unit test prepared to test the following features:
 *
 * LocalDateTime - BookingDate is time.LocalDateTime instead of util.Date
 *
 * Enum (using converter) - BookingStatus is assigned BOOKED but
 *               the database stores only the code 'B'.  To verify actual value (code) from the database, applying native
 *               query to retrieve row data.
 *
 * Lazy loading - Lazy loading verified by looking at queries applied against database from the logs.
 * 				 While applying findById executes query to fetch data from booking table not querying against invited
 * 				 guests. But when we verify `getInvitedGuests().size()` the invited guests query executes.
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BookingRepositoryTest {

	@Autowired
	private BookingRepository bookingRepository;

	@PersistenceContext
	private EntityManager em;

	@Test
	@Transactional
	public void givenBooking_whenSave_thenGetOk() {

		// Booking + Invited Guests creation
		BookingEntity newBooking = getBooking("booking1");
		BookingEntity booking = this.bookingRepository.save(newBooking);

		// Retrieving the booking:
		Optional<BookingEntity> foundBooking = this.bookingRepository
				.findById(booking.getId());
		if (foundBooking.isPresent()) {
			booking = foundBooking.get();
			assertEquals(booking.getName(), newBooking.getName());
			assertEquals(booking.getBookingDate(), newBooking.getBookingDate());
			assertEquals(booking.getInvitedGuests().size(), 10);
		}

		// Native query used here to get the exact Enum data from the DB.

		Query q = this.em.createNativeQuery(
				"SELECT b.name,b.booking_status FROM Booking b where id="
						+ booking.getId());
		List<Object[]> bookings = q.getResultList();
		assertEquals(bookings.size(), 1);
		for (Object[] bookingObject : bookings) {
			assertEquals(bookingObject[0], newBooking.getName());
			assertEquals(bookingObject[1], BookingStatus.BOOKED.getCode());
		}

	}

	private BookingEntity getBooking(String bookingName) {

		BookingEntity booking = new BookingEntity();
		booking.setName(bookingName);
		booking.setBookingStatus(BookingStatus.BOOKED);
		booking.setBookingDate(LocalDateTime.now());
		booking.setName(bookingName);
		booking.setBookingToken("token");
		booking.setComment("comment");
		booking.setEmail(bookingName + "@gmail.com");
		booking.setInvitedGuests(getInvitedGuest(bookingName));
		return booking;
	}

	private Set<InvitedGuestEntity> getInvitedGuest(String bookingName) {

		Set<InvitedGuestEntity> guests = new HashSet<>();
		for (int i = 0; i < 10; i++) {
			InvitedGuestEntity guest = new InvitedGuestEntity();
			guest.setGuestToken(
					String.format("%s-guest%stoken", bookingName, i));
			guest.setEmail(
					String.format("%s.user%s@gmail.com", bookingName, i));
			guests.add(guest);
		}
		return guests;
	}

}
