package core.clients;

import core.settings.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class APIClient {

    private final String baseUrl;
    private String token;

    public APIClient() {
        this.baseUrl = determineBaseUrl();
    }

    private String determineBaseUrl() {
        String environment = System.getProperty("env", "test");
        String configFileName = "application-" + environment + ".properties";

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalStateException("Configuration file not found:" + configFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load configuration file: " + configFileName, e);
        }

        return properties.getProperty("baseUrl");
    }

    private RequestSpecification getRequestSpec() {
        return given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .filter(addAuthTokenFilter());
    }

    public void createToken(String username, String password) {
        String requestBody = String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", username, password);

        Response response = getRequestSpec()
                .body(requestBody)
                .when()
                .post(ApiEndpoints.AUTH.getPath())
                .then()
                .statusCode(200)
                .extract()
                .response();

        token = response.jsonPath().getString("token");
    }

    private Filter addAuthTokenFilter() {
        return (FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) -> {
            if (token != null) {
                requestSpec.header("Cookie", "token=" + token);
            }
            return ctx.next(requestSpec, responseSpec);
        };
    }


    public Response ping() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.PING.getPath())
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    public Response getBooking() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOKING.getPath())
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();
    }

    public Response getBookingById(int byId) {
        return getRequestSpec()
                .pathParam("id", byId)
                .log().all()
                .when()
                .get(ApiEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();
    }

    public Response getDeleteBookingId(int deleteId) {
        return getRequestSpec()
                .pathParam("id", deleteId)
                .when()
                .get(ApiEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .statusCode(404)
                .log().all()
                .extract()
                .response();
    }

    public Response deleteBooking(int bookingId) {
        return getRequestSpec()
                .pathParam("id", bookingId)
                .log().all()
                .when()
                .delete(ApiEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .response();
    }

    public Response createBooking(String newBooking) {
        return getRequestSpec()
                .body(newBooking)
                .log().all()
                .when()
                .post(ApiEndpoints.BOOKING.getPath())
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();
    }

    public Response putBookingId(String putBooking, int Id) {
        return getRequestSpec()
                .pathParam("id", Id)
                .body(putBooking)
                .log().all()
                .when()
                .put(ApiEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();
    }

    public Response patchBookingId(String putBooking, int Id) {
        return getRequestSpec()
                .pathParam("id", Id)
                .body(putBooking)
                .log().all()
                .when()
                .patch(ApiEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();
    }

    public Response getFilterBooking(String firstName, String lastName) {
        return getRequestSpec()
                .when()
                .queryParam( "firstname", firstName)
                .queryParam( "lastname", lastName)
                .log().all()
                .get(ApiEndpoints.BOOKING.getPath())
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();
    }
}

