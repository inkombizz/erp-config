/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inkombizz.report;

import com.inkombizz.base.Session;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.persistence.MappedSuperclass;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author imamsolikhin
 */
@MappedSuperclass
public abstract class ReportBase extends Session{

  /**
   * write byte array to response to download/open report
   *
   * @param jasperReport CusJasperReportDef
   * @param forceDownload whether force browser to download, if false browser
   * can open like image/pdf
   * @return
   * @throws IOException
   */
  public ResponseEntity<byte[]> respondReportOutput(CustomJasperReport jasperReport, boolean forceDownload) throws IOException {
    if (jasperReport == null || jasperReport.getContent() == null) {
      throw new FileNotFoundException("jasper Report Not found");
    } else {

      String outputFileName = (jasperReport.getOutputFilename()) + "." + jasperReport.getReportFormat().getExtension();
      String contentDisposition = forceDownload == true ? "attachment;filename=\"" + outputFileName + "\"" : "filename=\"" + outputFileName + "\"";
      return ResponseEntity
              .ok()
              .header("Access-Control-Allow-Origin", "*")
              .header("Content-Type", jasperReport.getReportFormat().getMimeType() + ";charset=UTF-8")
              .header("Content-Disposition", contentDisposition)
              .body(jasperReport.getContent());

    }

  }

  /**
   * write byte array to response to download/open report
   *
   * @param jasperReport CusJasperReportDef
   * @param forceDownload whether force browser to download, if false browser
   * can open like image/pdf
   * @return
   * @throws IOException
   */
  public ResponseEntity<byte[]> respondReportOutputWithoutHeader(CustomJasperReport jasperReport, boolean forceDownload) throws IOException {
    if (jasperReport == null || jasperReport.getContent() == null) {
      throw new FileNotFoundException("jasper Report Not found");
    } else {
      String outputFileName = (jasperReport.getOutputFilename()) + "." + jasperReport.getReportFormat().getExtension();
      String contentDisposition = forceDownload == true ? "attachment;filename=\"" + outputFileName + "\"" : "filename=\"" + outputFileName + "\"";
      return ResponseEntity
              .ok()
              .header("Content-Type", jasperReport.getReportFormat().getMimeType() + ";charset=UTF-8")
              .header("Content-Disposition", contentDisposition)
              .body(jasperReport.getContent());

    }
  }

}
