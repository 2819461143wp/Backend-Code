package org.example.springboot.service;

import org.example.springboot.mapper.CharacterMapper;
import org.example.springboot.pojo.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    @Autowired
    private CharacterMapper CharacterMapper;
    //注册时插入新角色
    public boolean insertCharacter(Integer user_id) {
        Integer result = CharacterMapper.insertCharacter(user_id);
        return result>0;
    }
    //根据用户id获取角色信息
    public Character getCharacterById(Integer user_id) {
        return CharacterMapper.getCharacterById(user_id);
    }

    //更新角色信息
    public boolean updateCharacter(Integer user_id, String name, String biography, String avatar_url) {
        Character character = CharacterMapper.getCharacterById(user_id);
        if (character == null) {
            return false;
        }
        if (name != null && !name.isEmpty()) {
            character.setName(name);
        }
        if (biography != null && !biography.isEmpty()) {
            character.setBiography(biography);
        }
        if (avatar_url != null && !avatar_url.isEmpty()) {
            character.setAvatarUrl(avatar_url);
        }
        Integer result = CharacterMapper.updateCharacter(character);
        return result > 0;
    }
}
