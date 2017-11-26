package de.mabe.marly;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InfoFactory {
  /**
   * creates a nice json static info
   */
  public String getInfoJson(boolean active, int size, Date lastUpdate) {
    if (!active)
      return "{ \"active\" : false }";
    else {
      String formattedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(lastUpdate);
      return "{ \"active\" : true, \"mappings\": " + size + ", \"lastupdate\" : \"" + formattedDate + "\" }";
    }
  }
}
