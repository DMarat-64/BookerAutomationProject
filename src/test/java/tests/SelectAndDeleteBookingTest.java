package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import core.models.CreatedBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SelectAndDeleteBookingTest {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreatedBooking createdBooking;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        apiClient.createToken("admin", "password123");
    }

    @Test
    public void testSelectAndDeleteBookingTest() throws JsonProcessingException {
        Response response = apiClient.getBooking();
        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        List<Booking> bookings = objectMapper.readValue(responseBody, new TypeReference<List<Booking>>() {});
        assertThat(bookings).isNotEmpty();

        for (Booking booking : bookings) {
            assertThat(booking.getBookingid()).isGreaterThan(0);
        }

        Booking firstBookingId = bookings.getFirst();
        int bookingId = firstBookingId.getBookingid();
        assertThat(bookingId).isGreaterThan(0);

        apiClient.createToken("admin", "password123");
        Response deleteBookingId = apiClient.deleteBooking(bookingId);
        assertThat(deleteBookingId.getStatusCode()).isEqualTo(201);

        Response getDeleteBookingId = apiClient.getDeleteBookingId(bookingId);
        assertThat(getDeleteBookingId.getStatusCode()).isEqualTo(404);
    }
}
