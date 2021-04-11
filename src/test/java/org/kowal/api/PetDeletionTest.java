package org.kowal.api;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.kowal.api.payloads.Payload;

import static io.restassured.RestAssured.given;

public class PetDeletionTest {

    @Before
    public void setBaseURI() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    public void deletePetTest() {

        int petId = 18;
        given().log().all().header("Content-type", "application/json")
                .body(Payload.provideNewPetRequest(petId))
                .when().post("/pet");

        given().log().all()
                .header("Content-type", "application/json")
                .when().delete("/pet/" + petId)
                .then().assertThat().statusCode(200);

        given().log().all()
                .header("Content-type", "application/json")
                .when().get("/pet/" + petId)
                .then().assertThat().statusCode(404);

    }

    @Test
    public void deletePetWithIncorrectIdTest() {
        int petId = 0;
        given().log().all()
                .header("Content-type", "application/json")
                .header("api_key", "true")
                .when().delete("/pet/" + petId)
                .then().assertThat().statusCode(404);

    }


}
