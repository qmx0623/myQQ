package com.myQQ.view.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.myQQ.view.util.Config;

import net.sf.json.JSONObject;

/**
 * ���ܷ���������ת��Ϣ
 * @author QinMX
 *
 */
public class MessageService extends Thread {
	private DatagramSocket client = null;
	
	public void run() {
		while (true) {
			try {
				byte[] bytes = new byte[1024*32];
				DatagramPacket datagrampacket = new DatagramPacket(bytes, bytes.length);
				//��������Ϣ���͸�������
				client.receive(datagrampacket);
				
				MessagePool.getMessagePool().addMessage(new String(datagrampacket.getData(), 0, datagrampacket.getData().length));
				
			} catch (Exception e) {
				
			}
		}
	}
	
	public MessageService(DatagramSocket client) {
		this.client = client;
		this.start();
	}

}
