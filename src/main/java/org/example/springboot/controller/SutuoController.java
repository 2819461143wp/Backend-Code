package org.example.springboot.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.springboot.mapper.FileRecordMapper;
import org.example.springboot.pojo.FileRecord;
import org.example.springboot.pojo.Sutuo;
import org.example.springboot.service.ExcelParserService;
import org.example.springboot.service.FileService;
import org.example.springboot.service.SutuoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sutuo")
public class SutuoController {
    @Autowired
    private FileService fileService;

    @Autowired
    private ExcelParserService excelParser;

    @Autowired
    private SutuoService sutuoService;

    @Autowired
    private FileRecordMapper fileRecordMapper;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam String operator
    ) {
        try {
            // 1. 处理文件上传并生成哈希
            String fileHash = fileService.processUpload(file, operator);

            // 2. 解析Excel数据
            List<Sutuo> sutuos = excelParser.parseExcel(file, fileHash);
            // 3. 批量插入并更新总分
            sutuoService.batchInsertWithScoreUpdate(sutuos);

            // 返回成功响应
            Map<String, Object> response = new HashMap<>();
            response.put("hash", fileHash);
            response.put("count", sutuos.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath) {
        // 1. 先检查文件路径是否为空
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        // 2. 检查文件是否存在
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new RuntimeException("文件不存在：" + filePath);
        }

        try {
            // 3. 创建资源并返回
            Resource resource = new FileSystemResource(path.toFile());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }
}