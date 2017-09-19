package com.myQQ.view.service;

import java.util.HashMap;
import java.util.LinkedList;

import com.myQQ.view.Msg;
import com.myQQ.view.TalkFrame;
import com.myQQ.view.util.Config;

import net.sf.json.JSONObject;

/**
 * ��Ϣ�� ������е���Ϣ���յ�������洢
 * @author QinMX
 *
 */
public class MessagePool {
	private MessagePool(){}
	
	private static MessagePool messagePool = new MessagePool();
	//��������ģʽ
	public static MessagePool getMessagePool() {
		return messagePool;
	}
	
	public static HashMap<String, LinkedList<Msg>> hashMap = new HashMap();
	
	public void addMessage(String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		String toUID = jsonObject.getString("toUID");
		String myUID = jsonObject.getString("myUID");
		String msg = jsonObject.getString("msg");
		String type = jsonObject.getString("type");
		String code = jsonObject.getString("code");
		
		//�ѽ��պõ���Ϣ ��װ��Msg������
		Msg msgObj = new Msg();
		msgObj.setCode(code);
		msgObj.setMsg(msg);
		msgObj.setMyUID(myUID);
		msgObj.setToUID(toUID);
		msgObj.setType(type);
		
		try {
			TalkFrame talkFrame = Config.talkTable.get(myUID);
			if (talkFrame.isVisible()) {
				talkFrame.addMessage(msgObj);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			//�����ϴ洢Msg���� �Ա�����ȡ���������
			LinkedList<Msg> list = hashMap.get(myUID);
			if (list == null) {
				list = new LinkedList();
			}
			list.add(msgObj);
			hashMap.put(myUID, list);
		}
		
	}

}
