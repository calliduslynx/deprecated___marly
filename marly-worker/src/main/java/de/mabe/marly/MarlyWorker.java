package de.mabe.marly;

import static de.mabe.marly.util.PortUtil.determineNextFreePort;
import de.mabe.marly.util.ThreadUtil;
import spark.Response;
import static spark.Spark.get;
import static spark.Spark.port;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class MarlyWorker {
  static Logger log = Log.getLogger(MarlyWorker.class);

  public static void main(String[] args) {
    new MarlyWorker().start();
  }

  // ********************************************************************
  // ***** Configuration
  // ********************************************************************
  private static final int MIN_PORT = 9010;
  private static final int MAX_PORT = 9020;

  private static final int MAPPING_REFRESH_TIME_IF_ACTIVE = 30_000; // ms
  private static final int MAPPING_REFRESH_TIME_IF_NOT_ACTIVE = 5_000; // ms

  private static final String SERVICE_URL = "http://localhost:8080";
  private static final String REDIRECT_EVENT_URL = "http://localhost:8080/intern/statistic/redirect_event";
  private static final String MAPPING_RESOLVE_URL = "http://localhost:8080/intern/mappings";

  // ********************************************************************
  // ***** Members
  // ********************************************************************
  private final RedirectEventThrower redirectEventThrower = new RedirectEventThrower(REDIRECT_EVENT_URL);
  private final MappingResolver mappingResolver = new MappingResolver(MAPPING_RESOLVE_URL);
  private final InfoFactory infoFactory = new InfoFactory();

  private volatile Map<String, String> redirects = Collections.emptyMap();
  private volatile boolean active = false;
  private volatile Date lastUpdate = null;

  /**
   * start the marly worker service
   */
  private void start() {
    startServer();
    resolveMappings();
  }

  private void startServer() {
    int port = determineNextFreePort(MIN_PORT, MAX_PORT);
    port(port);
    log = Log.getLogger("Marly-Worker:" + port);

    get("/", (req, res) -> redirect(res, SERVICE_URL));
    get("/info", (req, res) -> infoFactory.getInfoJson(active, redirects.size(), lastUpdate));
    get("/:tiny", (req, res) -> performMarlyRedirect(res, req.params("tiny")));
  }

  private Object performMarlyRedirect(Response res, String tiny) {
    if (!active) {
      res.status(424);
      return "";
    }

    String url = redirects.get(tiny);

    if (url == null) {
      res.status(404);
    } else {
      log.info("::: " + tiny + " -> " + url);
      redirectEventThrower.throwUrlCalledEvent(tiny);
      res.redirect(url);
    }
    return null;
  }

  private Void redirect(Response res, String url) {
    res.redirect(url);
    return null;
  }

  /**
   * starts an endless loop that resolves all mappings from marly-core
   */
  private void resolveMappings() {
    new Thread(() -> {
      while (true) {
        Map<String, String> newMappings = mappingResolver.resolve();
        if (newMappings != null) {
          redirects = newMappings;
          active = true;
          lastUpdate = new Date();
          log.info("updated mappings: " + redirects.size());
        } else {
          log.warn("unable to resolve mappings");
        }

        ThreadUtil.sleep(active ? MAPPING_REFRESH_TIME_IF_ACTIVE : MAPPING_REFRESH_TIME_IF_NOT_ACTIVE);
      }
    }).start();
  }
}