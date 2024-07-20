# Hotel-California
### 🚀 Spring Boot API Challenge

**Objective:**
Create a reservation management API for a hotel system.

#### 🛠️ Project Requirements:

1. **Entities and Relationships:**
   - **`Room`**: Room with attributes like `id`, `number`, `type`, `price`, `status`.
   - **`Booking`**: Reservation with attributes like `id`, `customerName`, `checkInDate`, `checkOutDate`, `status`, `room`.
   - **`Customer`**: Customer with attributes like `id`, `name`, `email`, `phone`.
   - **Relationships:**
     - A reservation is associated with a single room and a customer.
     - A customer can have multiple reservations.
     - A room can have multiple reservations over time but cannot be reserved for more than one reservation at the same time.

2. **Business Rules:**
   - Check room availability before creating a reservation.
   - Do not allow a reservation for a room that is already booked for the specified dates.
   - Update the room status to `BOOKED` when a reservation is created and to `AVAILABLE` when the reservation is canceled or completed.
   - The reservation status can be `PENDING`, `CONFIRMED`, `CHECKED_IN`, `CANCELLED`.

3. **Technologies and Tools:**
   - **Spring Boot** for creating the API.
   - **Spring Validation** for validating input data.
   - **Spring Data JPA** for database interaction.
   - **Relational Database** (e.g., PostgreSQL, MySQL).
   - **Flyway** for database migration.
   - **Integration Tests** with **Spring Test**.
   - **Error Handling** with `@ControllerAdvice`.

4. **API Endpoints:**
   - `GET /rooms`: List all rooms.
   - `GET /rooms/{id}`: Get details of a specific room.
   - `POST /rooms`: Add a new room.
   - `PUT /rooms/{id}`: Update an existing room.
   - `DELETE /rooms/{id}`: Delete a room.
   - `GET /bookings`: List all reservations.
   - `GET /bookings/{id}`: Get details of a specific reservation.
   - `POST /bookings`: Create a new reservation.
   - `PUT /bookings/{id}`: Update an existing reservation.
   - `DELETE /bookings/{id}`: Cancel a reservation.
   - `GET /customers`: List all customers.
   - `GET /customers/{id}`: Get details of a specific customer.
   - `POST /customers`: Add a new customer.
   - `PUT /customers/{id}`: Update an existing customer.
   - `DELETE /customers/{id}`: Delete a customer.

#### 🔄 Status: In Progress

#### 🎯 Final Considerations:
This challenge involves creating an API for managing hotel reservations, including rooms, reservations, and customers. The implementation should address validation, database migration, error handling, and integration tests. Good luck! 🚀
