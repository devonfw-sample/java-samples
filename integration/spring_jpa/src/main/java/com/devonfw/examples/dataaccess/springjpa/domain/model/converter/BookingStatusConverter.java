package com.devonfw.examples.dataaccess.springjpa.domain.model.converter;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.devonfw.examples.dataaccess.springjpa.domain.model.BookingStatus;
/**
 * BookingStatusConverter defines a mapping between BookinStatus enum and the
 * database column booking_status at booking table.
 *
 */
@Converter(autoApply = true)
public class BookingStatusConverter
		implements
			AttributeConverter<BookingStatus, String> {

	/**
	 * Converts BookingStatus to string 'code', eg: `BOOKED` to 'B'.
	 */
	@Override
	public String convertToDatabaseColumn(BookingStatus status) {

		if (status == null) {
			return null;
		}
		return status.getCode();
	}

	/**
	 * Converts the string 'code' to BookingStatus eg: 'B' to `BOOKED`
	 */
	@Override
	public BookingStatus convertToEntityAttribute(final String code) {

		if (code == null) {
			return null;
		}

		return Stream.of(BookingStatus.values())
				.filter(c -> c.getCode().equals(code)).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
