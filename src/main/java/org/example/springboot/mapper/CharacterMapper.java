package org.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.springboot.pojo.Character;

@Mapper
public interface CharacterMapper {
    int insertCharacter(Integer user_id);
    Character getCharacterById(Integer user_id);
    int updateCharacter(Character character);
}
