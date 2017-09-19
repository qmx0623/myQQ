package com.myQQ.view.util;

import java.net.DatagramSocket;
import java.util.Hashtable;

import com.myQQ.view.FriendsListJPanel;
import com.myQQ.view.TalkFrame;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Config {
	//��������ַ
	public static final String IP = "127.0.0.1";
	//��¼�˿�
	public static final int LOGIN_PORT = 4001;
	//ע��˿�
	public static final int REG_PORT = 4002;
	
	//�û���������Ĵ�   ���ڶ�������
	public static String username;
	public static String password;
	
	public static FriendsListJPanel friendsListJPanel;
	
	//������Ϣ�б� JSON��ʽ
	public static volatile String friends_json_data = "[]";
	
	//������Ϣ�б�
	public static String friends_list_data = "";
	
	//����Ϊ�����б�
	public static void read_friends_json_data(String friends_json_data) {
		Config.friends_json_data = friends_json_data;
		JSONArray json = JSONArray.fromObject(friends_json_data);
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < json.size(); i++) {
			JSONObject jsonobj = (JSONObject) json.get(i);
			stringBuffer.append(jsonobj.getString("uid"));
			stringBuffer.append(",");
		}
		Config.friends_list_data = stringBuffer.toString();
	}
	
	public static String own_json_data = "{}";
	
	public static String friends_online = "";
	
	public static DatagramSocket datagramSocket_client = null;
	
	//���촰�ڵǼ�
	public static Hashtable<String, TalkFrame> talkTable = new Hashtable<String, TalkFrame>();
	
	//��ʾ���촰��
	public static void showTalkFrame(String uid, String netName, String img, String info) {
		if (talkTable.get(uid) == null) {
			TalkFrame talk = new TalkFrame(uid, netName, img, info);
			talk.setAlwaysOnTop(true);
			talkTable.put(uid, talk);
		} else {
			talkTable.get(uid).setAlwaysOnTop(true);
			talkTable.get(uid).setVisible(true);
		}
	}
	
	public static void closeTalkFrame(String uid) {
		talkTable.remove(uid);
	}

}
