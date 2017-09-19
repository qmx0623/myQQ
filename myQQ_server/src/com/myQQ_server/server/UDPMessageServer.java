package com.myQQ_server.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.myQQ_server.server.UserOnlineList;

import net.sf.json.JSONObject;
/**
 * UDP信息中转服务器
 * @author QinMX
 *
 */
public class UDPMessageServer implements Runnable {
	private DatagramPacket packet = null;
	
	public UDPMessageServer(DatagramPacket packet) {
		this.packet = packet;
	}
	
	//业务处理
	public void run() {
		try {
			String jsonStr = new String(packet.getData(), 0, packet.getLength());
			JSONObject json = JSONObject.fromObject(jsonStr);
			
			//处理心跳包
			if (json.getString("type").equals("reg")) {
				String myUID = json.getString("myUID");
				//保持持续联系
				UserOnlineList.getUserOnlineList().updateOnlineUDP(myUID, packet.getAddress().getHostAddress(), packet.getPort());//第二个参数是ip
				System.out.println("有注册信息发来:" + jsonStr);
				
			//处理信息转发//处理消息确认
			} else if (json.getString("type").equals("msg") || json.getString("type").equals("response")) {
				String myUID = json.getString("myUID");
				String toUID = json.getString("toUID");
				UserOnlineList.getUserOnlineList().updateOnlineUDP(myUID, packet.getAddress().getHostAddress(), packet.getPort());
				
				UserInfo toUserinfo = UserOnlineList.getUserOnlineList().getOnlineUserInfo(toUID);
				
				//准备转发到客户端的数据包
				DatagramPacket datagramPacket = new DatagramPacket(
						packet.getData(), packet.getLength(), InetAddress.getByName(toUserinfo.getUdpip()), toUserinfo.getUdpport());
				
				//发出数据包
				datagramSocket.send(datagramPacket);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private static DatagramSocket datagramSocket = null;
	
	//启动服务器
	public static void openServer() throws Exception {
		datagramSocket = new DatagramSocket(4003);
		//制作线程池
		ExecutorService execute = Executors.newFixedThreadPool(1000);
		
		while (true) {
			try {
				//等待客户端的数据
				byte[] b = new byte[1024*10];
				DatagramPacket datagramPacket = new DatagramPacket(b, b.length);
				datagramSocket.receive(datagramPacket);
				
				//数据一旦收到立马抓出一个线程处理
				execute.execute(new UDPMessageServer(datagramPacket));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
