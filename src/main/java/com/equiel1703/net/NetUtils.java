package com.equiel1703.net;

import java.util.Collections;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.net.NetworkInterface;
import java.net.SocketException;

public class NetUtils {
  public static List<NetworkInterface> getNetworkInterfaces() {
    List<NetworkInterface> list = new LinkedList<>();

    try {
      NetworkInterface
          .networkInterfaces()
          .filter((netInt) -> {
            try {
              return netInt.isUp();
            } catch (SocketException e) {
              return false;
            }
          })
          .forEach((netInt) -> list.add(netInt));

      return list;
    } catch (Exception e) {
      // In case of problems, return an empty list
      return Collections.emptyList();
    }
  }

  public static String getFormattedInfo(NetworkInterface netInt) {
    StringBuilder strBuilder = new StringBuilder();
    Formatter formatter = new Formatter(strBuilder);

    boolean isUp, isLoopback;

    try {
      isUp = netInt.isUp();
      isLoopback = netInt.isLoopback();
    } catch (SocketException s) {
      isUp = false;
      isLoopback = false;
    }

    formatter.format("Interface: %s (%s)%n", netInt.getName(), netInt.getDisplayName());
    formatter.format("- Index: %d%n", netInt.getIndex());
    formatter.format("- Up: %s%n", isUp);
    formatter.format("- Loopback: %s%n", isLoopback);
    formatter.format("- Virtual: %s%n", netInt.isVirtual());
    formatter.format("- MAC address: %s%n", getFormattedMacAddress(netInt));
    formatter.format("- Addresses: %s%n", Collections.list(netInt.getInetAddresses()));
    formatter.close();

    return strBuilder.toString();
  }

  public static String getFormattedMacAddress(NetworkInterface netInt) {
    try {
      byte[] address = netInt.getHardwareAddress();
      if (address == null) {
        return "unavailable";
      }
      
      StringBuilder str = new StringBuilder();
      Formatter fstr = new Formatter(str);

      for (int i = 0; i < address.length; i++) {
        fstr.format("%X", address[i]);
        if (i < address.length - 1){
          str.append(":");
        }
      }
      fstr.close();
      
      return str.toString();
    } catch (SocketException e) {
      return "unavailable";
    }
  }
}
