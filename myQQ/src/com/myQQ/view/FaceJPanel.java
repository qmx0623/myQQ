package com.myQQ.view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.myQQ.view.util.Config;

/**
 * 好友列表中一个好友的Panel类
 * @author QinMX
 *
 */
public class FaceJPanel extends JPanel implements Comparable<FaceJPanel>, MouseListener {
	private String image; 
	private String netName;
	private String info;
	private String uid;
	private JLabel lblNewLabel;//头像
	private JLabel lblNewLabel_1;//昵称
	private JLabel lblNewLabel_2;//说明
	private boolean online = false;
	int x = 0;
	int y = 0;
	
	public FaceJPanel(String image, String netName, String info, String uid) {
		this.image = image;
		this.netName = netName;
		this.info = info;
		this.uid = uid;
		
		this.setLayout(null);
		
		if (image.equals("def")) {
			image = "0";
		}
		
		lblNewLabel = new JLabel();
		lblNewLabel.setBounds(4, 4, 48, 48);
		add(lblNewLabel);
		setImage(image);
		
		lblNewLabel_1 = new JLabel(netName);
		lblNewLabel_1.setBounds(62, 4, 378, 23);
		add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("微软雅黑 Light", Font.BOLD, 14));
		
		lblNewLabel_2 = new JLabel(info);
		lblNewLabel_2.setBounds(62, 37, 378, 15);
		add(lblNewLabel_2);
		
		this.addMouseListener(this);
		
	}
	
	public void setImage(String image) {
		if (image.equals("def")) {
			image = "0";
		}
		if (online) {
			lblNewLabel.setIcon(new ImageIcon("face0/"+ image +".png"));
		} else {
			lblNewLabel.setIcon(new ImageIcon("face1/"+ image +".png"));
		}
		this.image = image;
	}
	
	public void setNetName(String netName) {
		lblNewLabel_1.setText(netName);
		this.netName = netName;
	}
	
	public void setInfo(String info) {
		lblNewLabel_2.setText(info);
		this.info = info;
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}
	
	public void setOnline(boolean online) {
		this.online = online;
		if (online) {
			lblNewLabel.setIcon(new ImageIcon("face0/"+ image +".png"));
		} else {
			lblNewLabel.setIcon(new ImageIcon("face1/"+ image +".png"));
		}
	}
	
	public int compareTo(FaceJPanel o) {
		if (o.online) {
			return 1;
		} else if (this.online) {
			return -1;
		} else {
			return 0;
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getClickCount() == 2)
			Config.showTalkFrame(uid, netName, image, info);
	}
	public void mouseEntered(MouseEvent arg0) {
		x = this.getX();
		y = this.getY();
		this.setLocation(x-3, y-3);
	}
	public void mouseExited(MouseEvent arg0) {
		this.setLocation(x, y);
	}
	public void mousePressed(MouseEvent arg0) {
		
	}
	public void mouseReleased(MouseEvent arg0) {
		
	}

}
