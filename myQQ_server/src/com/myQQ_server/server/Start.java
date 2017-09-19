package com.myQQ_server.server;

public class Start {
	public static void main (String[] args) {
		new Thread() {
			public void run() {
				try {
					System.out.println("登录服务器启动成功！");
					LoginServer.openServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		new Thread() {
			public void run() {
				try {
					System.out.println("注册服务器启动成功！");
					RegServer.openServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		new Thread() {
			public void run() {
				try {
					System.out.println("消息转发服务器启动成功！");
					UDPMessageServer.openServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

}
