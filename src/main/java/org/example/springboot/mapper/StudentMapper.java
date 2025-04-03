package org.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.springboot.pojo.Student;
import org.example.springboot.pojo.Sutuo;

@Mapper
public interface StudentMapper {
    Integer InsertStudent(Student student);
    Integer AddStudent(Sutuo sutuo);
    Integer ReduceStudent(Sutuo sutuo);
    Student SelectStudent(Integer user_id);

}
