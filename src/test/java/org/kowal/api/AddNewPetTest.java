package org.kowal.api;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kowal.api.payloads.Payload;

import static io.restassured.RestAssured.given;

public class AddNewPetTest {

    @Before
    public void setBaseURI() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    public void addPetTest() {
        Integer petId = 12;
        given().log().all().header("Content-type", "application/json")
                .baseUri(Endpoints.baseURI)
                .body(Payload.provideNewPetRequest(petId))
                .when().post("/pet")
                .then().log().all().assertThat().statusCode(200);

        String response = given().log().all()
                .baseUri(Endpoints.baseURI)
                .header("Content-type", "application/json")
                .when().get("/pet/" + petId)
                .then().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(response);
        Integer idFromResponse = js.getInt("id");
        Assert.assertEquals(petId, idFromResponse);
    }

    @Test
    public void addPetWithIncorrectIdTest() {
        String petId = "cow";
        given().log().all().header("Content-type", "application/json")
                .baseUri(Endpoints.baseURI)
                .body(Payload.provideBadNewPetRequest(petId))
                .when().post("/pet")
                .then().log().all().assertThat().statusCode(400);

    }
}
