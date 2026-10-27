package com.equiel1703;

import java.net.NetworkInterface;
import java.util.List;

import com.equiel1703.net.NetUtils;

public class PONTE {

    public static void main(String[] args) {
        List<NetworkInterface> list = NetUtils.getNetworkInterfaces();

        for (NetworkInterface n : list) {
            System.out.println(NetUtils.getFormattedInfo(n));
        }
    }
}