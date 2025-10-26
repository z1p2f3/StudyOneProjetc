import io.restassured.RestAssured;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.specification.ProxySpecification.host;

public class TestForm {
    @Test
    void testForm(){
        RestAssured.proxy =host("127.0.0.1").withPort(8888);
        RestAssured.useRelaxedHTTPSValidation();
        given()
                .formParams("Hello","zhangpengfei","Hi","maqiongke")
                .log().all()
        .when()
                .post("https://httpbin.ceshiren.com/post")
        .then()
                .statusCode(200);
    }
}
