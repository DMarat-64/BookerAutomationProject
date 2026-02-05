package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetBookingsFilteredByParametersTest {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreateBooking createdBooking;
    private NewCreatedBooking newCreatedBooking;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @ParameterizedTest
    @CsvSource({
            "Maik, Bin, 200, true, 2025-02-12, 2025-02-20, Breakfast",
            "Boris, Britva, 355, true, 2025-04-22, 2025-04-28, Dinner",
            "Sony, Liston, 741, false, 2026-03-02, 2026-03-04, Breakfast"
    })
    @Feature("Booking")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("marat")
    public void createBooking(String firstname, String lastname, int totalPrice,
                              boolean depositPaid, String checkin, String checkout, String additionalneeds) throws JsonProcessingException {
        createdBooking = new CreateBooking();
        createdBooking.setFirstname(firstname);
        createdBooking.setLastname(lastname);
        createdBooking.setTotalprice(totalPrice);
        createdBooking.setDepositpaid(depositPaid);
        createdBooking.setBookingdates(new BookingDates(checkin, checkout));
        createdBooking.setAdditionalneeds(additionalneeds);

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

        Response getResponse = apiClient.getFilterBooking(firstname, lastname);
        assertThat(getResponse.getStatusCode()).isEqualTo(200);
        assertEquals(newCreatedBooking.getBooking().getFirstname(), createdBooking.firstname);
        assertEquals(newCreatedBooking.getBooking().getLastname(), createdBooking.lastname);
    }

    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(newCreatedBooking.getBookingid());

        assertThat(apiClient.getDeleteBookingId(newCreatedBooking.getBookingid()).getStatusCode()).isEqualTo(404);
    }
}
