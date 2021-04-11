package org.kowal.api;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kowal.api.payloads.Payload;

import java.io.File;

import static io.restassured.RestAssured.given;

public class UpdatePetTest {

    @Before
    public void setBaseURI() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    public void updateExistingPetTest() {
        Integer petId = 15;
        String newPetName = "Pet wit modified name";
        given().log().all().header("Content-type", "application/json")
                .body(Payload.providePetModificationRequest(petId, newPetName))
                .when().put("/pet")
                .then().log().all().assertThat().statusCode(200);

        String response = given().log().all()
                .header("Content-type", "application/json")
                .when().get("/pet/" + petId)
                .then().assertThat()
                .statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(response);
        Integer idFromResponse = js.getInt("id");
        String nameFromResponse = js.getString("name");

        Assert.assertEquals(petId, idFromResponse);
        Assert.assertEquals(newPetName, nameFromResponse);
    }

    @Test
    public void updateNonExistingPetTest() {
        String petId = "xdxyiu";
        String newPetName = "Pet wit modified name";
        given().log().all().header("Content-type", "application/json")
                .body(Payload.providePetModificationIncorrectRequest(petId, newPetName))
                .when().put("/pet")
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    public void updatePetWithFormDataTest() {
        int petId = 5;
        given().log().all()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("name", "Ingri123")
                .formParam("status", "esteri123")
                .when().post("/pet/" + petId)
                .then().log().all().assertThat().statusCode(200);
    }

    @Test
    public void uploadImageForPetTest() {
        int petId = 15;
        given().log().all()
                .header("Content-type", "multipart/form-data")
                .multiPart("file", new File("utilities/low-res-horse.jpg"))
                .when().post("/pet/" + petId + "/uploadImage")
                .then().log().all().assertThat().statusCode(200);
    }

}
