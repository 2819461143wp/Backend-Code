package org.example.springboot.service;

import org.example.springboot.pojo.DocumentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    private static final String DOCS_DIR = "F:/dachuang/source/documents";

    @Autowired
    private DocumentIndexService documentIndexService;

    public void uploadDocument(MultipartFile file, String description) throws IOException {
        // 确保目录存在
        File directory = new File(DOCS_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".docx")) {
            throw new IOException("仅支持.docx格式的文件");
        }

        // 保存文件
        Path filePath = Paths.get(DOCS_DIR, originalFilename);
        Files.copy(file.getInputStream(), filePath);

        // 重建索引
        rebuildIndex();
    }

    public void rebuildIndex() throws IOException {
        documentIndexService.indexDocuments();
    }

    public List<DocumentInfo> listDocuments() throws IOException {
        List<DocumentInfo> documents = new ArrayList<>();
        File directory = new File(DOCS_DIR);

        if (!directory.exists()) {
            return documents;
        }

        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".docx"));
        if (files != null) {
            for (File file : files) {
                DocumentInfo info = new DocumentInfo();
                info.setFilename(file.getName());
                info.setSize(file.length());
                info.setLastModified(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(file.lastModified()),
                        ZoneId.systemDefault()
                ));
                documents.add(info);
            }
        }
        return documents;
    }

    public void deleteDocument(String filename) throws IOException {
        File file = new File(DOCS_DIR, filename);
        if (!file.exists()) {
            throw new IOException("文件不存在");
        }

        // 验证文件扩展名
        if (!filename.toLowerCase().endsWith(".docx")) {
            throw new IOException("无效的文件类型");
        }

        // 检查文件路径，防止目录遍历攻击
        if (!file.getCanonicalPath().startsWith(new File(DOCS_DIR).getCanonicalPath())) {
            throw new IOException("无效的文件路径");
        }

        if (!file.delete()) {
            throw new IOException("文件删除失败");
        }

        // 重建索引
        rebuildIndex();
    }
}