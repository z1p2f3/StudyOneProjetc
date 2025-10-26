package com.doubao.study;

import com.alibaba.excel.EasyExcel;

import java.util.List;

public class ExcelUtils {
    public static List<ApiCase> readExcel(String filePath) {
        return EasyExcel.read(filePath)
                .head(ApiCase.class)  // 映射实体类
                .sheet()  // 读取第一个sheet
                .doReadSync();  // 同步读取
    }

}
