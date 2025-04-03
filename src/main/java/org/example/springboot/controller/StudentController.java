package org.example.springboot.controller;

import org.example.springboot.pojo.Student;
import org.example.springboot.pojo.StudentResponse;
import org.example.springboot.pojo.Sutuo;
import org.example.springboot.service.StudentService;
import org.example.springboot.service.SutuoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private SutuoService sutuoService;

    @PostMapping("/insert")
    public String insertStudent() {
        return "";
    }

    @GetMapping("/lookfor")
    public ResponseEntity<StudentResponse> lookForStudent(@RequestParam Integer user_id) {
        Student student = studentService.SelectStudent(user_id);
        List<Sutuo> sutuoList = sutuoService.SelectSutuo(String.valueOf(student.getId()));
        StudentResponse response = new StudentResponse(student, sutuoList);
        return ResponseEntity.ok(response);
    }
}