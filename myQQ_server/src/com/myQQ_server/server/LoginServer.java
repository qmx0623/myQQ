package com.myQQ_server.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.myQQ_server.server.db.PasswordException;
import com.myQQ_server.server.db.StateException;
import com.myQQ_server.server.db.UserInfo2;
import com.myQQ_server.server.db.UserService;
import com.myQQ_server.server.db.UsernameNotFoundException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 登陆服务器 主要负责登陆
 * @author QinMX
 *
 */
public class LoginServer implements Runnable {
	private Socket socket = null;
	
	public LoginServer(Socket socket) {
		this.socket = socket;
	}
	
	//线程方法
	public void run() {
		String uid = null;
		//登陆操作
		InputStream in = null;
		OutputStream out = null;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			//等待客户端信息
			byte[] bytes = new byte[1024];
			int len = in.read(bytes);
			String json_str = new String(bytes, 0, len);
			JSONObject json = JSONObject.fromObject(json_str);//解析
			String username = json.getString("username");
			String password = json.getString("password");
			
			boolean type = false;
			//判断是不是手机号码
			if(username.trim().length() == 11 && username.indexOf("@") <= -1) {
				try {
					//判断手机号码是不是纯数字
					Long.parseLong(username);//如果不是类型转换抛出异常
					type = true;
				} catch(NumberFormatException e) {
					out.write("{\"state\":4,\"msg\":\"未知错误！\"}".getBytes());
					out.flush();
					return;
				}
			} else {//非手机号码
				type = false;
			}
			
			try {
				
				if(type == true) {
					uid = new UserService().loginForPhone(username, password);
					//登记登录信息
					UserOnlineList.getUserOnlineList().regOnline(uid, socket, null, username);
				} else {
					uid = new UserService().loginForEmail(username, password);
					//登记登录信息
					UserOnlineList.getUserOnlineList().regOnline(uid, socket, username, null);
				}
				
				out.write("{\"state\":0,\"msg\":\"登陆成功！\"}".getBytes());
				out.flush();
				
				while(true) {
					bytes = new byte[1024];
					len = in.read(bytes);
					String command = new String(bytes, 0, len);
					if(command.equals("U0001")) {//更新好友列表
						
						Vector<com.myQQ_server.server.db.UserInfo> userinfos = new UserService().getFriendsList(uid);
						out.write(JSONArray.fromObject(userinfos).toString().getBytes());//JSONArray
						out.flush();
						
					} else if (command.equals("U0002")) {//更新好友在线
						out.write(1);
						out.flush();
						//这种方式增加网络压力，否则会增加服务器数据库压力，还有个好处，保持连接！
						//获得好友的列表编号
						len = in.read(bytes);//448280931,448280932,448280933....
						String str = new String(bytes, 0, len);
						String[] ids = str.split(",");
						
						StringBuffer stringBuffer = new StringBuffer();
						for (String string : ids) {
							if (UserOnlineList.getUserOnlineList().isUserOnline(string)) {
								stringBuffer.append(string);
								stringBuffer.append(",");
							}
						}
						if (stringBuffer.length() == 0) {
							out.write("notFound".getBytes());
							out.flush();
						} else {
							out.write(stringBuffer.toString().getBytes());
							out.flush();
						}
						
					} else if (command.equals("U0003")) {//更新个人资料
						
						UserInfo2 userinfo2 = new UserService().getUserInfo(uid);
						out.write(JSONObject.fromObject(userinfo2).toString().getBytes());
						out.flush();
						
					} else if (command.equals("E0001")) {//修改个人资料
						
					} else if (command.equals("EXIT")) {//退出用户登录
						UserOnlineList.getUserOnlineList().logout(uid);
						return;
					}
				}
				
				
			} catch (UsernameNotFoundException e) {
				out.write("{\"state\":2,\"msg\":\"账户名错误！\"}".getBytes());
				out.flush();
				return;
			} catch (PasswordException e) {
				out.write("{\"state\":1,\"msg\":\"密码错误！\"}".getBytes());
				out.flush();
				return;
			} catch (StateException e) {
				out.write("{\"state\":3,\"msg\":\"账户锁定！\"}".getBytes());
				out.flush();
				return;
			} catch (SQLException e) {
				out.write("{\"state\":4,\"msg\":\"未知错误！\"}".getBytes());
				out.flush();
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//结束后把连接关闭
			try {
				UserOnlineList.getUserOnlineList().logout(uid);
				in.close();
				out.close();
				socket.close();
			} catch (Exception e2) {
			}
		}
	}
	
	public static void openServer() throws Exception {
		//线程池
		ExecutorService execute = Executors.newFixedThreadPool(1000);
		
		//开启了TCP 4001端口 用于登陆业务
		ServerSocket server = new ServerSocket(4001); //此处需要抛出IO异常
		
		while(true) {
			Socket socket = server.accept();//客户端定义的Socket，服务器端定义的是ServerSocket
			socket.setSoTimeout(10000);
			execute.execute(new LoginServer(socket)); //线程池
		}
		
		
	}
	
	public static void main(String[] args) {
		try {
			openServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
