package org.kowal.api;


import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;
import org.kowal.api.payloads.Payload;

import static io.restassured.RestAssured.given;

public class AddNewPetTest {

    @Test
    public void addPetTest() {

        Integer petId = 12;
        given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URI)
                .body(Payload.provideNewPetRequest(petId))
                .when().post(Config.ACTIONS_ON_PET)
                .then().log().all().assertThat().statusCode(200);

        String response = given().log().all()
                .baseUri(Config.BASE_URI)
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
                .baseUri(Config.BASE_URI)
                .body(Payload.provideBadNewPetRequest(petId))
                .when().post(Config.ACTIONS_ON_PET)
                .then().log().all().assertThat().statusCode(400);

    }
}
