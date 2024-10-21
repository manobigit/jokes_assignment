package org.acme.controllertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@QuarkusTest
public class JokeControllerTest {


    @Test
    void testGetJokesAsValidCountQueryParam() throws JsonProcessingException {
        int count = 10;
        var result = given()
                .header("Content-Type", "application/json")
                // when
                .when()
                .queryParam("count", count)

                .get("/jokes")
                // then
                .then()
                .statusCode(200).extract().response().asString();
        var jsonMapper = new JsonMapper();
        var json = jsonMapper.readTree(result);
        assertEquals(json.size(), count);

    }

    @Test
    void testWhenCountQueryParamIsNotSameAsSize() throws JsonProcessingException {
        int count = 10;
        var result = given()
                .header("Content-Type", "application/json")
                // when
                .when()
                .queryParam("count", count)

                .get("/jokes")
                // then
                .then()
                .statusCode(200).
                extract().response().asString();
        var jsonMapper = new JsonMapper();
        var json = jsonMapper.readTree(result);
        assertNotEquals(json.size(), 20);

    }

    @Test
    void testWhenCountQueryParamIsLessThanZero() throws JsonProcessingException {
        int count = -1;
        var result = given()
                .header("Content-Type", "application/json")
                // when
                .when()
                .queryParam("count", count)

                .get("/jokes")
                // then
                .then()
                .statusCode(400) .body("message", is("Count must be between 1 and 100."))  // Assert the error message in the response
                .body("statusCode", is(400));
}

    @Test
    void testWhenCountQueryParamIsGreaterThanHundred() throws JsonProcessingException {
        int count = 102;
        var result = given()
                .header("Content-Type", "application/json")
                // when
                .when()
                .queryParam("count", count)

                .get("/jokes")
                // then
                .then()
                .statusCode(400) .body("message", is("Count must be between 1 and 100."))  // Assert the error message in the response
                .body("statusCode", is(400));
    }

}
