package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingDates;
import core.models.CreateBooking;
import core.models.NewCreatedBooking;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetListAllBookingTest {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateBooking createdBooking;
    private NewCreatedBooking newCreatedBooking;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();

        createdBooking = new CreateBooking();
        createdBooking.setFirstname("Jon");
        createdBooking.setLastname("Smit");
        createdBooking.setTotalprice(115);
        createdBooking.setDepositpaid(true);
        createdBooking.setBookingdates(new BookingDates("2025-07-10", "2025-07-20"));
        createdBooking.setAdditionalneeds("Breakfast");
    }

    @Test
    @Feature("Booking")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("marat")
    public void testGetListAllBooking() throws JsonProcessingException {

        String requestBody = objectMapper.writeValueAsString(createdBooking);
        Response response = apiClient.createBooking(requestBody);

        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.asString();
        newCreatedBooking = objectMapper.readValue(responseBody, NewCreatedBooking.class);

        assertThat(newCreatedBooking).isNotNull();
        assertEquals(newCreatedBooking.getBooking().getFirstname(), createdBooking.getFirstname());
        assertEquals(newCreatedBooking.getBooking().getLastname(), createdBooking.getLastname());
        assertEquals(newCreatedBooking.getBooking().getTotalprice(), createdBooking.getTotalprice());
        assertEquals(newCreatedBooking.getBooking().getBookingdates().getCheckin(), createdBooking.getBookingdates().getCheckin());
        assertEquals(newCreatedBooking.getBooking().getAdditionalneeds(), createdBooking.getAdditionalneeds());

        Response getResponse = apiClient.getBooking();
        assertThat(getResponse.getStatusCode()).isEqualTo(200);

        String getResponseBody = getResponse.getBody().asString();
        List<Booking> bookings = objectMapper.readValue(getResponseBody, new TypeReference<List<Booking>>() {});
        assertThat(bookings).isNotEmpty();

        for (Booking booking : bookings) {
            assertThat(booking.getBookingid()).isGreaterThan(0);
        }
    }

    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(newCreatedBooking.getBookingid());

        assertThat(apiClient.getDeleteBookingId(newCreatedBooking.getBookingid()).getStatusCode()).isEqualTo(404);
    }
}
