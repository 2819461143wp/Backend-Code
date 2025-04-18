package org.example.springboot.controller;

import com.github.pagehelper.PageInfo;
import org.example.springboot.pojo.Student;
import org.example.springboot.pojo.StudentResponse;
import org.example.springboot.pojo.Sutuo;
import org.example.springboot.service.StudentService;
import org.example.springboot.service.SutuoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/list")
    public ResponseEntity<?> getStudentList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String academy,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String studentName) {
        PageInfo<Student> pageInfo = studentService.getStudentList(pageNum, pageSize,
                academy, className, studentId, studentName);
        Map<String, Object> result = new HashMap<>();
        result.put("students", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return ResponseEntity.ok(result);
    }
}