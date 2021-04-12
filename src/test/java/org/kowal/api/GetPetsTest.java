package org.kowal.api;

import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;

public class GetPetsTest {

    @Test
    public void getPetsByStatusTest() {
        String parameterName = "status";
        String statusValue = "available";

        String response = Config.getRequestSpecification("Content-type", "application/json")
                .queryParam(parameterName, statusValue)
                .when().get(Config.FIND_PETS)
                .then().assertThat()
                .statusCode(200).extract().response().asString();

        JsonPath json = new JsonPath(response);

        int elementsNumber = json.get("pet.size()");
        Assert.assertTrue(elementsNumber > 0);
        System.out.println(elementsNumber);
    }

    @Test
    public void getPetsByNonExistingStatusTest() {
        String parameterName = "status";
        String value = "sleepy";

        String response = Config.getRequestSpecification("Content-type", "application/json")
                .queryParam(parameterName, value)
                .when().get(Config.FIND_PETS)
                .then().assertThat()
                .statusCode(200).extract().response().asString();

        JsonPath json = new JsonPath(response);
        int elements = json.get("array.size()");

        Assert.assertTrue(elements == 0);
        System.out.println(elements);
    }
}
