package org.example.springboot.controller;

import org.example.springboot.pojo.DocumentInfo;
import org.example.springboot.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description) {
        try {
            documentService.uploadDocument(file, description);
            return ResponseEntity.ok("文档上传成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("文档上传失败: " + e.getMessage());
        }
    }

    @GetMapping("/reindex")
    public ResponseEntity<String> rebuildIndex() {
        try {
            documentService.rebuildIndex();
            return ResponseEntity.ok("索引重建成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("索引重建失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<DocumentInfo>> listDocuments() {
        try {
            List<DocumentInfo> documents = documentService.listDocuments();
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<String> deleteDocument(@PathVariable String filename) {
        try {
            documentService.deleteDocument(filename);
            return ResponseEntity.ok("文档删除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("文档删除失败: " + e.getMessage());
        }
    }
}