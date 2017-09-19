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
 * ͨѶ���� ���������������״̬
 * 1.���º�������״̬ 5����һ������
 * 2.��¼��֤
 * 3.�˳��˺�
 * @author QinMX
 *
 */
public class NetService implements Runnable {
	private NetService() {
	}
	
	private static NetService netService = new NetService();
	
	//����
	public static NetService getNetService() {
		return netService;
	}

	//����׼������������ֳ�ʱ��ͨѶ
	public void run() {
		try {
			//������Ϣ���
			output.write("U0001".getBytes());
			output.flush();
			byte[] bytes = new byte[1024 * 10];//10���е��ٰɣ��˴�Ӧ����Щ����
			int len = input.read(bytes);
			String jsonstr = new String(bytes, 0, len);
			
			//���������б�
			Config.read_friends_json_data(jsonstr);
			System.out.println("��������"+Config.friends_json_data);
			
			//�������ϻ��
			output.write("U0003".getBytes());
			output.flush();
			len = input.read(bytes);
			Config.own_json_data = new String(bytes, 0, len);
			System.out.println("��������"+Config.own_json_data);
			
			int i = 0;
			//�������ߵ�ʵʱ����
			while (run) {
				output.write("U0002".getBytes());
				output.flush();
				input.read();
				output.write(Config.friends_list_data.getBytes());
				output.flush();
				
				len = input.read(bytes);
				String online = new String(bytes, 0, len);
				System.out.println("�����˻�"+online);
				
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
	private boolean run = false;  //����Ӧ����volatile����ֹͣ�߳�
	private volatile boolean GoOn = false;//����û��volatile���߳̾�һֱ������
	
	public JSONObject login() throws UnknownHostException, IOException{
		socket = new Socket(Config.IP, Config.LOGIN_PORT);
		input = socket.getInputStream();   //�˴�Ӧ���г�ʱ����Socket s = new Socket(); s��connect(new InetSocketAddress(host,port), timeout)!!!��s��setSoTimeout(10000)ֻ�������׽��֣���������
		output = socket.getOutputStream();
		String json_str = "{\"username\":\"" + Config.username + "\",\"password\":\"" + Config.password + "\"}";
		
		//��ʼ�������������Ϣ
		output.write(json_str.getBytes());
		output.flush();
		
		//�ȴ���������ִ��Ϣ
		byte[] bytes = new byte[1024];
		int len = input.read(bytes);
		
		json_str = new String(bytes, 0, len);
		JSONObject json = JSONObject.fromObject(json_str);
		
		//�����0���ǵ�½�ɹ���
		if (json.getInt("state") == 0) {
			//�����������������ӷ���
			if (thread != null) {
				//ѯ���߳��Ƿ񻹻���
				if (thread.getState() == Thread.State.TERMINATED) {
					run = false;//��ֹ�߳�����
					try {
						thread.stop();//ǿ���з���
					} catch (Exception e) {
					}
				}
			}
			run = true;
			//���¿��߳������������ͨѶ
			thread = new Thread(this);
			thread.start();
			
			//����������������
			while (GoOn == false) {//�������һ��BUG������д��if����
				//System.out.println("11111111111111");
			}
		}
		
		
		//����UDP������
		Config.datagramSocket_client = new DatagramSocket();
		//����������
		new MessageRegService(Config.datagramSocket_client);//�ѵ�UDP����һ���˿ڣ���������
		//������Ϣ����
		new MessageService(Config.datagramSocket_client);
		
		return json;
		
	}

}
