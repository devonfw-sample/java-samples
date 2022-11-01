CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 100000;


CREATE TABLE Booking (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modification_counter INTEGER NOT NULL,
  id_user BIGINT,
  name VARCHAR (255) NOT NULL,
  booking_token VARCHAR (255),
  comment VARCHAR (4000),
  email VARCHAR(255) NOT NULL,
  booking_date TIMESTAMP NOT NULL,
  expiration_date TIMESTAMP,
  booking_status VARCHAR(1),
  CONSTRAINT PK_Booking PRIMARY KEY(id)
);

CREATE TABLE Invited_Guest (
  id BIGINT NOT NULL AUTO_INCREMENT,
  modification_counter INTEGER NOT NULL,
  id_booking BIGINT ,
  guest_token VARCHAR (255),
  email VARCHAR (60),
  accepted BOOLEAN,
  CONSTRAINT PK_InvitedGuest PRIMARY KEY(id),
  CONSTRAINT FK_InvitedGuest_idBooking FOREIGN KEY(id_booking) REFERENCES Booking(id) NOCHECK
);
