package com.myQQ_server.server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * 单例模式 在线用户列表
 * 
 * @author QinMX
 *
 */
public class UserOnlineList {
	private UserOnlineList() {
	}
	
	private static UserOnlineList userOnlineList = new UserOnlineList();
	
	public static UserOnlineList getUserOnlineList() {
		return userOnlineList;
	}
	
	//我们把所有的在线账户 全部登记在集合中
	/**
	 * String是用户的编号
	 */
	private HashMap<String, UserInfo> hashMap = new HashMap<String, UserInfo>();
	
	//注册在线用户
	public void regOnline(String uid, Socket socket, String email, String phone) throws IOException {//这是finally抛出的异常
		//判断其他客户端是否登陆一样额用户名，如果一样，就强行弄下去
		if(hashMap.containsValue(uid)) {
			UserInfo userInfo = hashMap.get(uid);
			try {
				userInfo.getSocket().getOutputStream().write(4);
			} catch (Exception e) {
			} finally {
				userInfo.getSocket().close();
			}
		}
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUid(uid);
		userInfo.setEmail(email);
		userInfo.setPhone(phone);
		userInfo.setSocket(socket);
		hashMap.put(uid, userInfo);//登记在线
	}
	
	/**
	 * 更新客户端的UDP信息
	 * @param uid     用户编号
	 * @param ip      udp IP地址
	 * @param port    udp端口
	 * @throws NullPointerException
	 */
	public void updateOnlineUDP(String uid, String ip, int port) throws NullPointerException{
		UserInfo userInfo = hashMap.get(uid);
		userInfo.setUdpip(ip);
		userInfo.setUdpport(port);
	}
	
	//判断用户是否在线
	public boolean isUserOnline(String uid) {
		return hashMap.containsKey(uid);
	}
	
	//获得在线用户信息
	public UserInfo getOnlineUserInfo(String uid) {
		return hashMap.get(uid);
	}
	
	//获得所有的在线信息
	public Set<String> getUserInfo() {
		return hashMap.keySet();
	}
	
	//下线
	public void logout(String uid) {
		hashMap.remove(uid);
	}

}
