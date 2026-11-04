package com.equiel1703.net;

import java.util.Arrays;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

public class NetUtils {
  public static List<NetworkIF> getNetworkInterfaces() {
      List<NetworkIF> outList = new LinkedList<>();

      SystemInfo si = new SystemInfo();
      HardwareAbstractionLayer hal = si.getHardware();

      for (NetworkIF netInt : hal.getNetworkIFs()) {
        final String intName = netInt.getName();

        // Ignore Docker stuff
        if (intName.startsWith("br") || intName.startsWith("docker")) {
          continue;
        }

        outList.add(netInt);
      }

      return outList;
  }

  public static String getFormattedInfo(NetworkIF netInt) {
    StringBuilder strBuilder = new StringBuilder();
    Formatter formatter = new Formatter(strBuilder);

    netInt.updateAttributes();

    formatter.format("Interface: %s (%s)\n", netInt.getName(), netInt.getDisplayName());
    formatter.format("- Index: %d\n", netInt.getIndex());
    formatter.format("- Connector Present? %s\n", netInt.isConnectorPresent());
    formatter.format("- MAC address: %s\n", netInt.getMacaddr());
    formatter.format("- IPV4: %s\n", Arrays.asList(netInt.getIPv4addr())).toString();
    formatter.format("- Status: %s\n", netInt.getIfOperStatus().toString());
    formatter.close();

    return strBuilder.toString();
  }
}
