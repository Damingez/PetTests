package org.kowal.api;

import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kowal.api.payloads.Payload;

public class GetPetTest {

    private static int petId;

    @BeforeClass
    public static void providePetForTests() {
        petId = 12;
        Config.getRequestSpecification("Content-type", "application/json")
                .baseUri(Config.BASE_URI)
                .body(Payload.provideNewPetRequest(petId))
                .when().post(Config.ACTIONS_ON_PET);
    }

    @Test
    public void getPetDetailsTest() {
        String response = Config.getRequestSpecification("Content-type", "application/json")
                .pathParam("petId", petId)
                .when().get(Config.GET_PET_BY_ID)
                .then().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(response);
        int idFromResponse = js.get("id");
        Assert.assertEquals(petId, idFromResponse);
    }


    @Test
    public void getPetDetailsNegativeTest() {
       int incorrectPetId = 0;
        Config.getRequestSpecification("Content-type", "application/json")
                .pathParam("petId", incorrectPetId)
                .when().get(Config.GET_PET_BY_ID)
                .then().assertThat().statusCode(404);

    }



}
