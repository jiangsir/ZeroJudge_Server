package tw.zerojudge.Server.Object;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TreeSet;

public class IpAddress implements Comparable<IpAddress>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4512555348283990017L;
	private InetAddress ip = null;
	private int cidr = 32;

	public static enum CIDR {
		ALL, // 全部
		A, // A
		B, // B
		C, // C
		ONE, // 1 個 ip
	}

	/**
	 * 預設取得 loopback address
	 */
	public IpAddress() {
		this.ip = InetAddress.getLoopbackAddress();
	}

	/**
	 * 接受 192.168.1.1 or 192.168.1.1/24 兩種格式。
	 * 
	 * @param ip
	 */
	public IpAddress(String ip) {
		if (ip == null || "null".equals(ip.trim())) {
			return;
		}
		if (ip.contains("/")) {
			String[] array = ip.split("/");
			try {
				this.ip = InetAddress.getByName(array[0]);
				this.cidr = Integer.parseInt(array[1]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			try {
				this.ip = InetAddress.getByName(ip.trim());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param ip
	 * @param cidr
	 *            代表 mask 有多少個 1, cidr==0 代表所有 ip
	 */
	public IpAddress(String ip, int cidr) {
		try {
			this.ip = InetAddress.getByName(ip.trim());
			this.cidr = cidr;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param ip
	 * @param cidr
	 *            代表 mask 有多少個 1, cidr==0 代表所有 ip
	 */
	public IpAddress(String ip, CIDR cidr) {
		try {
			this.ip = InetAddress.getByName(ip.trim());
			this.cidr = cidr.ordinal() * 8;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public int getCidr() {
		return cidr;
	}

	public void setCidr(int cidr) {
		this.cidr = cidr;
	}

	public boolean getIsIpv4() {
		return this.getIp() instanceof Inet4Address;
	}

	public boolean getIsIpv6() {
		return this.getIp() instanceof Inet6Address;
	}

	private int toInt() {
		byte[] address = this.getIp().getAddress();
		int net = 0;
		for (byte addres : address) {
			net <<= 8;
			net |= addres & 0xFF;
		}
		return net;
	}

	private int getMask() {
		return 0x80000000 >> cidr - 1;
	}

	/**
	 * 判斷這個 ip 是否是 ipAddress 的子網域
	 * 
	 * @param ipgroup
	 * @return
	 */
	public boolean getIsSubnetOf(IpAddress ipgroup) {
		if (ipgroup.cidr == 0) {
			return true;
		}
		return (this.toInt() & ipgroup.getMask()) == (ipgroup.toInt() & ipgroup.getMask());
	}

	/**
	 * 判斷這個 IP 是否屬於 一個 network 如：192.168.*.*, 203.1.2.3
	 * 
	 * @param iprules
	 * @return
	 */
	public boolean getIsSubnetOf(TreeSet<IpAddress> iprules) {
		if (iprules == null || iprules.size() == 0 || this.isLoopbackAddress()) {
			return true;
		}
		for (IpAddress ip : iprules) {
			if (this.getIsSubnetOf(ip)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		if (ip == null) {
			return "null";
		}
		return cidr == 32 ? ip.getHostAddress() : ip.getHostAddress() + "/" + cidr;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null) {
			return false;
		}
		return this.toString().equals(arg0.toString());
	}

	@Override
	public int compareTo(IpAddress o) {
		return this.getIp().toString().compareTo(o.getIp().toString());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	public boolean isLoopbackAddress() {
		return ip.isLoopbackAddress();
	}

}
