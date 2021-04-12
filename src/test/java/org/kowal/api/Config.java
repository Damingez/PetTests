package org.kowal.api;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Config {

    public final static String BASE_URI = "https://petstore.swagger.io/v2";

    public final static String FIND_PETS = "/pet/findByStatus";
    public final static String ACTIONS_ON_PET = "/pet";
    public static final String GET_PET_BY_ID = "/pet/{petId}";
    public static final String PET_UPLOAD_IMAGE = "/pet/{petId}/uploadImage";

    public static RequestSpecification getRequestSpecification(String contentType, String contentTypeValue) {
        return given().log().all()
                .header(contentType, contentTypeValue)
                .baseUri(Config.BASE_URI);
    }


}
