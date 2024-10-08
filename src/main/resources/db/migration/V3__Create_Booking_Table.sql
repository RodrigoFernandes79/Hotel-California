CREATE TABLE bookings (
 id BIGSERIAL PRIMARY KEY,
 customer_id BIGSERIAL NOT NULL,
 check_in_date DATE NOT NULL,
 check_out_date DATE,
 status VARCHAR(10) NOT NULL,
 room_id BIGSERIAL NOT NULL,

 CONSTRAINT fk_bookings_customer FOREIGN KEY(customer_id) REFERENCES customers(id),

 CONSTRAINT fk_bookings_room FOREIGN KEY(room_id) REFERENCES rooms(id)
);