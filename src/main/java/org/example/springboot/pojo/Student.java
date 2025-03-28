package org.example.springboot.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Student {
    @ExcelProperty("学号")
    private String id;
    private Integer userId;
    @ExcelProperty("姓名")
    private String studentName;
    @ExcelProperty("学院")
    private String academy;
    @ExcelProperty("班级")
    private String className;
    private Integer deyu;
    private Integer zhiyu;
    private Integer meiyu;
    private Integer tiyu;
    private Integer xiaoyuan;
    private Integer xiangtu;
    private Integer chanxue;
    private Integer jiating;
    private Integer qingshi;
    private Float volunteerTime;
}