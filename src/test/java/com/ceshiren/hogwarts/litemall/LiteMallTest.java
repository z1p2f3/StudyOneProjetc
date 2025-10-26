package com.ceshiren.hogwarts.litemall;

import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LiteMallTest {
    static String adminToken;
    static String userToken;
    static Integer goodsId;
    static Integer productId;
    static String goodsSn = "20251017018";

    static String name = "宝杯";

    @BeforeAll
    @DisplayName("获取token")
    static void getCookie() {
        //获取管理员的token
        String adminLoginURL = "https://litemall.hogwarts.ceshiren.com/admin/auth/login";
        String adminLoginParam = "{\"username\":\"hogwarts\",\"password\":\"test12345\",\"code\":\"\"}";
        adminToken =
                given()
                        .body(adminLoginParam).contentType("application/json")
                .when()
                        .post(adminLoginURL)
                .then()
                        .extract().path("data.token");

        //获取用户的token
        String userLoginURL = "https://litemall.hogwarts.ceshiren.com/wx/auth/login";
        String userLoginParam = "{\"username\":\"user123\",\"password\":\"user123\"}";
        userToken =
                given()
                        .body(userLoginParam).contentType("application/json")
                .when()
                        .post(userLoginURL)
                .then()
                        .extract().path("data.token");

    }

    @Test
    @Order(1)
    @DisplayName("商品添加")
    void addBuyCar() {
        //商品添加
        String addProductURL = "https://litemall.hogwarts.ceshiren.com/admin/goods/create";
        String addProductParam = "{\"goods\":{\"picUrl\":\"\",\"gallery\":[],\"isHot\":false,\"isNew\":true,\"isOnSale\":true,\"goodsSn\":\"" + goodsSn + "\",\"name\":\"" + name + "\",\"counterPrice\":\"17\"},\"specifications\":[{\"specification\":\"规格\",\"value\":\"标准\",\"picUrl\":\"\"}],\"products\":[{\"id\":0,\"specifications\":[\"标准\"],\"price\":0,\"number\":10,\"url\":\"\"}],\"attributes\":[]}";
        given()
                .body(addProductParam)
                .contentType("application/json")
                .header("X-Litemall-Admin-Token", adminToken)
                .log().all()
        .when()
                .post(addProductURL)
        .then().statusCode(200)
                .log().all();

        //获取商品ID
        String getProductIdURL = "https://litemall.hogwarts.ceshiren.com/admin/goods/list";
        goodsId =
                given()
                        .header("X-Litemall-Admin-Token", adminToken)
                        .param("name", name)
                        .log().all()
                .when()
                        .get(getProductIdURL)
                .then()
                        .log().all().extract().path("data.list[0].id");
        System.out.println(goodsId);

        //获取商品库存ID
        String getProductInfoURL = "https://litemall.hogwarts.ceshiren.com/admin/goods/detail";
        productId =
                given()
                        .param("id", goodsId)
                        .contentType("application/json").header("X-Litemall-Admin-Token", adminToken)
                        .log().all()
                .when()
                        .get(getProductInfoURL)
                .then()
                        .log().all().extract().path("data.products[0].id");

        //加入购物车
        String addBuyCarURL = "https://litemall.hogwarts.ceshiren.com/wx/cart/add";
        String addBuyCarParam = "{\"goodsId\":" + goodsId + ",\"number\":1,\"productId\":" + productId + "}";
        String errmsg =
                given()
                        .body(addBuyCarParam)
                        .contentType("application/json").header("X-Litemall-Token", userToken)
                .when()
                        .post(addBuyCarURL)
                        .then()
                        .extract().path("errmsg");
        assertEquals("成功", errmsg);
    }

    @AfterAll
    @DisplayName("删除商品")
    static void delProduct() {
        String delProductURL = "https://litemall.hogwarts.ceshiren.com/admin/goods/delete";
        String delProductParam = "{\"id\":" + goodsId + ",\"goodsSn\":\"" + goodsSn + "\",\"name\":\"" + name + "\",\"categoryId\":0,\"brandId\":0,\"gallery\":[],\"keywords\":\"\",\"brief\":\"\",\"isOnSale\":true,\"sortOrder\":100,\"picUrl\":\"\",\"isNew\":true,\"isHot\":false,\"unit\":\"’件‘\",\"counterPrice\":17,\"retailPrice\":0,\"addTime\":\"2025-10-17 12:30:47\",\"updateTime\":\"2025-10-17 12:30:47\",\"deleted\":false,\"preview\":[\"\"]}";
        given()
                .body(delProductParam)
                .contentType("application/json")
                .header("X-Litemall-Admin-Token", adminToken)
                .log().all()
        .when()
                .post(delProductURL)
        .then()
                .statusCode(200)
                .log().all();
    }
}
