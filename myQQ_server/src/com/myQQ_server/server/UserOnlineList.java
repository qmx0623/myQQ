package com.myQQ_server.server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * ����ģʽ �����û��б�
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
	
	//���ǰ����е������˻� ȫ���Ǽ��ڼ�����
	/**
	 * String���û��ı��
	 */
	private HashMap<String, UserInfo> hashMap = new HashMap<String, UserInfo>();
	
	//ע�������û�
	public void regOnline(String uid, Socket socket, String email, String phone) throws IOException {//����finally�׳����쳣
		//�ж������ͻ����Ƿ��½һ�����û��������һ������ǿ��Ū��ȥ
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
		hashMap.put(uid, userInfo);//�Ǽ�����
	}
	
	/**
	 * ���¿ͻ��˵�UDP��Ϣ
	 * @param uid     �û����
	 * @param ip      udp IP��ַ
	 * @param port    udp�˿�
	 * @throws NullPointerException
	 */
	public void updateOnlineUDP(String uid, String ip, int port) throws NullPointerException{
		UserInfo userInfo = hashMap.get(uid);
		userInfo.setUdpip(ip);
		userInfo.setUdpport(port);
	}
	
	//�ж��û��Ƿ�����
	public boolean isUserOnline(String uid) {
		return hashMap.containsKey(uid);
	}
	
	//��������û���Ϣ
	public UserInfo getOnlineUserInfo(String uid) {
		return hashMap.get(uid);
	}
	
	//������е�������Ϣ
	public Set<String> getUserInfo() {
		return hashMap.keySet();
	}
	
	//����
	public void logout(String uid) {
		hashMap.remove(uid);
	}

}
