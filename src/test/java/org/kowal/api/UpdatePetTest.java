package org.kowal.api;

import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kowal.api.payloads.Payload;

import java.io.File;

public class UpdatePetTest {

    private static int petId = 16;

    @BeforeClass
    public static void providePetForTests() {
         petId = 16;
        Config.getRequestSpecification("Content-type", "application/json")
                .body(Payload.provideNewPetRequest(petId))
                .when().post(Config.ACTIONS_ON_PET);
    }

    @Test
    public void updateExistingPetTest() {

        String newPetName = "Pet wit modified name";
        Config.getRequestSpecification("Content-type", "application/json")
                .body(Payload.providePetModificationRequest(petId, newPetName))
                .when().put(Config.ACTIONS_ON_PET)
                .then().log().all().assertThat().statusCode(200);

        String response = Config.getRequestSpecification("Content-type", "application/json")
                .pathParam("petId", petId)
                .when().get(Config.GET_PET_BY_ID)
                .then().assertThat()
                .statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(response);
        int idFromResponse = js.get("id");
        String nameFromResponse = js.getString("name");

        Assert.assertEquals(petId, idFromResponse);
        Assert.assertEquals(newPetName, nameFromResponse);
    }



    @Test
    public void updateNonExistingPetTest() {
        String incorrectPetId = "xdxyiu";
        String newPetName = "Pet wit modified name";
        Config.getRequestSpecification("Content-type", "application/json")
                .body(Payload.providePetModificationIncorrectRequest(incorrectPetId, newPetName))
                .when().put(Config.ACTIONS_ON_PET)
                .then().log().all().assertThat().statusCode(400);
    }

    @Test
    public void updatePetWithFormDataTest() {

        String newName = "Ingri123";
        String newStatus = "sold";

        Config.getRequestSpecification("Content-Type", "application/x-www-form-urlencoded")
                .formParam("name",newName )
                .formParam("status", newStatus )
                .pathParam("petId",petId)
                .when().post(Config.GET_PET_BY_ID)
                .then().log().all().assertThat().statusCode(200);

        String response = Config.getRequestSpecification("Content-type", "application/json")
                .pathParam("petId", petId)
                .when().get(Config.GET_PET_BY_ID)
                .then().assertThat()
                .statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(response);

        String nameFromResponse = js.getString("name");
        String statusFromResponse = js.getString("status");

        Assert.assertEquals(newStatus, statusFromResponse);
        Assert.assertEquals(newName, nameFromResponse);


    }

    @Test
    public void uploadImageForPetTest() {
        Config.getRequestSpecification("Content-type", "multipart/form-data")
                .pathParam("petId", petId)
                .multiPart("file", new File("utilities/low-res-horse.jpg"))
                .when().post(Config.PET_UPLOAD_IMAGE)
                .then().log().all().assertThat().statusCode(200);
    }

}
