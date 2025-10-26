import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestJsonHashMap {
    @Test
    void testJsonHashMap(){
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("Hello","zhangpengfei");
        given()
                .contentType("application/json")
                .body(objectObjectHashMap)
        .when()
                .post("https://httpbin.ceshiren.com/post")
        .then()
                .log().all()
                .body("headers['Content-Length']",equalTo("24"))
                .statusCode(200);
    }
}
