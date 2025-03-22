package org.example.springboot.pojo;

import lombok.Data;
import java.util.Date;
@Data
public class FileRecord {
    private Integer id;
    private String hash;
    private String filePath;
    private Date uploadTime;
    private String operator;
}
