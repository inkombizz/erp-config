/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inkombizz.conf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author imamsolikhin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JasperReport {

  private String reportName;
  private List<?> responseEntityList;

  public String getReportName() {
    return reportName;
  }

  public void setReportName(String reportName) {
    this.reportName = reportName;
  }

  public List<?> getResponseEntityList() {
    return responseEntityList;
  }

  public void setResponseEntityList(List<?> responseEntityList) {
    this.responseEntityList = responseEntityList;
  }

}
