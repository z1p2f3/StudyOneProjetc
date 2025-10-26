package com.doubao.study;

import com.alibaba.excel.EasyExcel;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.*;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * 接口自动化测试类（Excel数据驱动版）
 * 功能：从Excel读取用例→自动执行→断言结果→打印报告
 */
public class ExcelDrivenApiTest {

    // 全局存储依赖参数（如登录后的token）
    private static final Map<String, String> DEPEND_DATA = new HashMap<>();
    // 接口基础URL（替换为你的实际域名）
    private static final String BASE_URL = "http://dataops-dev.senses-ai.com";
    // Excel用例文件路径（固定路径，无需修改）
    private static final String EXCEL_PATH = "src/test/resources/api_cases.xlsx";

    /**
     * 初始化配置：所有用例执行前运行一次
     */
    @BeforeAll
    public static void globalSetup() {
        // 设置接口基础URL
        baseURI = BASE_URL;
        // 开启日志：请求/响应失败时打印详情（方便调试）
        enableLoggingOfRequestAndResponseIfValidationFails();
        // 设置超时时间（10秒）
//        RestAssured.timeout = 10000;
    }

    /**
     * 核心测试方法：读取Excel用例并执行
     */
    @Test
    public void runAllCasesFromExcel() {
        System.out.println("===== 开始执行Excel数据驱动测试 =====");

        // 1. 从Excel读取所有用例
        List<ApiCase> caseList = readExcelCases();
        if (caseList.isEmpty()) {
            fail("未从Excel中读取到用例，请检查文件路径或格式！");
        }

        // 2. 按Excel序号顺序执行用例（0→1→2→3→4）
        for (ApiCase apiCase : caseList) {
            System.out.println("\n===== 执行用例【" + apiCase.getCaseName() + "】（序号：" + apiCase.getId() + "） =====");
            try {
                // 执行单条用例
                executeSingleCase(apiCase);
                System.out.println("用例【" + apiCase.getCaseName() + "】执行成功！");
            } catch (Exception e) {
                System.err.println("用例【" + apiCase.getCaseName() + "】执行失败：" + e.getMessage());
                // 如需失败后终止所有用例，解开下面一行注释
                // fail("用例执行失败：" + e.getMessage());
            }
        }

        System.out.println("\n===== 所有用例执行完毕 =====");
    }

