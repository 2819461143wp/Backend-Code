package org.example.springboot.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DocumentInfo {
    private String filename;
    private String description;
    private long size;
    private LocalDateTime lastModified;
}