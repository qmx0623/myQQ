package com.myQQ.view;

import javax.swing.JPanel;

import com.myQQ.view.util.Config;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class FriendsListJPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public FriendsListJPanel() {
		setLayout(null);
		
//		for (int i = 0; i < 40; i++) {
//			JPanel panel = new JPanel();
//			panel.setBounds(0, i*50, 450, 58);//58-4-4
//			add(panel);
//			panel.setLayout(null);
//			
//			JLabel lblNewLabel = new JLabel(new ImageIcon("face0/"+ i +".png"));
//			lblNewLabel.setBounds(4, 4, 48, 48);
//			panel.add(lblNewLabel);
//			
//			JLabel lblNewLabel_1 = new JLabel("New label");
//			lblNewLabel_1.setBounds(62, 4, 378, 23);
//			panel.add(lblNewLabel_1);
//			lblNewLabel_1.setFont(new Font("΢���ź� Light", Font.BOLD, 14));
//			
//			JLabel lblNewLabel_2 = new JLabel("New label");
//			lblNewLabel_2.setBounds(62, 37, 378, 15);
//			panel.add(lblNewLabel_2);
//		}
//		//���ù���panel��С
//		this.setPreferredSize(new Dimension(0, 40*50));
		
		FriendsUpdata();
		
		Config.friendsListJPanel = this;
	}
	
	private Hashtable<String, FaceJPanel> list = new Hashtable<String, FaceJPanel>();
	
	//�������߸���
	public void OnlineFriendsUpdata() {
		//�����б�
		String friends_online = Config.friends_online;
		String[] uids = friends_online.split(",");
		
		//����ȫ������Ϊ����
		Set<String> keys = list.keySet();
		for (String string : keys) {
			list.get(string).setOnline(false);
		}
		
		if (!friends_online.equals("notFound") && !friends_online.trim().equals("")) {//û�����ߺ���
			for (String uid : uids) {
				if (!uid.trim().equals("")) {
					FaceJPanel faceJPanel = (FaceJPanel)list.get(uid);
					faceJPanel.setOnline(true);
				}
			}
		}
		Collection<FaceJPanel> faceJPanels = list.values();
		List<FaceJPanel> list = new ArrayList<FaceJPanel>(faceJPanels);
		Collections.sort(list);
		
		this.removeAll();
		int i = 0;
		for (FaceJPanel faceJPanel : list) {
			faceJPanel.setBounds(0, i++*55, 450, 58);//58-4-4
			this.add(faceJPanel);
		}
		
		//���ù���panel��С
		this.setPreferredSize(new Dimension(0, 40*list.size()));
		this.updateUI();
		
	}
	
	//���º����б�(�����Ƿ�����)
	public void FriendsUpdata() {
		//�����б�
		String friends_json_data = Config.friends_json_data;
		System.out.println("11111111111");
		System.out.println(Config.friends_json_data);
		JSONArray jsonArray = JSONArray.fromObject(friends_json_data);
		
		if (list.size() == 0) {//��һ�μ����б�
			
			//{"img":"def","info":"....",....}
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				list.put(jsonObject.getString("uid"), new FaceJPanel(jsonObject.getString("img"),
						jsonObject.getString("netname"),jsonObject.getString("info"),jsonObject.getString("uid")));
			}
			
		} else {//�Ѿ����б�����
			
			
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				String uid = jsonObject.getString("uid");
				
				FaceJPanel faceJPanel = (FaceJPanel)list.get(uid);
				if (faceJPanel != null) {//�Ѿ�����
					faceJPanel.setNetName(jsonObject.getString("netname"));
					faceJPanel.setNetName(jsonObject.getString("info"));
					faceJPanel.setNetName(jsonObject.getString("image"));
				} else {
					list.put(jsonObject.getString("uid"), new FaceJPanel(jsonObject.getString("img"),
							jsonObject.getString("netname"),jsonObject.getString("info"),jsonObject.getString("uid")));
				}
			}
			
		}
		OnlineFriendsUpdata();
	}
}
