package com.myQQ_server.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.myQQ_server.server.db.UserService;
import com.myQQ_server.server.db.UsernameException;

import net.sf.json.JSONObject;
/**
 * 注册服务器
 * 1.注册申请
 * 2.验证码申请
 * @author QinMX
 *
 */
public class RegServer implements Runnable {
	
	private Socket socket;
	public RegServer(Socket socket) {
		this.socket = socket;
	}
	
	//所有的验证码都存储在这里
	private static HashMap<String, String> hashMap_code = new HashMap<String, String>();
	
	public void run() {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = socket.getInputStream();
			output = socket.getOutputStream();
			
			//等待客户端发送消息过来
			byte[] bytes = new byte[1024];
			int len = input.read(bytes);
			String str = new String(bytes, 0, len);
			JSONObject json = JSONObject.fromObject(str);
			String type = json.getString("type");
			if (type.equals("code")) {//验证码的请求
				String username = json.getString("username");
				//6位随机验证码
				Random random = new Random();
				StringBuffer code = new StringBuffer();
				for (int i = 0; i < 6; i++) {
					//code.append(random.nextInt(10));
					code.append(0);//这里暂时使用默认验证码00000
				}
				
				if(username.trim().length() == 11) {
					try {
						Long.parseLong(type);
						hashMap_code.put(username, code.toString());
						//SendCode.send(username, code.toString());
						output.write("{\"state\":0,\"msg\":\"验证码发送成功！\"}".getBytes());
						output.flush();
					} catch (Exception e) {
						output.write("{\"state\":1,\"msg\":\"验证码发送失败！\"}".getBytes());
						output.flush();
					}
				} else {
					if(username.indexOf("@") >= 0) {
						hashMap_code.put(username, code.toString());
						output.write("{\"state\":0,\"msg\":\"验证码发送成功！\"}".getBytes());
						//SendCode.sendEmail(username, code.toString());
						//暂时使用默认验证码
						
						output.flush();
					} else {
						output.write("{\"state\":1,\"msg\":\"验证码发送失败！\"}".getBytes());
						output.flush();
					}
				}
				
			} else if (type.equals("reg")) {//注册的请求
				String username = json.getString("username");
				String password = json.getString("password");
				String code = json.getString("code");
				String code1 = hashMap_code.get(username);
				if (code1 != null) {
					hashMap_code.remove(username);//一定要删除，无论是否正确
				}
				if (code1.equals(code)) {//询问验证码是否正确
					try {
						new UserService().regUser(username, password);
					} catch (UsernameException e) {
						output.write("{\"state\":1,\"msg\":\"用户名已经存在！\"}".getBytes());
						output.flush();
						return;
					} catch (SQLException e) {
						output.write("{\"state\":3,\"msg\":\"未知错误！\"}".getBytes());
						output.flush();
						return;
					}
					output.write("{\"state\":0,\"msg\":\"注册成功，可以登录了！\"}".getBytes());
					output.flush();
				} else {
					output.write("{\"state\":0,\"msg\":\"验证码错误，请重新获得！\"}".getBytes());
					output.flush();
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
				output.close();
			} catch (Exception e2) {
			}
		}
		
	}
	
	public static void openServer() throws IOException {
		ExecutorService service = Executors.newFixedThreadPool(1000);
		ServerSocket server = new ServerSocket(4002);
		while (true) {
			Socket socket = server.accept();
			service.execute(new RegServer(socket));
		}
	}

}
