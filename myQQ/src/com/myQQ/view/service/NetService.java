package com.myQQ.view.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.myQQ.view.util.Config;

import net.sf.json.JSONObject;

/**
 * 通讯服务 与服务器保持连接状态
 * 1.更新好友在线状态 5秒钟一次请求
 * 2.登录验证
 * 3.退出账号
 * @author QinMX
 *
 */
public class NetService implements Runnable {
	private NetService() {
	}
	
	private static NetService netService = new NetService();
	
	//对象
	public static NetService getNetService() {
		return netService;
	}

	//这里准备与服务器保持长时间通讯
	public void run() {
		try {
			//好友信息获得
			output.write("U0001".getBytes());
			output.flush();
			byte[] bytes = new byte[1024 * 10];//10个有点少吧！此处应该有些问题
			int len = input.read(bytes);
			String jsonstr = new String(bytes, 0, len);
			
			//解析好友列表
			Config.read_friends_json_data(jsonstr);
			System.out.println("好友资料"+Config.friends_json_data);
			
			//个人资料获得
			output.write("U0003".getBytes());
			output.flush();
			len = input.read(bytes);
			Config.own_json_data = new String(bytes, 0, len);
			System.out.println("个人资料"+Config.own_json_data);
			
			int i = 0;
			//好友在线的实时更新
			while (run) {
				output.write("U0002".getBytes());
				output.flush();
				input.read();
				output.write(Config.friends_list_data.getBytes());
				output.flush();
				
				len = input.read(bytes);
				String online = new String(bytes, 0, len);
				System.out.println("在线账户"+online);
				
				try {
					if (!Config.friends_online.equals(online)) {
						Config.friends_online = online;
						Config.friendsListJPanel.OnlineFriendsUpdata();
					}
				} catch (Exception e) {
				}
				Config.friends_online = online;
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					
				}
				
				if (i == 1000) {
					i = 1;
				} else if (i == 0){
					GoOn = true;
				} else {
					i++;
				}
			}
		} catch (Exception e) {
			run = false;
		}
		
		
	}
	
	private Socket socket = null;
	private InputStream input = null;
	private OutputStream output = null;
	private Thread thread = null;
	private boolean run = false;  //这里应该用volatile用于停止线程
	private volatile boolean GoOn = false;//这里没有volatile主线程就一直阻塞了
	
	public JSONObject login() throws UnknownHostException, IOException{
		socket = new Socket(Config.IP, Config.LOGIN_PORT);
		input = socket.getInputStream();   //此处应该有超时设置Socket s = new Socket(); s。connect(new InetSocketAddress(host,port), timeout)!!!而s。setSoTimeout(10000)只能用于套接字！！！！！
		output = socket.getOutputStream();
		String json_str = "{\"username\":\"" + Config.username + "\",\"password\":\"" + Config.password + "\"}";
		
		//开始与服务器传递消息
		output.write(json_str.getBytes());
		output.flush();
		
		//等待服务器回执消息
		byte[] bytes = new byte[1024];
		int len = input.read(bytes);
		
		json_str = new String(bytes, 0, len);
		JSONObject json = JSONObject.fromObject(json_str);
		
		//如果是0就是登陆成功！
		if (json.getInt("state") == 0) {
			//开启持续的网络连接服务
			if (thread != null) {
				//询问线程是否还活着
				if (thread.getState() == Thread.State.TERMINATED) {
					run = false;//终止线程运行
					try {
						thread.stop();//强制有风险
					} catch (Exception e) {
					}
				}
			}
			run = true;
			//重新开线程与服务器保持通讯
			thread = new Thread(this);
			thread.start();
			
			//人肉阻塞！！！！
			while (GoOn == false) {//这里出了一个BUG，必须写在if里面
				//System.out.println("11111111111111");
			}
		}
		
		
		//启动UDP服务器
		Config.datagramSocket_client = new DatagramSocket();
		//启动心跳包
		new MessageRegService(Config.datagramSocket_client);//难道UDP共用一个端口？？？？？
		//启动消息服务
		new MessageService(Config.datagramSocket_client);
		
		return json;
		
	}

}
