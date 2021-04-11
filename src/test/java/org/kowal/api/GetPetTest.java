package org.kowal.api;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class GetPetTest {

    @Before
    public void setBaseURI() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    public void getPetDetailsTest() {
        int petId = 12;
        String response = given().log().all()
                .header("Content-type", "application/json")
                .when().get("/pet/" + petId)
                .then().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(response);
        int idFromResponse = js.get("id");
        Assert.assertEquals(petId, idFromResponse);
    }


    @Test
    public void getPetDetailsNegativeTest() {
        int petId = 0;
        given().log().all()
                .header("Content-type", "application/json")
                .when().get("/pet/" + petId)
                .then().assertThat().statusCode(404);

    }

    @Test
    public void getPetsByStatusTest() {
        String parameterName = "status";
        String value = "available";

        String response = given().log().all()
                .header("Content-type", "application/json")
                .queryParam(parameterName, value)
                .when().get("/pet/findByStatus")
                .then().assertThat()
                .statusCode(200).extract().response().asString();

        JsonPath json = new JsonPath(response);
        int elements = json.get("array.size()");

        Assert.assertTrue(elements > 0);
        System.out.println(elements);
    }

}
