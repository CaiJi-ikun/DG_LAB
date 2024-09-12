package buzz.kbpf.dg_lab.client.entity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class NetworkAdapter {
    private final Map<String, String> networkMap = new HashMap<>();

    public NetworkAdapter() {
        GetAllINC();
    }

    public Map<String, String> getNetworkMap() {
        return networkMap;
    }

    public String NICGetaddress(String NIC) {
        return networkMap.get(NIC);
    }

    public void GetAllINC() {
        try {
            // 获取本机所有的网络接口
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                // 过滤掉不活动的网卡和回环地址
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                // 获取网卡上的所有IP地址
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();

                    // 只存储IPv4地址
                    if (inetAddress instanceof java.net.Inet4Address) {
                        // 将网卡名和IPv4地址存储到Map中
                        networkMap.put(networkInterface.getDisplayName(), inetAddress.getHostAddress());
                    }
                }
            }



        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
