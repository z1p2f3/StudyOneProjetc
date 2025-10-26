import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TestGet {
    @Test
    void testGet(){
        given()
                .log().all()
        .when()
                .get("http://httpbin.ceshiren.com/get")
        .then()
                .statusCode(200)
                .log().all();
    }
}
