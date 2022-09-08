package com.pentagon.warungkita.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class PoiUtils {

    public static String cellValue(Cell cell) {
        if (cell.getCellType() == CellType.BOOLEAN) return String.valueOf(cell.getBooleanCellValue());
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf(cell.getNumericCellValue());
        if (cell.getCellType() == CellType.STRING) return String.valueOf(cell.getStringCellValue());

        return "";
    }
}
