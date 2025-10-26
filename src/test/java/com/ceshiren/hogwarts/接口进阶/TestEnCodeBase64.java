package com.ceshiren.hogwarts.接口进阶;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TestEnCodeBase64 {
    @Test
    void enCode(){
        //获取字符串数组
        byte[] bytes = "zhangpengfei".getBytes(StandardCharsets.UTF_8);
        String encoded = Base64.encodeBase64String(bytes);
        System.out.println("加密：" + encoded);

        byte[] intArr = Base64.decodeBase64("encoded");
        String decode = new String(intArr,StandardCharsets.UTF_8);
        System.out.println("数字解密：" + Arrays.toString(intArr));
        System.out.println("解密：" + decode);

    }
}
