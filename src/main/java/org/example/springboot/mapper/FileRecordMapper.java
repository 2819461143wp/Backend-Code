package org.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.springboot.pojo.FileRecord;

@Mapper
public interface FileRecordMapper {
    Integer insertFileRecord(FileRecord record);
    FileRecord selectByHash(String hash);
}
