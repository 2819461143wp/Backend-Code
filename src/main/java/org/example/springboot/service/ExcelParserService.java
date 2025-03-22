package org.example.springboot.service;

import com.alibaba.excel.EasyExcel;
import org.example.springboot.pojo.Sutuo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelParserService {
    public List<Sutuo> parseExcel(MultipartFile file, String fileHash) throws IOException {
        return EasyExcel.read(file.getInputStream())    // 读取上传的Excel文件输入流
                .head(Sutuo.class)                      // 设置Excel表头对应的实体类
                .registerConverter(new Sutuo.StringToIntegerConverter()) // 显式注册
                .registerConverter(new Sutuo.StringToFloatConverter())
                .headRowNumber(1)
                .sheet()                                // 读取第一个sheet
                .doReadSync()                          // 同步读取Excel内容
                .stream()                              // 转换为流处理
                .map(obj -> (Sutuo) obj)              // 将读取的Object对象转换为Sutuo类型
                .peek(sutuo -> sutuo.setFileHash(fileHash))  // 为每个Sutuo对象设置文件哈希值
                .collect(Collectors.toList());         // 收集处理后的对象到List中
    }
}