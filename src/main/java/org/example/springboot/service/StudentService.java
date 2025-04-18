package org.example.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.springboot.mapper.StudentMapper;
import org.example.springboot.pojo.Student;
import org.example.springboot.pojo.Sutuo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PageInfo<Student> getStudentList(int pageNum, int pageSize, String academy,
                                            String className, String studentId, String studentName) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> students = studentMapper.selectStudentList(academy, className, studentId, studentName);
        return new PageInfo<>(students);
    }
}
