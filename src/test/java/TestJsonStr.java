import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestJsonStr {
    @Test
    void testJsonStr(){
        String jsonStr = "{\"name\":\"zhangpengfei\"}";
        given()
                .contentType("application/json")
                .body(jsonStr)
                .log().headers()
                .log().body()
        .when()
                .post("https://httpbin.ceshiren.com/post")
        .then()
//                .log().all()
                .statusCode(200);
    }
}
