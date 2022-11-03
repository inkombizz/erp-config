package com.inkombizz.utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.inkombizz.common.enums.EnumDataType;
import com.inkombizz.common.enums.EnumDataType.ENUM_DataType;
import static com.inkombizz.common.enums.EnumDataType.ENUM_DataType.CURRENCY;
import static com.inkombizz.common.enums.EnumDataType.ENUM_DataType.DATETIME;
import static com.inkombizz.common.enums.EnumDataType.ENUM_DataType.DOUBLE;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author ahmad
 */
public class IOExcel {

  public static Workbook wb = null;
  public static Sheet sheet = null;
  public static DataFormat format = null;
  public static Row row = null;
  public static Cell cell = null;
  public static CellStyle style = null;
  public static Font font = null;
  public static String font_name = null;
  public static int cols = 0;
  public static boolean isBold = false;
  public static int idx = 0;
  public static InputStream streamFile = null;
  public static String sheetName = null;
  public static Map<String, Object[]> listData = null;
  public static Font font_bold = null;
  public static Font font_normal = null;
  public static CellStyle style_left = null;
  public static CellStyle style_center = null;
  public static CellStyle style_right = null;
  public static CellStyle style_datetime = null;
  public static CreationHelper createHelper = null;

  public static void start() {
    IOExcel.wb = null;
    IOExcel.sheet = null;
    IOExcel.row = null;
    IOExcel.cell = null;
    IOExcel.style = null;
    IOExcel.font = null;
    IOExcel.font_name = null;
    IOExcel.cols = 0;
    IOExcel.isBold = false;
    IOExcel.idx = 0;
    IOExcel.streamFile = null;
    IOExcel.sheetName = null;
    IOExcel.listData = null;
  }

  public static void importUtils(Workbook wb) {
    IOExcel.start();
    IOExcel.wb = wb;
    IOExcel.sheet = wb.getSheetAt(0);
    IOExcel.format = wb.createDataFormat();
    IOExcel.createHelper = wb.getCreationHelper();

    IOExcel.font_bold = IOExcel.wb.createFont();
    IOExcel.font_bold.setFontHeightInPoints((short) 11);
    IOExcel.font_bold.setBold(true);

    IOExcel.font_normal = IOExcel.wb.createFont();
    IOExcel.font_normal.setFontHeightInPoints((short) 11);
    IOExcel.font_bold.setBold(false);

    IOExcel.style_left = IOExcel.wb.createCellStyle();
    IOExcel.style_left.setAlignment(HorizontalAlignment.LEFT);

    IOExcel.style_center = IOExcel.wb.createCellStyle();
    IOExcel.style_center.setAlignment(HorizontalAlignment.CENTER);

    IOExcel.style_right = IOExcel.wb.createCellStyle();
    IOExcel.style_right.setAlignment(HorizontalAlignment.RIGHT);

    IOExcel.style_datetime = IOExcel.wb.createCellStyle();
    IOExcel.style_datetime.setDataFormat(IOExcel.createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
    IOExcel.style_datetime.setAlignment(HorizontalAlignment.CENTER);
  }

  public static void importUtils(Workbook wb, String name) {
    IOExcel.wb = wb;
    IOExcel.sheet = wb.getSheet(name);
  }

  public static void importExcel() {
    Iterator<Row> rowIterator = IOExcel.sheet.iterator();
    Map<String, Object[]> list = new HashMap<String, Object[]>();
    while (rowIterator.hasNext()) {
      IOExcel.row = rowIterator.next();
      List<String> data = new ArrayList<>();
      if (IOExcel.row.getRowNum() == 0) {
        Iterator<Cell> cellIterator = IOExcel.row.cellIterator();
        while (cellIterator.hasNext()) {
          IOExcel.cell = cellIterator.next();
          data.add(IOExcel.impCellValue().toString());
        }
        Object[] map = data.toArray();
        list.put("column", map);
      } else {

        if (IOExcel.row.getRowNum() == 1) {
          Iterator<Cell> cellIterator = IOExcel.row.cellIterator();
          while (cellIterator.hasNext()) {
            IOExcel.cell = cellIterator.next();
            data.add(IOExcel.impCellValue(true).toString());
          }
          Object[] map = data.toArray();
          list.put("field", map);
        }

        Iterator<Cell> cellIterator = IOExcel.row.cellIterator();
        while (cellIterator.hasNext()) {
          IOExcel.cell = cellIterator.next();
          data.add(IOExcel.impCellValue().toString());
        }
        Object[] map = data.toArray();
        list.put("data", map);
      }
    }

    IOExcel.listData = list;
  }

  private static Object impCellValue() {
    return IOExcel.impCellValue(false);
  }

  private static Object impCellValue(boolean type) {
    Object result = "";
    switch (IOExcel.cell.getCellType()) {
      case BOOLEAN:
        result = IOExcel.cell.getBooleanCellValue();
        if (type) {
          result = EnumDataType.toString(ENUM_DataType.BOOLEAN);
        }
        break;
      case STRING:
        result = IOExcel.cell.getStringCellValue();
        if (type) {
          result = EnumDataType.toString(ENUM_DataType.STRING);
        }
        break;
      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          result = IOExcel.cell.getDateCellValue();
          if (type) {
            result = EnumDataType.toString(ENUM_DataType.DATETIME);
          }
        } else {
          result = cell.getNumericCellValue();
          if (type) {
            result = EnumDataType.toString(ENUM_DataType.CURRENCY);
          }
        }
        break;
      case FORMULA:
        result = IOExcel.cell.getCellFormula();
        if (type) {
          result = EnumDataType.toString(ENUM_DataType.TEXT);
        }
        break;
      case BLANK:
        result = IOExcel.cell.getStringCellValue();
        if (type) {
          result = EnumDataType.toString(ENUM_DataType.STRING);
        }
        break;
      default:
        result = IOExcel.cell.getStringCellValue();
        if (type) {
          result = EnumDataType.toString(ENUM_DataType.TEXT);
        }
    }
    return result;
  }

