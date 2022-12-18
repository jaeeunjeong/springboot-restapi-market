package learning.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;

public class ExcelFileTest {
    public static void main(String[] args) throws Exception{
        XSSFWorkbook xssfWorkbook = null;
        XSSFSheet xssfSheet = null;
        XSSFRow xssfRow = null;
        XSSFCell xssfCell = null;

        try {
            int rowNo = 0;

            xssfWorkbook = new XSSFWorkbook();
            xssfSheet = xssfWorkbook.createSheet("test1");

            //테이블 셀 스타일
            CellStyle cellStyle = xssfWorkbook.createCellStyle();
            xssfSheet.setColumnWidth(0, (xssfSheet.getColumnWidth(0))+(short)2048); // 0번째 컬럼 넓이 조절

            cellStyle.setAlignment(HorizontalAlignment.CENTER); // 정렬

            //셀병합
            xssfSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 4)); //첫행, 마지막행, 첫열, 마지막열 병합

            // 타이틀 생성
            xssfRow = xssfSheet.createRow(rowNo++); // 행 객체 추가
            xssfCell = xssfRow.createCell((short) 0); // 추가한 행에 셀 객체 추가
            xssfCell.setCellStyle(cellStyle); // 셀에 스타일 지정
            xssfCell.setCellValue("타이틀"); // 데이터 입력

            xssfSheet.createRow(rowNo++);
            xssfRow = xssfSheet.createRow(rowNo++);  // 빈행 추가

            xssfRow = xssfSheet.createRow(rowNo++);
            xssfCell = xssfRow.createCell((short) 0);
            xssfCell.setCellValue("셀1");
            xssfCell = xssfRow.createCell((short) 1);
            xssfCell.setCellValue("셀2");
            xssfCell = xssfRow.createCell((short) 2);
            xssfCell.setCellValue("셀3");
            xssfCell = xssfRow.createCell((short) 3);
            xssfCell.setCellValue("셀4");
            xssfCell = xssfRow.createCell((short) 4);

            String localFile = "C:/User/Owner/Downloads" + "excelDownTest" + ".xlsx";

            File file = new File(localFile);
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            xssfWorkbook.write(fos);

            if (fos != null) fos.close();

        } catch (Exception e) {

        }
    }
}
