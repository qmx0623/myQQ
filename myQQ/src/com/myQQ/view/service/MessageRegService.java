package com.myQQ.view.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.myQQ.view.util.Config;

import net.sf.json.JSONObject;

public class MessageRegService extends Thread {
	private DatagramSocket client = null;
	
	//ÿ10���� �������ע������һ��
	public void run() {
		String uid = JSONObject.fromObject(Config.own_json_data).getString("uid");
		String jsonStr = "{\"type\":\"reg\", \"myUID\":\""+ uid +"\"}";
		byte[] bytes = jsonStr.getBytes();
		
		while (true) {
			try {
				DatagramPacket datagrampacket = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(Config.IP), 4003);
				//��������Ϣ���͸�������
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
