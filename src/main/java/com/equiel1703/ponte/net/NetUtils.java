package com.equiel1703.ponte.net;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

public class NetUtils {
  public enum InterfaceType {
    WIRELESS,
    ETHERNET,
    OTHER
  }

  public static List<NetworkIF> getNetworkInterfaces() {
    List<NetworkIF> outList = new LinkedList<>();

    SystemInfo si = new SystemInfo();
    HardwareAbstractionLayer hal = si.getHardware();

    for (NetworkIF netInt : hal.getNetworkIFs()) {
      final String intName = netInt.getName();

      // Ignore known Docker stuff
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
    formatter.format("- Mask: %s\n",
        Arrays.stream(netInt.getSubnetMasks())
            .map(sm -> cidrToNetmask(sm))
            .toList()
            .toString());
    formatter.format("- Status: %s\n", netInt.getIfOperStatus().toString());
    formatter.close();

    return strBuilder.toString();
  }

  public static String cidrToNetmask(int prefixLength) {
    if (prefixLength < 0 || prefixLength > 32) {
      throw new IllegalArgumentException("CIDR prefix must be between 0 and 32");
    }

    int mask = prefixLength == 0 ? 0 : 0xFFFFFFFF << (32 - prefixLength);

    return String.format("%d.%d.%d.%d",
        (mask >>> 24) & 0xFF,
        (mask >>> 16) & 0xFF,
        (mask >>> 8) & 0xFF,
        mask & 0xFF);
  }

  public static InterfaceType getInterfaceType(NetworkIF net) {
    String osName = System.getProperty("os.name").toLowerCase();
    boolean isLinux = osName.contains("nix") || osName.contains("nux") || osName.contains("aix");

    /*
     * (Windows, macOS) The NDIS Interface Type.
     * NDIS interface types are registered with the Internet Assigned Numbers
     * Authority (IANA), which publishes a list of interface types periodically.
     * 
     * (Linux) ARP Protocol hardware identifiers defined in
     * include/uapi/linux/if_arp.h
     */
    int ifType = net.getIfType();

    if (isLinux) {
      /*
       * In Linux, a normal Wi-Fi interface reports itself as ARPHRD_ETHER (1) instead
       * of 801 (ARPHRD_IEEE80211), because why not?? THAT'S MAKES TOTAL SENSE!!!
       * 
       * The value 801 is only used by interfaces in monitor mode with no radiotap
       * header, and 803 (ARPHRD_IEEE80211_RADIOTAP) for monitor mode with radiotap.
       * So in practice, 801 will almost NEVER show up for a normal client connection.
       * TORVALDS YOU'RE A LAZY CUNT!!!
       * 
       * We will have to MANUALLY CHECK for the 'phy80211' directory to verify if this
       * interface is wireless or a true cabled ethernet.
       */
      Path phyPath = Path.of("/sys/class/net", net.getName(), "phy80211");

      if (Files.exists(phyPath) || ifType == 801 || ifType == 803) {
        return InterfaceType.WIRELESS;
      } else if (ifType == 1) {
        return InterfaceType.ETHERNET;
      }
    } else {
      /*
       * If not Linux, we must be in Windows or Mac.
       * 
       * According to IANA:
       * - 71 = IF_TYPE_IEEE80211 (wifi)
       * -  6 = IF_TYPE_ETHERNET_CSMACD (Ethernet)
       * 
       * Hope this crap will work here without any weird hackery.
       */
      if (ifType == 71) {
        return InterfaceType.WIRELESS;
      } else if (ifType == 6) {
        return InterfaceType.ETHERNET;
      }
    }

    // If not any of these, we assign as 'generic'
    return InterfaceType.OTHER;
  }
}
