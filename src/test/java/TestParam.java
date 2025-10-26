import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TestParam {
    @Test
    void testParam1(){
        given()
                .log().all()
                .when()
                .post("https://httpbin.ceshiren.com/post?username=zhangpengfei")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    void testParam2(){
        given()
                .log().all()
                .when()
                .get("https://httpbin.ceshiren.com/get?username=zhangpengfei")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    void testParamGet(){
        given()
                .log().all().param("username","zhangpengfei")
                .when()
                .get("https://httpbin.ceshiren.com/get")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    void testParamPost(){
        given()
                .queryParam("username","zhangpengfei")
                .log().all()
        .when()
                .post("https://httpbin.ceshiren.com/post")
        .then()
                .log().all()
                .statusCode(200);
    }
}
