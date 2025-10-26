package com.ceshiren.hogwarts.接口进阶;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class TestEnvironment {

    @BeforeAll
    static void setUp() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("org","http://httpbin.org");
        hm.put("ceshiren","http://httpbin.ceshiren.com");
        hm.put("default","ceshiren");

        RestAssured.baseURI = hm.get(hm.get("default"));
    }
    @Test
    void testEnvironment(){
        given()
                .when().get("/get")
                .then().log().all().statusCode(200);
    }
}
