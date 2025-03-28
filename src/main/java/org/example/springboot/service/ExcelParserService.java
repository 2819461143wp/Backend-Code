package org.example.springboot.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.example.springboot.pojo.Student;
import org.example.springboot.pojo.Sutuo;
import org.example.springboot.pojo.User;
import org.example.springboot.pojo.UserStudentData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExcelParserService {

    // Sutuo Excel 解析方法（保持不变）
    public List<Sutuo> parseSutoExcel(MultipartFile file, String fileHash) throws IOException {
        return EasyExcel.read(file.getInputStream())
                .head(Sutuo.class)
                .registerConverter(new Sutuo.StringToIntegerConverter())
                .registerConverter(new Sutuo.StringToFloatConverter())
                .headRowNumber(1)
                .sheet()
                .doReadSync()
                .stream()
                .map(obj -> (Sutuo) obj)
                .peek(sutuo -> sutuo.setFileHash(fileHash))
                .collect(Collectors.toList());
    }

    // 解析用户和学生信息的Excel，并返回解析后的数据
    public List<UserStudentData> parseUserStudentExcel(MultipartFile file) throws IOException {
        List<UserStudentData> result = new ArrayList<>();

        // 使用自定义监听器读取Excel
        EasyExcel.read(file.getInputStream(), new AnalysisEventListener<Map<Integer, String>>() {

            @Override
            public void invoke(Map<Integer, String> data, AnalysisContext context) {
                // 创建数据对象
                UserStudentData userStudentData = new UserStudentData();
                User user = userStudentData.getUser();
                Student student = userStudentData.getStudent();

                // 根据Excel的列顺序填充数据（学院0, 班级1, 姓名2, 学号3, 账号4, 密码5）
                student.setAcademy(data.get(0));   // 学院
                student.setClassName(data.get(1));  // 班级
                student.setStudentName(data.get(2)); // 姓名
                student.setId(data.get(3));        // 学号

                user.setUsername(data.get(4));     // 账号
                user.setPassword(data.get(5));     // 密码
                user.setRole("USER");              // 默认角色

                // 将解析的数据添加到结果列表
                result.add(userStudentData);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                // 处理完成后的回调
            }
        }).sheet().doRead();

        return result;
    }
}