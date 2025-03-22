package org.example.springboot.service;

import org.example.springboot.mapper.StudentMapper;
import org.example.springboot.mapper.SutuoMapper;
import org.example.springboot.pojo.Student;
import org.example.springboot.pojo.Sutuo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SutuoService {
    @Autowired
    private SutuoMapper sutuoMapper;

    @Autowired
    private StudentMapper studentMapper;



    @Transactional
    public void batchInsertWithScoreUpdate(List<Sutuo> sutuos) {
        try {
            for (Sutuo sutuo : sutuos) {
                try {
                    System.out.println(sutuo);
                    sutuoMapper.InsertSutuo(sutuo);
                } catch (Exception e) {
                    throw new RuntimeException("插入素拓记录失败，学生ID: " + sutuo.getStudentId() + ", 错误: " + e.getMessage());
                }

                try {
                    // 更新学生总分
                    studentMapper.UpdateStudent(sutuo);
                } catch (Exception e) {
                    throw new RuntimeException("更新学生总分失败，学生ID: " + sutuo.getStudentId() + ", 错误: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            // 捕获并重新抛出异常，保留事务回滚特性
            throw new RuntimeException("批量处理失败: " + e.getMessage());
        }
    }
    public boolean UpdateSutuo(Sutuo sutuo) {
        return sutuoMapper.UpdateSutuo(sutuo) == 1;
    }

    public List<Sutuo> SelectSutuo(String student_id) {
        return sutuoMapper.SelectSutuo(student_id);
    }
}
