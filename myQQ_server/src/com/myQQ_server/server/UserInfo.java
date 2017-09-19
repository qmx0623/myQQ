package com.myQQ_server.server;

import java.net.Socket;

/**
 * 用于通讯的用户信息，主要是给UserOnlineList用
 * @author QinMX
 *
 */
public class UserInfo {
	private String uid;
	private String phone;
	private String email;
	private Socket socket;
	private String udpip;
	private int udpport;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getUdpip() {
		return udpip;
	}
	public void setUdpip(String udpip) {
		this.udpip = udpip;
	}
	public int getUdpport() {
		return udpport;
	}
	public void setUdpport(int udpport) {
		this.udpport = udpport;
	}

}
