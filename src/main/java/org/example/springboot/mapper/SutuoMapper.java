package org.example.springboot.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.example.springboot.pojo.Sutuo;

import java.util.List;
import java.util.Map;

@Mapper
public interface SutuoMapper {
    Integer InsertSutuo(Sutuo sutuo);
    Integer UpdateSutuo(Sutuo sutuo);
    Integer deleteSutuo(Integer id);
    Sutuo getSutuoById(Integer id);
    List<Sutuo> SelectSutuo(String student_id);
    List<Sutuo> getAllSutuosByPage();
    List<Sutuo> searchSutuos(String studentId, String activity);

    @MapKey("name")
    Map<String, Object> getOverviewStats();

    Double getAverageScores();

    @MapKey("dimension")
    Map<String, Object> getDimensionsAverage();

    @MapKey("month")
    Map<String, Object> getVolunteerTrend();

    @MapKey("student_id")
    Map<String, Object> getTotalScoreRankings();

    @MapKey("student_id")
    Map<String, Object> getVolunteerTimeRankings();
}