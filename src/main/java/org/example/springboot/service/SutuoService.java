package org.example.springboot.service;

import com.github.pagehelper.PageHelper;
import org.example.springboot.mapper.StudentMapper;
import org.example.springboot.mapper.SutuoMapper;
import org.example.springboot.pojo.Student;
import org.example.springboot.pojo.Sutuo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    studentMapper.AddStudent(sutuo);
                } catch (Exception e) {
                    throw new RuntimeException("更新学生总分失败，学生ID: " + sutuo.getStudentId() + ", 错误: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            // 捕获并重新抛出异常，保留事务回滚特性
            throw new RuntimeException("批量处理失败: " + e.getMessage());
        }
    }
    @Transactional
    public boolean UpdateSutuo(Sutuo newSutuo) {
        // 1. 先获取原始素拓记录
        Sutuo oldSutuo = sutuoMapper.getSutuoById(newSutuo.getId());
        if (oldSutuo == null) {
            return false;
        }
        try {
            studentMapper.ReduceStudent(oldSutuo);
            sutuoMapper.UpdateSutuo(newSutuo);
            studentMapper.AddStudent(newSutuo);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("更新素拓记录失败: " + e.getMessage());
        }
    }
    public List<Sutuo> SelectSutuo(String student_id) {
        return sutuoMapper.SelectSutuo(student_id);
    }

    public List<Sutuo> getAllSutuosByPage(Integer pageNum, Integer pageSize, String studentId, String activity) {
        PageHelper.startPage(pageNum, pageSize, "id DESC");
        if (studentId == null && activity == null) {
            return sutuoMapper.getAllSutuosByPage();
        }
        return sutuoMapper.searchSutuos(studentId, activity);
    }

    @Transactional
    public boolean deleteSutuo(Integer id) {
        // 1. 先获取素拓记录
        Sutuo sutuo = sutuoMapper.getSutuoById(id);
        if (sutuo == null) {
            return false;
        }

        try {
            // 2. 减去学生分数
            studentMapper.ReduceStudent(sutuo);

            // 3. 删除素拓记录
            sutuoMapper.deleteSutuo(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("删除素拓记录失败: " + e.getMessage());
        }
    }

    public Map<String, Object> getAnalysisData() {
        Map<String, Object> result = new HashMap<>();

        // 获取总览数据
        result.put("overview", sutuoMapper.getOverviewStats());

        // 获取平均分
        result.put("averageScore", sutuoMapper.getAverageScores());

        // 获取维度平均分
        result.put("dimensions", sutuoMapper.getDimensionsAverage());

        // 获取统计数据并转换为列表
        Map<String, Object> volunteerTrendMap = sutuoMapper.getVolunteerTrend();
        result.put("volunteerTrend", new ArrayList<>(volunteerTrendMap.values()));

        Map<String, Object> scoreRankingsMap = sutuoMapper.getTotalScoreRankings();
        result.put("scoreRankings", new ArrayList<>(scoreRankingsMap.values()));

        Map<String, Object> volunteerRankingsMap = sutuoMapper.getVolunteerTimeRankings();
        result.put("volunteerRankings", new ArrayList<>(volunteerRankingsMap.values()));

        return result;
    }
}
