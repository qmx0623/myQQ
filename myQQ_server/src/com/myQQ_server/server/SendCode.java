package com.myQQ_server.server;

public class SendCode {
	public static boolean sendEmail(String emailaddress, String code) {
		try {
			myJavaMail email = new myJavaMail();
			email.setHost("smtp.163.com");
			//email.setCharset("UTF-8");
			email.setTo(emailaddress);
			
			email.setFrom("448280931@163.com");
			email.setAuthtication("448280931@163.com", "95271084qmx");
			
			email.setSubject("QinMX通讯");
			email.setContent("验证码是：" + code);
			
			email.send();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
