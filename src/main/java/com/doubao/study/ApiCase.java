package com.doubao.study;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data  // 用 Lombok 简化 Getter/Setter
public class ApiCase {
    @ExcelProperty(index = 0)  // 对应 Excel 第1列（序号）
    private int id;

    @ExcelProperty(index = 1)  // 模块
    private String module;

    @ExcelProperty(index = 2)  // 用例名称
    private String caseName;

    @ExcelProperty(index = 3)  // 请求方法
    private String method;

    @ExcelProperty(index = 4)  // 请求URL
    private String url;

    @ExcelProperty(index = 5)  // 请求头
    private String headers;

    @ExcelProperty(index = 6)  // 请求参数
    private String params;

    @ExcelProperty(index = 7)  // 依赖接口
    private String dependCase;

    @ExcelProperty(index = 8)  // 预期状态码
    private int expectedStatusCode;

    @ExcelProperty(index = 9)  // 预期响应体
    private String expectedResponse;

    @ExcelProperty(index = 10) // 优先级
    private String priority;

    @ExcelProperty(index = 11) // 备注
    private String remark;
}
