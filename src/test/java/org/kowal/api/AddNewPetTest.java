package org.kowal.api;


import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;
import org.kowal.api.payloads.Payload;

public class AddNewPetTest {

    @Test
    public void addPetTest() {

        int petId = 12;
        Config.getRequestSpecification("Content-type", "application/json")
                .body(Payload.provideNewPetRequest(petId))
                .when().post(Config.ACTIONS_ON_PET)
                .then().log().all().assertThat().statusCode(200);

        String response = Config.getRequestSpecification("Content-type", "application/json")
                .when().get("/pet/" + petId)
                .then().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(response);
        int idFromResponse = js.get("id");
        Assert.assertEquals(petId, idFromResponse);
    }

    @Test
    public void addPetWithIncorrectIdTest() {
        String petId = "cow";
        Config.getRequestSpecification("Content-type", "application/json")
                .body(Payload.provideBadNewPetRequest(petId))
                .when().post(Config.ACTIONS_ON_PET)
                .then().log().all().assertThat().statusCode(400);

    }
}
