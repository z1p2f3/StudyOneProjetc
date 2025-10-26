import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.specification.ProxySpecification.host;

public class TestCookie {
    @Test
    void testCookie(){
        RestAssured.proxy =host("127.0.0.1").withPort(8888);
        RestAssured.useRelaxedHTTPSValidation();
        given()
                .cookies("Hello","zhangpengfei","Hi","maqiongke")
                .log().all()
        .when()
                .get("http://httpbin.ceshiren.com/get")
        .then()
                .statusCode(200)
                .log().all();
    }
}
