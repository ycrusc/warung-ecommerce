package com.pentagon.warungkita.service;

import net.sf.jasperreports.engine.JasperPrint;

public interface ReportService {
    JasperPrint generateJasperPrintProductList() throws Exception;
}
