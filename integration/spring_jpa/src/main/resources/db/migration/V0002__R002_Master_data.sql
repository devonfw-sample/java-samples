INSERT INTO booking(
  id, modification_counter, id_user,
  NAME, booking_token, comment, email,
  booking_date, expiration_date,
  booking_status
)
VALUES
  (
    0, 0, 0,
    'user0', 'CB_20170509_123502555Z', 'Booking Type CSR', 'user0@mail.com',
    Dateadd('DAY', 5, CURRENT_TIMESTAMP),
    Dateadd(
      'DAY',
      5,
      Dateadd('HOUR', -1, CURRENT_TIMESTAMP)
    ),
    'C'
  );
INSERT INTO Invited_Guest(
  id, modification_counter, id_booking,
  guest_token, email, accepted
)
VALUES
  (
    0,
    0,
    0,
    'GB_20170510_02350266501Z',
    'guest0@mail.com',
    true
  );