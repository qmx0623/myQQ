package com.myQQ.view.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.myQQ.view.util.Config;

import net.sf.json.JSONObject;

public class MessageRegService extends Thread {
	private DatagramSocket client = null;
	
	//每10秒钟 向服务器注册心跳一下
	public void run() {
		String uid = JSONObject.fromObject(Config.own_json_data).getString("uid");
		String jsonStr = "{\"type\":\"reg\", \"myUID\":\""+ uid +"\"}";
		byte[] bytes = jsonStr.getBytes();
		
		while (true) {
			try {
				DatagramPacket datagrampacket = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(Config.IP), 4003);
				//将更新消息发送给服务器
				client.send(datagrampacket);
				Thread.sleep(9999);
			} catch (Exception e) {
				
			}
		}
	}
	
	public MessageRegService(DatagramSocket client) {
		this.client = client;
		this.start();
	}

}
