package com.inkombizz.conf;

public class ResponsBody {
  private Object status = 200;
  private Object message = "";
  private Object data = "";

  public ResponsBody(Object data) {
    this.data = data;
  }

  public ResponsBody(Object data, Object message) {
    this.data = data;
    this.message=message;
  }

  public ResponsBody(Object data, Object message, Object status) {
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public Object getStatus() {
    return status;
  }

  public Object getMessage() {
    return message;
  }

  public Object getData() {
    return data;
  }
  
}