  public static void exportUtils(Workbook wb, String name) {
    IOExcel.start();
    IOExcel.wb = wb;
    DataFormat format = wb.createDataFormat();
    IOExcel.sheet = wb.createSheet(name);
    IOExcel.format = wb.createDataFormat();
    IOExcel.row = IOExcel.sheet.createRow((short) IOExcel.idx);
    IOExcel.createHelper = wb.getCreationHelper();
    IOExcel.row = IOExcel.sheet.createRow((short) 0);

    IOExcel.font_bold = IOExcel.wb.createFont();
    IOExcel.font_bold.setFontHeightInPoints((short) 11);
    IOExcel.font_bold.setBold(true);

    IOExcel.font_normal = IOExcel.wb.createFont();
    IOExcel.font_normal.setFontHeightInPoints((short) 11);
    IOExcel.font_bold.setBold(false);

    IOExcel.style_left = IOExcel.wb.createCellStyle();
    IOExcel.style_left.setAlignment(HorizontalAlignment.LEFT);

    IOExcel.style_center = IOExcel.wb.createCellStyle();
    IOExcel.style_center.setAlignment(HorizontalAlignment.CENTER);

    IOExcel.style_right = IOExcel.wb.createCellStyle();
    IOExcel.style_right.setAlignment(HorizontalAlignment.RIGHT);

    IOExcel.style_datetime = IOExcel.wb.createCellStyle();
    IOExcel.style_datetime.setDataFormat(IOExcel.createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
    IOExcel.style_datetime.setAlignment(HorizontalAlignment.CENTER);
  }

  public static File exportExcel(String name) throws FileNotFoundException, IOException {
    FileOutputStream out = new FileOutputStream(name);
    IOExcel.wb.write(out);
    return new File(name);
  }

  public static byte[] exportExcel() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      IOExcel.wb.write(baos);
      baos.close();
      IOExcel.wb.close();
      return baos.toByteArray();
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }
  }

  public static void expCellValues(String values, ENUM_DataType cell, int row) throws ParseException {
    IOExcel.isBold = false;
    IOExcel.expCellCols(values, cell, row, 0);
  }

  public static void expCellValues(String values, ENUM_DataType cell, int row, int col) throws ParseException {
    IOExcel.isBold = false;
    IOExcel.expCellCols(values, cell, row, col);
  }

  public static void expCellValues(String values, ENUM_DataType cell, int row, int col, boolean isBold) throws ParseException {
    IOExcel.isBold = isBold;
    IOExcel.expCellCols(values, cell, row, col);
  }

  private static void expCellCols(String values, ENUM_DataType type, int row, int col) throws ParseException {
    if (IOExcel.idx != row) {
      IOExcel.idx = row;
      IOExcel.row = IOExcel.sheet.createRow(row);
    }
    IOExcel.cols = col;
    IOExcel.cell = IOExcel.row.createCell(Globalize.getCell(ExcelColumnName.getColumnName(IOExcel.cols)));
    if (values == null) {
      values = "";
      type = ENUM_DataType.STRING;
    }

    switch (type) {
      case STRING:
        IOExcel.styleFormatCell(0, 0);
        IOExcel.cell.setCellStyle(IOExcel.style);
        IOExcel.cell.setCellValue(values);
        break;
      case NUMERIC:
        IOExcel.styleFormatCell(2, 0);
        IOExcel.style.setDataFormat(IOExcel.format.getFormat("0"));
        IOExcel.cell.setCellStyle(IOExcel.style);
        IOExcel.cell.setCellValue(Double.parseDouble(values));
        break;
      case CURRENCY:
        IOExcel.styleFormatCell(2, 0);
        IOExcel.style.setDataFormat(IOExcel.format.getFormat("#.##0,00_);(#.##0,00)"));
        IOExcel.cell.setCellStyle(IOExcel.style);
        IOExcel.cell.setCellValue(Double.parseDouble(values));
        break;
      case DOUBLE:
        IOExcel.styleFormatCell(2, 0);
        IOExcel.style.setDataFormat(IOExcel.format.getFormat("#.##0,0000_);(#.##0,0000)"));
        IOExcel.cell.setCellStyle(IOExcel.style);
        IOExcel.cell.setCellValue(Double.parseDouble(values));
        break;
      case DATETIME:
        IOExcel.styleFormatCell(3, 0);
        IOExcel.style.setDataFormat(IOExcel.format.getFormat("dd/mm/yyyy"));
        IOExcel.cell.setCellStyle(IOExcel.style);
        Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(values);
        IOExcel.cell.setCellValue(dt);
        break;
      case BOOLEAN:
        IOExcel.styleFormatCell(1, 0);
        IOExcel.cell.setCellStyle(IOExcel.style);
        IOExcel.cell.setCellValue(values);
        break;
      default:
        IOExcel.styleFormatCell(0, 0);
        IOExcel.cell.setCellStyle(IOExcel.style);
        IOExcel.cell.setCellValue(values);
    }
  }

  private static void styleFormatCell(int align, int border) {
    if (IOExcel.isBold) {
      IOExcel.font = IOExcel.font_bold;
    } else {
      IOExcel.font = IOExcel.font_normal;
    }
    switch (align) {
      case 1:
        IOExcel.style_center.setFont(IOExcel.font);
        IOExcel.style = IOExcel.style_center;
        break;
      case 2:
        IOExcel.style_right.setFont(IOExcel.font);
        IOExcel.style = IOExcel.style_right;
        break;
      case 3:
        IOExcel.style_datetime.setFont(IOExcel.font);
        IOExcel.style = IOExcel.style_datetime;
        break;
      default:
        IOExcel.style_left.setFont(IOExcel.font);
        IOExcel.style = IOExcel.style_left;
        break;
    }
//    IOExcel.style.setWrapText(true);
  }
}
