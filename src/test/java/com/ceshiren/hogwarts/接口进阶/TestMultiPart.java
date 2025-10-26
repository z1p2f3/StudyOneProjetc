package com.ceshiren.hogwarts.接口进阶;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.specification.ProxySpecification.host;

public class TestMultiPart {

    @Test
    void case1(){
        File file = new File("src/test/resources/MultiPart.txt");

        RestAssured.proxy = host("localhost").withPort(8888);
        RestAssured.useRelaxedHTTPSValidation();

        given()
                .log().headers()
                .log().body()
                .multiPart("zhangma",file)
                .multiPart("ceshiren","{\"fei\":6666}","application/json")
        .when()
                .post("https://httpbin.ceshiren.com/post")
        .then()
                .statusCode(200);
    }
}
