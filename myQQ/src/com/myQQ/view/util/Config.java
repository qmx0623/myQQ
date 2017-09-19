package com.myQQ.view.util;

import java.net.DatagramSocket;
import java.util.Hashtable;

import com.myQQ.view.FriendsListJPanel;
import com.myQQ.view.TalkFrame;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Config {
	//服务器地址
	public static final String IP = "127.0.0.1";
	//登录端口
	public static final int LOGIN_PORT = 4001;
	//注册端口
	public static final int REG_PORT = 4002;
	
	//用户名和密码寄存   用于断线自连
	public static String username;
	public static String password;
	
	public static FriendsListJPanel friendsListJPanel;
	
	//好友信息列表 JSON格式
	public static volatile String friends_json_data = "[]";
	
	//好友信息列表
	public static String friends_list_data = "";
	
	//解析为好友列表
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
	
	//聊天窗口登记
	public static Hashtable<String, TalkFrame> talkTable = new Hashtable<String, TalkFrame>();
	
	//显示聊天窗口
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
