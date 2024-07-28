# Hotel-California
### 🚀 Spring Boot API Challenge

**Objective:**
Create a reservation management API for a hotel system.

#### 🛠️ Project Requirements:

1. **Entities and Relationships:**
   - **`Room`**: Room with attributes like `id`, `number`, `type`, `price`, `status`. ✅**DONE**
   - **`Booking`**: Reservation with attributes like `id`, `customerName`, `checkInDate`, `checkOutDate`, `status`, `room`. ✅**DONE**
   - **`Customer`**: Customer with attributes like `id`, `name`, `email`, `phone`. ✅**DONE**
   - **Relationships:**
     - A reservation is associated with a single room and a customer. ✅**DONE**
     - A customer can have multiple reservations. ✅**DONE**
     - A room can have multiple reservations over time but cannot be reserved for more than one reservation at the same time. ✅**DONE**

2. **Business Rules:**
   - Check room availability before creating a reservation. ✅**DONE**
   - Do not allow a reservation for a room that is already booked for the specified dates. ✅**DONE**
   - Update the room status to `BOOKED` when a reservation is created and to `AVAILABLE` when the reservation is canceled or completed.
   - The reservation status can be `PENDING`, `CONFIRMED`, `CHECKED_IN`, `CANCELLED`. ✅**DONE**

3. **Technologies and Tools:**
   - **Spring Boot** for creating the API. ✅**DONE**
   - **Spring Validation** for validating input data. 🔄**In Progress**
   - **Spring Data JPA** for database interaction. ✅**DONE**
   - **Relational Database** (e.g., PostgreSQL, MySQL). ✅**DONE**
   - **Flyway** for database migration. ✅**DONE**
   - **Integration Tests** with **Spring Test**.
   - **Error Handling** with `@ControllerAdvice`. 🔄**In Progress**

4. **API Endpoints:**
   - `GET /rooms`: List all rooms. ✅**DONE**
   - `GET /rooms/{id}`: Get details of a specific room. ✅**DONE**
   - `POST /rooms`: Add a new room. ✅**DONE**
   - `PUT /rooms/{id}`: Update an existing room. ✅**DONE**
   - `DELETE /rooms/{id}`: Delete a room. ✅**DONE**
   - `GET /bookings`: List all reservations. ✅**DONE**
   - `GET /bookings/customer_name`: Get details of List reservation by customer name.✅**DONE**
   - `POST /bookings`: Create a new reservation. ✅**DONE**
   - `PUT /bookings/{id}`: Update an existing reservation. ✅**DONE**
   - `PUT /bookings/check_out/{id}`: Check out a reservation.
   - `DELETE /bookings/{id}`: Cancel a reservation.
   - `GET /customers`: List all customers. ✅**DONE**
   - `GET /customers/{id}`: Get details of a specific customer. ✅**DONE**
   - `POST /customers`: Add a new customer. ✅**DONE**
   - `PUT /customers/{id}`: Update an existing customer. ✅**DONE**
   - `DELETE /customers/{id}`: Delete a customer.✅**DONE**
   - `GET /customers/actives`: List all actives customers.✅**DONE**

#### 🔄 Status: In Progress

#### 🎯 Final Considerations:
This challenge involves creating an API for managing hotel reservations, including rooms, reservations, and customers. The implementation should address validation, database migration, error handling, and integration tests. Good luck! 🚀
