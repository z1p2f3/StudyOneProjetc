package com.ceshiren.hogwarts.接口进阶;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TestTimeOut {

    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://httpbin.ceshiren.com";
    }

    @Test
    void case1(){
        given()
                .when().get("/get")
                .then().statusCode(200);
    }

    @Test
    void case2(){
        HttpClientConfig httpClientConfig = HttpClientConfig.httpClientConfig().setParam("http.socket.timeout",3000);
        RestAssuredConfig restAssuredConfig = RestAssuredConfig.config().httpClient(httpClientConfig);

        given()
                .config(restAssuredConfig)
        .when().get("/dalay/1")
                .then().statusCode(200);
    }

    @Test
    void case3(){
        given()
                .when().get("/get")
                .then().statusCode(200);
    }
}
