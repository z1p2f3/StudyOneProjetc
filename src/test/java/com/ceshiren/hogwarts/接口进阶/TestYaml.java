package com.ceshiren.hogwarts.接口进阶;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static io.restassured.RestAssured.given;

public class TestYaml {
    @BeforeAll
    static void setupClass() throws IOException {
        /*
        使用Jackson读取yaml文件
         */

        // 实例化一个ObjectMapper 对象
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        // 读取 resources 目录中的envs.yaml文件
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File yamlFile = new File(Objects.requireNonNull(classLoader.getResource("envs.yaml")).getFile());
        // 定义序列化的结构 TypeReference
        TypeReference<HashMap<String, String>> typeRef = new
                TypeReference<HashMap<String, String>>() {};
        // 解析envs.yaml文件内容
        HashMap<String, String> envs = objectMapper.readValue(yamlFile, typeRef);
        // 设置基路径，值为选定的域名地址
        RestAssured.baseURI = envs.get(envs.get("default"));
    }

    @Test
    void testEnvs() {

        //发起请求
        given()
                .log().all()
        .when()
                .get("/get")
        .then().log().all()
                .statusCode(200);
    }
}
