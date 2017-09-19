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
 * ��½������ ��Ҫ�����½
 * @author QinMX
 *
 */
public class LoginServer implements Runnable {
	private Socket socket = null;
	
	public LoginServer(Socket socket) {
		this.socket = socket;
	}
	
	//�̷߳���
	public void run() {
		String uid = null;
		//��½����
		InputStream in = null;
		OutputStream out = null;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			//�ȴ��ͻ�����Ϣ
			byte[] bytes = new byte[1024];
			int len = in.read(bytes);
			String json_str = new String(bytes, 0, len);
			JSONObject json = JSONObject.fromObject(json_str);//����
			String username = json.getString("username");
			String password = json.getString("password");
			
			boolean type = false;
			//�ж��ǲ����ֻ�����
			if(username.trim().length() == 11 && username.indexOf("@") <= -1) {
				try {
					//�ж��ֻ������ǲ��Ǵ�����
					Long.parseLong(username);//�����������ת���׳��쳣
					type = true;
				} catch(NumberFormatException e) {
					out.write("{\"state\":4,\"msg\":\"δ֪����\"}".getBytes());
					out.flush();
					return;
				}
			} else {//���ֻ�����
				type = false;
			}
			
			try {
				
				if(type == true) {
					uid = new UserService().loginForPhone(username, password);
					//�Ǽǵ�¼��Ϣ
					UserOnlineList.getUserOnlineList().regOnline(uid, socket, null, username);
				} else {
					uid = new UserService().loginForEmail(username, password);
					//�Ǽǵ�¼��Ϣ
					UserOnlineList.getUserOnlineList().regOnline(uid, socket, username, null);
				}
				
				out.write("{\"state\":0,\"msg\":\"��½�ɹ���\"}".getBytes());
				out.flush();
				
				while(true) {
					bytes = new byte[1024];
					len = in.read(bytes);
					String command = new String(bytes, 0, len);
					if(command.equals("U0001")) {//���º����б�
						
						Vector<com.myQQ_server.server.db.UserInfo> userinfos = new UserService().getFriendsList(uid);
						out.write(JSONArray.fromObject(userinfos).toString().getBytes());//JSONArray
						out.flush();
						
					} else if (command.equals("U0002")) {//���º�������
						out.write(1);
						out.flush();
						//���ַ�ʽ��������ѹ������������ӷ��������ݿ�ѹ�������и��ô����������ӣ�
						//��ú��ѵ��б���
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
						
					} else if (command.equals("U0003")) {//���¸�������
						
						UserInfo2 userinfo2 = new UserService().getUserInfo(uid);
						out.write(JSONObject.fromObject(userinfo2).toString().getBytes());
						out.flush();
						
					} else if (command.equals("E0001")) {//�޸ĸ�������
						
					} else if (command.equals("EXIT")) {//�˳��û���¼
						UserOnlineList.getUserOnlineList().logout(uid);
						return;
					}
				}
				
				
			} catch (UsernameNotFoundException e) {
				out.write("{\"state\":2,\"msg\":\"�˻�������\"}".getBytes());
				out.flush();
				return;
			} catch (PasswordException e) {
				out.write("{\"state\":1,\"msg\":\"�������\"}".getBytes());
				out.flush();
				return;
			} catch (StateException e) {
				out.write("{\"state\":3,\"msg\":\"�˻�������\"}".getBytes());
				out.flush();
				return;
			} catch (SQLException e) {
				out.write("{\"state\":4,\"msg\":\"δ֪����\"}".getBytes());
				out.flush();
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//����������ӹر�
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
		//�̳߳�
		ExecutorService execute = Executors.newFixedThreadPool(1000);
		
		//������TCP 4001�˿� ���ڵ�½ҵ��
		ServerSocket server = new ServerSocket(4001); //�˴���Ҫ�׳�IO�쳣
		
		while(true) {
			Socket socket = server.accept();//�ͻ��˶����Socket���������˶������ServerSocket
			socket.setSoTimeout(10000);
			execute.execute(new LoginServer(socket)); //�̳߳�
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
