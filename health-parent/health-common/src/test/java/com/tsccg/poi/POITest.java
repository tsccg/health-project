package com.tsccg.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

/**
 * @Author: TSCCG
 * @Date: 2021/11/20 13:48
 * 使用POI操作Excel文件数据
 */
public class POITest {
    /**
     * 使用POI读取Excel文件数据
     */
    @Test
    public void readTest() throws IOException {
        //获取Excel文件
        XSSFWorkbook excel = new XSSFWorkbook(
                new FileInputStream(new File("D:\\Temp\\test.xlsx")));
        //获取工作表
        XSSFSheet sheet = excel.getSheetAt(0);
        //遍历工作表，获得每行数据
        for (Row cells : sheet) {
            //遍历每行数据，获得每个单元格
            for (Cell cell : cells) {
                //输出数据
                if(cell.getCellType() == 1) {//数据类型为String
                    System.out.println(cell.getStringCellValue());
                } else if(cell.getCellType() == 0) {//数据类型为int
                    System.out.println(cell.getNumericCellValue());
                }
                System.out.println(cell.getCellType());
            }
        }
        //关闭资源
        excel.close();
    }

    /**
     * 使用POI读取Excel文件数据
     * 根据行号遍历工作表
     */
    @Test
    public void readTest2() throws IOException {
        //获取Excel文件
        XSSFWorkbook excel = new XSSFWorkbook(
                new FileInputStream(new File("D:\\Temp\\test.xlsx")));
        //获取工作表
        XSSFSheet sheet = excel.getSheetAt(0);
        //获取当前工作表有效数据的最后一行行号，从0开始
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("最后一行行号："+lastRowNum);
        //根据行号执行for循环，遍历工作表
        for (int i = 0; i <= lastRowNum; i++) {
            //获取行对象
            XSSFRow row = sheet.getRow(i);
            //获取当前行有效数据的最后一列列号，从1开始
            short lastCellNum = row.getLastCellNum();
            System.out.println("最后一列列号："+lastCellNum);
            for (int j = 0; j < lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                if(cell.getCellType() == 0) {
                    System.out.println(cell.getNumericCellValue());
                } else if(cell.getCellType() == 1) {
                    System.out.println(cell.getStringCellValue());
                }
            }
        }
        //关闭资源
        excel.close();
    }

    @Test
    public void writeTest() throws IOException {
        //在内存创建一个excel文件
        XSSFWorkbook excel = new XSSFWorkbook();
        //创建一个工作表，制定工作表名称
        XSSFSheet sheet1 = excel.createSheet("sheet1");
        //创建标题行，0代表第一行
        XSSFRow titleRow = sheet1.createRow(0);
        //创建单元格，0代表第一个单元格
        titleRow.createCell(0).setCellValue("编号");
        titleRow.createCell(1).setCellValue("姓名");
        titleRow.createCell(2).setCellValue("年龄");

        //创建数据行，1代表第二行
        XSSFRow dataRow = sheet1.createRow(1);
        dataRow.createCell(0).setCellValue("001");
        dataRow.createCell(1).setCellValue("张三");
        dataRow.createCell(2).setCellValue(20);

        //创建输出流，将excel对象写入到磁盘文件内
        FileOutputStream out = new FileOutputStream(new File("D:\\Temp\\OutTest.xlsx"));
        excel.write(out);
        out.flush();
        out.close();
        excel.close();
    }
}
