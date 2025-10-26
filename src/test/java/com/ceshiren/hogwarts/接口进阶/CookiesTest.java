package com.ceshiren.hogwarts.接口进阶;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.specification.ProxySpecification.host;

public class CookiesTest {

    @Test
    void addCookie(){

        // 设置全局配置
//        RestAssured.proxy = host("localhost").withPort(8888);
//        RestAssured.useRelaxedHTTPSValidation();
//        RestAssured.urlEncodingEnabled = false;

        // 方法2：直接使用JSON字符串
        String jsonBody = "{\"username\": \"admin\", \"password\": \"123456\"}";
        String a = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
        .when()
                .post("http://dataops-dev.senses-ai.com/upc/user/login")
        .then().extract().path("data.token");
        System.out.println(a);

        given()
                .header("Authorization","Bearer " + a)
                .when()
                .get("http://dataops-dev.senses-ai.com/upc/user/info")
                .then();

    }
}
