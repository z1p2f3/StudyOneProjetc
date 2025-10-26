import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestBody {
    @Test
    void testBody(){
        given()
                .log().all()
        .when()
                .get("https://httpbin.ceshiren.com/get")
        .then()
                .body(
                        "origin",equalTo("123.112.138.29, 123.112.138.29")
                )
                .log().all();
    }
}
