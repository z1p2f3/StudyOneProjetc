package com.ceshiren.hogwarts.接口进阶;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MulitiResTest {

    String resToJson(String originRes) throws IOException {
        if(originRes.startsWith("<?xml")){
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode node = xmlMapper.readTree(originRes.getBytes());
            ObjectMapper jsonMapper = new ObjectMapper();
            originRes = jsonMapper.writeValueAsString(node);
        }
        return originRes;
    }

    @Test
        // 部分代码
    void resToJson() throws IOException {
        String result = given()
                .when()
//                .get("https://httpbin.ceshiren.com/get")
                .get("https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss")
                .then().extract().body().asString();

        // 调用转换成json的方法
        String res = resToJson(result);

        // 如果是向"https://httpbin.ceshiren.com/get"发起请求
//        String host = JsonPath.read(res, "$..Host");
//        assertEquals("httpbin.ceshiren.com", host);
//         如果是向"https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss"发起请求
//        System.out.println(res);
        List<String> titleList = JsonPath.read(res, "$..title");
        assertEquals(titleList.get(1),"NASA Image of the Day");
    }

}