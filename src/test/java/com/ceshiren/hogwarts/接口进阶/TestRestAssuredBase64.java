package com.ceshiren.hogwarts.接口进阶;

import io.restassured.RestAssured;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static io.restassured.specification.ProxySpecification.host;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestRestAssuredBase64 {
    @Test
    void testBase64() {
//        RestAssured.proxy = host("localhost").withPort(8888);
//        RestAssured.useRelaxedHTTPSValidation();
        byte[] bytes = "qiongke".getBytes(StandardCharsets.UTF_8);
        String encode = Base64.encodeBase64String(bytes);
        String res =
        given()
                .param("ma",encode)
        .when()
                .post("https://httpbin.ceshiren.com/post")
        .then()
                .extract().path("form.ma");
//        System.out.println(res);

        byte[] decoded = Base64.decodeBase64(res);
        String s = new String(decoded,StandardCharsets.UTF_8);

        assertEquals("qiongke",s);

    }
}
