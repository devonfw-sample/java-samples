package com.devonfw.examples.dataaccess.springjpa.domain.model;

/**
 * BookingStatus enumerator helps to handle Status in a better way. Here the
 * code associated with each enum is persisted to the database instead of the
 * whole string, by the BookingStatusConverter.
 */
public enum BookingStatus {
	BOOKED("B"), CANCELED("C"), NOTBOOKED("N");

	private String code;

	BookingStatus(String code) {

		this.code = code;
	}

	public String getCode() {

		return this.code;
	}
}