    /**
     * 读取Excel用例文件
     */
    private List<ApiCase> readExcelCases() {
        try {
            // 用EasyExcel读取Excel，映射到ApiCase实体类
            return EasyExcel.read(EXCEL_PATH)
                    .head(ApiCase.class) // 绑定实体类
                    .sheet(0) // 读取第一个sheet（索引从0开始）
                    .doReadSync(); // 同步读取
        } catch (Exception e) {
            fail("读取Excel失败：" + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 执行单条用例的完整流程
     */
    private void executeSingleCase(ApiCase apiCase) {
        // 步骤1：解析请求头（处理动态参数如{{token}}）
        Map<String, String> headers = parseHeaders(apiCase.getHeaders());

        // 步骤2：解析请求参数（替换动态参数）
        String requestParams = replaceDynamicParams(apiCase.getParams());

        // 步骤3：发送接口请求
        Response response = sendRequest(
                apiCase.getMethod(),
                apiCase.getUrl(),
                headers,
                requestParams
        );

        // 步骤4：断言响应结果（状态码+响应体）
        assertResponse(response, apiCase);

        // 步骤5：提取依赖参数（如登录用例的token）
        extractDependData(apiCase, response);
    }

    /**
     * 解析请求头（从Excel的字符串转成Map）
     * 示例："Authorization=Bearer {{token}}; Content-Type=application/json" → Map
     */
    private Map<String, String> parseHeaders(String headersStr) {
        Map<String, String> headers = new HashMap<>();
        if (headersStr == null || headersStr.trim().isEmpty()) {
            return headers;
        }

        // 按分号分割多个请求头
        String[] headerPairs = headersStr.split(";");
        for (String pair : headerPairs) {
            // 按第一个"="分割键和值（避免值中包含=）
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = replaceDynamicParams(keyValue[1].trim()); // 替换动态参数
                headers.put(key, value);
            }
        }
        return headers;
    }

    /**
     * 替换动态参数（如{{token}} → 实际token值）
     */
    private String replaceDynamicParams(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        // 遍历所有依赖参数，替换{{变量名}}
        for (Map.Entry<String, String> entry : DEPEND_DATA.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            if (content.contains(placeholder)) {
                content = content.replace(placeholder, entry.getValue());
                System.out.println("替换动态参数：" + placeholder + " → " + entry.getValue());
            }
        }
        return content;
    }

    /**
     * 发送接口请求（支持POST/GET等方法）
     */
    private Response sendRequest(String method, String url, Map<String, String> headers, String params) {
        // 初始化请求，设置请求头
        var request = given().headers(headers);

        // 根据请求方法发送请求
        switch (method.toUpperCase()) {
            case "POST":
                return request
                        .contentType(ContentType.JSON) // 声明JSON格式
                        .body(params) // 设置请求体
                        .when()
                        .post(url); // 发送POST请求
            case "GET":
                return request
                        .params(parseQueryParams(params)) // 解析GET参数
                        .when()
                        .get(url); // 发送GET请求
            default:
                throw new RuntimeException("不支持的请求方法：" + method);
        }
    }

    /**
     * 解析GET请求的Query参数（如"page=1; size=10" → Map）
     */
    private Map<String, String> parseQueryParams(String paramsStr) {
        Map<String, String> queryParams = new HashMap<>();
        if (paramsStr == null || paramsStr.trim().isEmpty()) {
            return queryParams;
        }

        String[] pairs = paramsStr.split(";");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return queryParams;
    }

    /**
     * 断言响应结果（状态码+响应体）
     */
    private void assertResponse(Response response, ApiCase apiCase) {
        // 1. 断言状态码
        int expectedStatusCode = apiCase.getExpectedStatusCode();
        response.then().statusCode(expectedStatusCode);
        System.out.println("状态码断言成功：预期" + expectedStatusCode + "，实际" + response.getStatusCode());

        // 2. 断言响应体（解析Excel中的预期响应体规则）
        String expectedResponse = apiCase.getExpectedResponse();
        if (expectedResponse == null || expectedResponse.trim().isEmpty()) {
            return;
        }

        // 按分号分割多个断言规则（如"code=1; data.token!=null"）
        String[] assertions = expectedResponse.split(";");
        for (String assertion : assertions) {
            assertion = assertion.trim();
            if (assertion.contains("!=")) { // 非空断言（如data.token!=null）
                String[] keyValue = assertion.split("!=", 2);
                String key = keyValue[0].trim();
                String expected = keyValue[1].trim();
                if ("null".equals(expected)) {
                    // 断言字段不为空
                    response.then().body(key, notNullValue());
                    System.out.println("响应体断言成功：" + key + " != null");
                }
            } else if (assertion.contains("=")) { // 等于断言（如code=1、data.records=[]）
                // 关键修改：用indexOf定位第一个"="，避免split拆分特殊字符（如[]）
                int equalIndex = assertion.indexOf("=");
                if (equalIndex == -1) {
                    continue; // 无效的断言格式，跳过
                }
                String key = assertion.substring(0, equalIndex).trim();
                String expectedValue = assertion.substring(equalIndex + 1).trim();

                // 新增：处理空数组断言（data.records=[]）
                if ("[]".equals(expectedValue)) {
                    response.then().body(key, empty()); // 用empty()判断空数组
                    System.out.println("响应体断言成功：" + key + " 是空数组（[]）");
                    continue; // 处理完毕，继续下一个断言
                }

                // 原有逻辑：处理数字类型和字符串类型
                if (expectedValue.matches("\\d+")) { // 数字（如code=1、total=0）
                    response.then().body(key, equalTo(Integer.parseInt(expectedValue)));
                } else { // 字符串（如msg=操作成功）
                    response.then().body(key, equalTo(expectedValue));
                }
                System.out.println("响应体断言成功：" + key + " = " + expectedValue);
            }
        }
    }

    /**
     * 提取依赖参数（如登录用例的token）
     */
    private void extractDependData(ApiCase apiCase, Response response) {
        // 只处理登录用例的token提取（根据模块和用例名称判断）
        if ("登录".equals(apiCase.getModule()) && apiCase.getCaseName().contains("登录成功")) {
            // 从响应体中提取data.token（对应Excel中“预期响应体=data.token!=null”）
            String token = response.jsonPath().getString("data.token");
            if (token == null || token.trim().isEmpty()) {
                throw new RuntimeException("登录用例未提取到token！");
            }
            // 存入全局依赖Map，供后续用例引用
            DEPEND_DATA.put("token", token);
            System.out.println("成功提取token：" + token);
        }

        // 可扩展：提取其他依赖参数（如创建用户的userId）
        // 示例：if ("用户管理".equals(apiCase.getModule()) && apiCase.getCaseName().contains("创建用户（参数完整）")) {
        //     String userId = response.jsonPath().getString("data.records.id");
        //     DEPEND_DATA.put("userId", userId);
        // }
    }

    /**
     * 用例实体类（与Excel字段一一对应）
     */
    @Data // Lombok自动生成getter/setter
    public static class ApiCase {
        private int id; // 序号
        private String module; // 模块
        private String caseName; // 用例名称
        private String method; // 请求方法
        private String url; // 请求URL
        private String headers; // 请求头
        private String params; // 请求参数
        private int expectedStatusCode; // 预期状态码
        private String expectedResponse; // 预期响应体
    }
}
