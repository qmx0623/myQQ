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
 * ע�������
 * 1.ע������
 * 2.��֤������
 * @author QinMX
 *
 */
public class RegServer implements Runnable {
	
	private Socket socket;
	public RegServer(Socket socket) {
		this.socket = socket;
	}
	
	//���е���֤�붼�洢������
	private static HashMap<String, String> hashMap_code = new HashMap<String, String>();
	
	public void run() {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = socket.getInputStream();
			output = socket.getOutputStream();
			
			//�ȴ��ͻ��˷�����Ϣ����
			byte[] bytes = new byte[1024];
			int len = input.read(bytes);
			String str = new String(bytes, 0, len);
			JSONObject json = JSONObject.fromObject(str);
			String type = json.getString("type");
			if (type.equals("code")) {//��֤�������
				String username = json.getString("username");
				//6λ�����֤��
				Random random = new Random();
				StringBuffer code = new StringBuffer();
				for (int i = 0; i < 6; i++) {
					//code.append(random.nextInt(10));
					code.append(0);//������ʱʹ��Ĭ����֤��00000
				}
				
				if(username.trim().length() == 11) {
					try {
						Long.parseLong(type);
						hashMap_code.put(username, code.toString());
						//SendCode.send(username, code.toString());
						output.write("{\"state\":0,\"msg\":\"��֤�뷢�ͳɹ���\"}".getBytes());
						output.flush();
					} catch (Exception e) {
						output.write("{\"state\":1,\"msg\":\"��֤�뷢��ʧ�ܣ�\"}".getBytes());
						output.flush();
					}
				} else {
					if(username.indexOf("@") >= 0) {
						hashMap_code.put(username, code.toString());
						output.write("{\"state\":0,\"msg\":\"��֤�뷢�ͳɹ���\"}".getBytes());
						//SendCode.sendEmail(username, code.toString());
						//��ʱʹ��Ĭ����֤��
						
						output.flush();
					} else {
						output.write("{\"state\":1,\"msg\":\"��֤�뷢��ʧ�ܣ�\"}".getBytes());
						output.flush();
					}
				}
				
			} else if (type.equals("reg")) {//ע�������
				String username = json.getString("username");
				String password = json.getString("password");
				String code = json.getString("code");
				String code1 = hashMap_code.get(username);
				if (code1 != null) {
					hashMap_code.remove(username);//һ��Ҫɾ���������Ƿ���ȷ
				}
				if (code1.equals(code)) {//ѯ����֤���Ƿ���ȷ
					try {
						new UserService().regUser(username, password);
					} catch (UsernameException e) {
						output.write("{\"state\":1,\"msg\":\"�û����Ѿ����ڣ�\"}".getBytes());
						output.flush();
						return;
					} catch (SQLException e) {
						output.write("{\"state\":3,\"msg\":\"δ֪����\"}".getBytes());
						output.flush();
						return;
					}
					output.write("{\"state\":0,\"msg\":\"ע��ɹ������Ե�¼�ˣ�\"}".getBytes());
					output.flush();
				} else {
					output.write("{\"state\":0,\"msg\":\"��֤����������»�ã�\"}".getBytes());
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
