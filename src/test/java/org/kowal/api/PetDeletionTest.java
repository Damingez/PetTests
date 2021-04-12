package org.kowal.api;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kowal.api.payloads.Payload;

public class PetDeletionTest {

   private static int petId;

    @BeforeClass
    public static void providePetForTests() {
        petId = 18;
        Config.getRequestSpecification("Content-type", "application/json")
                .body(Payload.provideNewPetRequest(petId))
                .when().post(Config.ACTIONS_ON_PET);
    }

    @Test
    public void deletePetTest() {

        Config.getRequestSpecification("Content-type", "application/json")
                .pathParam("petId", petId)
                .when().delete(Config.GET_PET_BY_ID)
                .then().assertThat().statusCode(200);

        Config.getRequestSpecification("Content-type", "application/json")
                .pathParam("petId", petId)
                .when().get(Config.GET_PET_BY_ID)
                .then().assertThat().statusCode(404);

    }

    @Test
    public void deletePetWithIncorrectIdTest() {
         petId = 0;
        Config.getRequestSpecification("Content-type", "application/json")
                .pathParam("petId", petId)
                .when().delete(Config.GET_PET_BY_ID)
                .then().assertThat().statusCode(404);

    }


}
