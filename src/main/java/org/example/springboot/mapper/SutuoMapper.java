package org.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.springboot.pojo.Sutuo;

import java.util.List;

@Mapper
public interface SutuoMapper {
    Integer InsertSutuo(Sutuo sutuo);
    Integer UpdateSutuo(Sutuo sutuo);
    List<Sutuo> SelectSutuo(String student_id);
}
