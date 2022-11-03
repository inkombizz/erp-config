package com.inkombizz.conf;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ResponsIO {

  public Object status = 200;
  public Object message = "";
  public List<String> failed = new ArrayList<>();
  public List<String> success = new ArrayList<>();

}
