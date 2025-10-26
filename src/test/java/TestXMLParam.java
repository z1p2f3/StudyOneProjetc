import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestXMLParam {
    @Test
    void testXMLParam() throws IOException {
        File file = new File("src/test/resources/add.xml");
        FileInputStream fileInputStream = new FileInputStream(file);
        String reBody = IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);

        given()
                .contentType("text/xml")
                .body(reBody)
                .log().headers()
                .log().body()
        .when()
                .post("http://dneonline.com//calculator.asmx")
        .then()
                .statusCode(200)
                .body("//AddResult.text()",equalTo("5"))
                .log().all();
    }
}
