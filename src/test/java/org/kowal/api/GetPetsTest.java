package org.kowal.api;

import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class GetPetsTest {

    @Test
    public void getPetsByStatusTest() {
        String parameterName = "status";
        String statusValue = "available";

        String response = given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URI)
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

        String response = given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URI)
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
