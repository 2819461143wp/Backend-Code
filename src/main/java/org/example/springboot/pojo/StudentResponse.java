package org.example.springboot.pojo;

import lombok.Data;

import java.util.List;
@Data
public class StudentResponse {
    private Student student;
    private List<Sutuo> sutuoList;
    public StudentResponse(Student student, List<Sutuo> sutuoList) {
        this.student = student;
        this.sutuoList = sutuoList;
    }
}