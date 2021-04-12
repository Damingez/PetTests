package org.kowal.api;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kowal.api.payloads.Payload;
import static io.restassured.RestAssured.given;

public class PetDeletionTest {

   private static int petId;

    @BeforeClass
    public static void providePetForTests() {
        petId = 18;
        given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URI)
                .body(Payload.provideNewPetRequest(petId))
                .when().post(Config.ACTIONS_ON_PET);
    }

    @Test
    public void deletePetTest() {

        given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URI)
                .pathParam("petId", petId)
                .when().delete(Config.GET_PET_BY_ID)
                .then().assertThat().statusCode(200);

        given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URI)
                .pathParam("petId", petId)
                .when().get(Config.GET_PET_BY_ID)
                .then().assertThat().statusCode(404);

    }

    @Test
    public void deletePetWithIncorrectIdTest() {
         petId = 0;
        given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URI)
                .pathParam("petId", petId)
                .when().delete(Config.GET_PET_BY_ID)
                .then().assertThat().statusCode(404);

    }


}
