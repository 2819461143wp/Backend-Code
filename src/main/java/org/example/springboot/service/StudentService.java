package org.example.springboot.service;

import org.example.springboot.mapper.StudentMapper;
import org.example.springboot.pojo.Student;
import org.example.springboot.pojo.Sutuo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentMapper studentMapper;

    public boolean InsertStudent(Student student) {
        return studentMapper.InsertStudent(student) == 1;
    }

    public Student SelectStudent(Integer user_id) {
        return studentMapper.SelectStudent(user_id);
    }
}
