package com.myQQ.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.myQQ.view.util.Config;

import net.sf.json.JSONObject;

import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.ComponentOrientation;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class TalkFrame extends JFrame implements WindowListener{
	JLabel img = new JLabel("");
	JLabel netName = new JLabel("New label");
	JLabel info = new JLabel("New label");
	
	private String uid;
	private String netNameStr;
	private String imgDir;
	private String infoStr;

	private JPanel contentPane;
	private JTextArea textArea;
	private JTextArea textArea_1;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					TalkFrame frame = new TalkFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	
	//添加消息
	public void addMessage(Msg msg) {
		String str = "\n" + netNameStr + "\t" + new Date().toLocaleString() + "\n" + msg.getMsg() + "\n";
		
		textArea_1.setText(textArea_1.getText() + str);
		textArea_1.setSelectionStart(textArea_1.getText().toString().length());//设置光标
		textArea_1.setSelectionEnd(textArea_1.getText().toString().length());
		
		textArea.requestFocus();
	}
	
	public void addMyMessage(Msg msg) {
		String str = "\n" + JSONObject.fromObject(Config.own_json_data).getString("netname") + "\t" + new Date().toLocaleString() + "\n" + msg.getMsg() + "\n";
		
		textArea_1.setText(textArea_1.getText() + str);
		textArea_1.setSelectionStart(textArea_1.getText().toString().length());
		textArea_1.setSelectionEnd(textArea_1.getText().toString().length());
		
		textArea.requestFocus();
	}

	/**
	 * Create the frame.
	 */
	public TalkFrame(String uid, String netNameStr, String imgDir, String infoStr) {
		this.uid = uid;
		this.netNameStr = netNameStr;
		this.imgDir = imgDir;
		this.infoStr = infoStr;
		
		this.setTitle(netNameStr);
		ImageIcon imageIcon = new ImageIcon("face0/" + imgDir + ".png");
		this.setIconImage(imageIcon.getImage());
		
		netName.setText(netNameStr);
		info.setText(infoStr);
		img.setIcon(new ImageIcon("face0/" + imgDir + ".png"));
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//一同关闭
		setBounds(100, 100, 600, 660);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		//头像
		img.setPreferredSize(new Dimension(48, 48)); //设置label大小
		panel.add(img, BorderLayout.WEST);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		//昵称
		netName.setFont(new Font("微软雅黑", Font.BOLD, 28));
		panel_1.add(netName, BorderLayout.CENTER);
		
		//签名
		panel_1.add(info, BorderLayout.SOUTH);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		splitPane.setRightComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JButton btnNewButton = new JButton("\u5B57\u4F53");
		panel_3.add(btnNewButton);
		
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton btnNewButton_1 = new JButton("\u5173\u95ED");
		panel_4.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("\u53D1\u9001");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Msg msg = new Msg();
					msg.setCode(System.currentTimeMillis() + "");//转字符串
					msg.setMyUID(JSONObject.fromObject(Config.own_json_data).getString("uid"));
					msg.setToUID(uid);
					msg.setMsg(textArea.getText());
					msg.setType("msg");
					
					String json = JSONObject.fromObject(msg).toString();
					byte[] bytes = json.getBytes();
					
					DatagramPacket datagramPacket = new DatagramPacket(bytes,bytes.length, InetAddress.getByName(Config.IP), 4003);
					Config.datagramSocket_client.send(datagramPacket);
					textArea.setText("");//发送后清空输入框中的文字
					
					addMyMessage(msg);//输入框中的文字显示到聊天框中
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		panel_4.add(btnNewButton_2);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setLeftComponent(scrollPane_1);
		
		textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		splitPane.setDividerLocation(290);
		
		this.addWindowListener(this);
		this.setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		Config.closeTalkFrame(uid);
		this.dispose();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

}
