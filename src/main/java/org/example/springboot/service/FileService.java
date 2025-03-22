package org.example.springboot.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.springboot.mapper.FileRecordMapper;
import org.example.springboot.pojo.FileRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class FileService {
    @Autowired
    private FileRecordMapper fileRecordMapper;

    private String uploadDir="F:/dachuang/source/excels";

    public String processUpload(MultipartFile file, String operator) throws IOException {
        // 计算上传文件的SHA-256哈希值
        String hash = DigestUtils.sha256Hex(file.getBytes());

        // 创建目录对象
        Path dir = Paths.get(uploadDir);
        // 确保上传目录存在
        Files.createDirectories(dir);

        // 生成文件保存路径（使用哈希值作为文件名）
        Path filePath = dir.resolve(hash + ".xlsx");
        // 将文件内容写入目标路径
        Files.write(filePath, file.getBytes());

        FileRecord record = new FileRecord();
        record.setHash(hash);
        record.setFilePath(filePath.toString());
        record.setUploadTime(new Date());
        record.setOperator(operator);
        fileRecordMapper.insertFileRecord(record);
        return hash;
    }
}