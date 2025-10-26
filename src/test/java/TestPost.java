import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class TestPost {
    @Test
    void testPost(){
        given()
                .log().all()
        .when()
                .post("https://httpbin.ceshiren.com/post")
        .then()
                .log().all()
                .statusCode(200);
    }
}
