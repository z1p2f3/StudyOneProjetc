import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TestRestAssuredDemo1 {
    @Test
    void run(){
        given().header("hello","cainiao")
                .when().get("http://httpbin.ceshiren.com/get")
                .then()
                .log().all();
    }
}
