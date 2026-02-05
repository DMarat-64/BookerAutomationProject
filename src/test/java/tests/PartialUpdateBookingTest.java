package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.BookingDates;
import core.models.CreateBooking;
import core.models.NewCreatedBooking;
import core.models.PartialUpdateBooking;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartialUpdateBookingTest {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateBooking createdBooking;
    private NewCreatedBooking newCreatedBooking;
    private PartialUpdateBooking partialUpdateBooking;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();

        createdBooking = new CreateBooking();
        createdBooking.setFirstname("Bob");
        createdBooking.setLastname("Falker");
        createdBooking.setTotalprice(135);
        createdBooking.setDepositpaid(true);
        createdBooking.setBookingdates(new BookingDates("2025-04-08", "2025-04-10"));
        createdBooking.setAdditionalneeds("Breakfast");
    }

    @Test
    @Feature("Booking")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("marat")
    public void testPatchBooking() throws JsonProcessingException {

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

        Integer bookingId = newCreatedBooking.getBookingid();
        assertThat(bookingId).isNotNull().isGreaterThan(0);

        apiClient.createToken("admin", "password123");

        PartialUpdateBooking patchData = new PartialUpdateBooking();
        patchData.setFirstname("Jonathan");

        String patchBody = objectMapper.writeValueAsString(patchData);
        Response patchBookingId = apiClient.patchBookingId(patchBody, bookingId);

        String responseBodyFromPatch = patchBookingId.getBody().asString();
        CreateBooking patchBooking = objectMapper.readValue(responseBodyFromPatch, CreateBooking.class);

        assertThat(patchBookingId.getStatusCode()).isEqualTo(200);
        assertThat(patchBooking).isNotNull();
        assertEquals(newCreatedBooking.getBooking().getFirstname(), createdBooking.getFirstname());
    }

    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(newCreatedBooking.getBookingid());

        assertThat(apiClient.getDeleteBookingId(newCreatedBooking.getBookingid()).getStatusCode()).isEqualTo(404);
    }
}
