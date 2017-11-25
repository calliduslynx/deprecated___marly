package de.mabe.marly.util;

import java.io.IOException;
import java.net.ServerSocket;

public class PortUtil {
  /**
   * @return a free porz between portToStart and portToEnd
   * @throws IllegalStateException if no port in range is available
   */
  public static int determineNextFreePort(int portToStart, int portToEnd) {
    for (int port = portToStart; port <= portToEnd; port++) {
      try (ServerSocket ignored = new ServerSocket(port)) {
        return port;
      } catch (IOException ignored) {
      }
    }
    throw new IllegalStateException("No available port between " + portToStart + " and " + portToEnd);
  }
}
