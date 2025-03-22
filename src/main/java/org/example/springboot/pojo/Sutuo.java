package org.example.springboot.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.Data;

@Data
public class Sutuo {
    // 更新后的StringToIntegerConverter
    public static class StringToIntegerConverter implements Converter<Integer> {
        @Override
        public Class<?> supportJavaTypeKey() {
            return Integer.class;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return null; // 处理所有类型
        }

        @Override
        public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                         GlobalConfiguration globalConfiguration) {
            if (cellData == null) return 0; // 空值返回0，避免null

            switch (cellData.getType()) {
                case NUMBER:
                    return cellData.getNumberValue().intValue();
                case STRING:
                    String value = cellData.getStringValue().trim();
                    if (value.isEmpty()) return 0;
                    try {
                        return Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        return 0; // 解析失败返回0
                    }
                case BOOLEAN:
                    return cellData.getBooleanValue() ? 1 : 0; // 布尔转0/1
                default:
                    return 0; // 其他类型默认0
            }
        }
    }
    // 更新后的StringToFloatConverter
    public static class StringToFloatConverter implements Converter<Float> {
        @Override
        public Class<?> supportJavaTypeKey() {
            return Float.class;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return null; // 支持所有类型
        }

        @Override
        public Float convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
            if (cellData == null) {
                return null;
            }
            if (cellData.getType() == CellDataTypeEnum.NUMBER) {
                return cellData.getNumberValue().floatValue();
            } else if (cellData.getType() == CellDataTypeEnum.STRING) {
                String value = cellData.getStringValue().trim();
                if (value.isEmpty()) return null;
                try {
                    return Float.parseFloat(value);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        }
    }
    private Integer id;

    @ExcelProperty(value = "学号")
    private String studentId;

    @ExcelProperty(value = "活动名称")
    private String activity;

    @ExcelProperty(index = 5, converter = StringToIntegerConverter.class)
    private Integer deyu;

    @ExcelProperty(index = 6, converter = StringToIntegerConverter.class)
    private Integer zhiyu;

    @ExcelProperty(index = 7, converter = StringToIntegerConverter.class)
    private Integer meiyu;

    @ExcelProperty(index = 8, converter = StringToIntegerConverter.class)
    private Integer tiyu;

    @ExcelProperty(index = 9, converter = StringToIntegerConverter.class)
    private Integer xiaoyuan;

    @ExcelProperty(index = 10, converter = StringToIntegerConverter.class)
    private Integer xiangtu;

    @ExcelProperty(index = 11, converter = StringToIntegerConverter.class)
    private Integer chanxue;

    @ExcelProperty(index = 12, converter = StringToIntegerConverter.class)
    private Integer jiating;

    @ExcelProperty(index = 13, converter = StringToIntegerConverter.class)
    private Integer qingshi;

    @ExcelProperty(index = 14, converter = StringToFloatConverter.class)
    private Float volunteerTime;

    private String fileHash;
}