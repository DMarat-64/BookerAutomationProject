package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.BookingById;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingByIdTest {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
   // private

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetBookingById() throws JsonProcessingException {
        Response response = apiClient.getBookingById();

        assertThat(response.getStatusCode()).isEqualTo(200);

      /* String responseBody = response.getBody().asString();
        List<BookingById> bookingByIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingById>>() {});

        assertThat(bookingByIds).isNotEmpty();

        for (BookingById bookingById : bookingByIds) {
            assertThat(bookingById).isNotNull();
           assertThat(bookingById.getTotalprice()).isGreaterThan(0);
        }*/
    }
}
