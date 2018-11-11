package util;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

/**
 * Created by guall on 08/11/2018.
 */
public class RestUtil {
    private static final String BASE_URL = System.getProperty("context");
    public static final int TEMPORARILY_REDIRECT = 302;
    public static final String SERVICE = "services";
    public static final String GOALS_SERVICE = SERVICE + "/goals";
    public static final String ADDGOAL_SERVICE = SERVICE + "/goal";
    public static final String HELLO_SERVICE = "hello";

    public Response post(String service, String body) {
        return getRequestSpec(getBaseUrl(service), body).post();
    }

    public Response get(String service) {
        return getRequestSpec(getBaseUrl(service)).get();
    }

    public Response getRedirected(String location) {
        return getRequestSpec(location).get();
    }

    private String getBaseUrl(String service){
        return String.format("%s/%s", BASE_URL, service);
    }

    private RequestSpecification getRequestSpec(String baseUrl) {
        RestAssured.baseURI = baseUrl;
        return RestAssured
                .given()
                .header("Content-Type","application/json");
    }

    private RequestSpecification getRequestSpec(String baseUrl, String body) {
        return getRequestSpec(baseUrl)
               .body(body);
    }
}
