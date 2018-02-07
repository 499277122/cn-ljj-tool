package tool;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public final class IPUtil {

	private IPUtil() {
	}

	public static String getRequestIP(HttpServletRequest request) {
		String ip = request.getHeader("X-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null && !"".equals(ip) && ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
			return ip;
		}
		return "";
	}

	public static List<String> getLocalIP() throws Exception {
		List<String> ipList = new ArrayList<String>();
		Enumeration<NetworkInterface> iEnum = NetworkInterface.getNetworkInterfaces();

		while (iEnum.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) iEnum.nextElement();
			Enumeration<InetAddress> aEnum = netInterface.getInetAddresses();
			while (aEnum.hasMoreElements()) {
				InetAddress ip = (InetAddress) aEnum.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					ipList.add(ip.getHostAddress());
				}
			}
		}
		return ipList;
	}

	/**
	 * 获取来访者的浏览器版本
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestBrowserInfo(HttpServletRequest request) {
		String header = request.getHeader("user-agent");
		if (StringUtils.isNotBlank(header)) {
			return header;
		}
		return null;
	}

}
