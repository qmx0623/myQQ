package com.myQQ.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;

import com.myQQ.util.WindowXY;
import com.myQQ.view.service.NetService;
import com.myQQ.view.util.Config;

import net.sf.json.JSONObject;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class LoginDialog extends JDialog {

	private JPanel contentPane;
	private JTextField username;
	private JTextField password;
	private JTextField reg_username;
	private JTextField code;
	private JTextField reg_password1;
	private JTextField reg_password2;

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
//		JFrame.setDefaultLookAndFeelDecorated(true);
//		JDialog.setDefaultLookAndFeelDecorated(true);
//		
//		try {
//			UIManager.setLookAndFeel("java.swing.plaf.nimbus.NimbusLookAndFeel");
//		} catch(Exception e){
//			e.printStackTrace();
//		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginDialog frame = new LoginDialog();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginDialog() {
		setResizable(false);
		setTitle("myQQ");
		setAlwaysOnTop(true);   //至于顶层
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 380, 348);//617 348
		
		setLocation(WindowXY.getXY(this.getSize()));
		
		contentPane = new JPanel();
		contentPane.setToolTipText("\u6CE8\u518C\uFF1A");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		username = new JTextField();
		username.setBounds(105, 128, 230, 32);
		contentPane.add(username);
		username.setColumns(10);
		
		JLabel label = new JLabel("\u624B\u673A\u53F7\uFF1A");
		label.setBounds(31, 128, 54, 15);
		contentPane.add(label);
		
		JLabel lblEmail = new JLabel("Email\uFF1A");
		lblEmail.setBounds(31, 145, 54, 15);
		contentPane.add(lblEmail);
		
		JLabel label_1 = new JLabel("\u5BC6\u3000\u7801\uFF1A");
		label_1.setBounds(31, 192, 54, 15);
		contentPane.add(label_1);
		
		password = new JTextField();
		password.setBounds(105, 184, 230, 32);
		contentPane.add(password);
		password.setColumns(10);
		
		JButton btnNewButton = new JButton("\u6CE8\u3000\u518C");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(LoginDialog.this.getHeight() == 617) {
					LoginDialog.this.setSize(380, 348);
				}else {
					LoginDialog.this.setSize(380, 617);
				}
				setLocation(WindowXY.getXY(LoginDialog.this.getSize()));
			}
		});
		btnNewButton.setBounds(31, 245, 132, 46);
		contentPane.add(btnNewButton);
		
		JButton loginbutton = new JButton("\u767B\u3000\u9646");
		loginbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//用户名和密码
				String username_str = username.getText().trim();
				String password_str = password.getText().trim(); //这里我出bug了，password写成了username
				if (username_str.trim().equals("") || password_str.trim().equals("")) {
					javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "请填写用户名和密码！");
					return;
				}
				Config.username = username_str;
				Config.password = password_str;
				try {
					JSONObject json = NetService.getNetService().login();
					if (json.getInt("state") == 0) {
						//javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "登陆成功");
						//登录成功后 显示好友列表
						new FriendsDialog().setVisible(true);
						LoginDialog.this.setVisible(false); //注意this的用法
						LoginDialog.this.dispose();
					} else {
						javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, json.getString("msg"));//密码错误或者用户名错误
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "网络连接失败");
				}
			}
		});
		loginbutton.setBounds(203, 245, 132, 46);
		contentPane.add(loginbutton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setToolTipText("\u6CE8\u518C\uFF1A");
		panel_1.setBorder(new TitledBorder(null, "\u6CE8\u518C\u7528\u6237\uFF1A", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(31, 315, 304, 253);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel label_2 = new JLabel("\u624B\u673A\u53F7\uFF1A");
		label_2.setBounds(10, 28, 54, 15);
		panel_1.add(label_2);
		
		JLabel lblEmail_1 = new JLabel("Email\uFF1A");
		lblEmail_1.setBounds(10, 44, 54, 15);
		panel_1.add(lblEmail_1);
		
		reg_username = new JTextField();
		reg_username.setBounds(68, 25, 226, 34);
		panel_1.add(reg_username);
		reg_username.setColumns(10);
		
		JLabel label_3 = new JLabel("\u9A8C\u8BC1\u7801\uFF1A");
		label_3.setBounds(10, 82, 54, 15);
		panel_1.add(label_3);
		
		code = new JTextField();
		code.setBounds(68, 73, 105, 34);
		panel_1.add(code);
		code.setColumns(10);
		
		JButton button = new JButton("\u53D1\u9001\u9A8C\u8BC1\u7801");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (reg_username.getText().trim().equals("")) {
					javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "用户名为空");
					return;
				}
				try {
					Socket socket = new Socket(Config.IP, Config.REG_PORT);
					InputStream input = socket.getInputStream();
					OutputStream output = socket.getOutputStream();
					
					output.write(("{\"type\":\"code\",\"username\":\"" + reg_username.getText() + "\"}").getBytes());
					output.flush();
					
					byte[] bytes = new byte[1024];
					int len = input.read(bytes);
					String str = new String(bytes, 0, len);
					JSONObject json = JSONObject.fromObject(str);
					if (json.getInt("state") == 0) {
						javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "发送成功！");
					} else {
						javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "发送失败！");
					}
					
					input.close();
					output.close();
					socket.close();
					
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		});
		button.setBounds(183, 75, 111, 29);
		panel_1.add(button);
		
		JLabel lblNewLabel = new JLabel("\u5BC6\u3000\u7801\uFF1A");
		lblNewLabel.setBounds(10, 131, 54, 15);
		panel_1.add(lblNewLabel);
		
		reg_password1 = new JTextField();
		reg_password1.setBounds(68, 122, 226, 34);
		panel_1.add(reg_password1);
		reg_password1.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\u786E\u8BA4\u5BC6\u7801\uFF1A");
		lblNewLabel_1.setBounds(10, 177, 65, 15);
		panel_1.add(lblNewLabel_1);
		
		reg_password2 = new JTextField();
		reg_password2.setBounds(68, 168, 226, 34);
		panel_1.add(reg_password2);
		reg_password2.setColumns(10);
		
		JButton button_1 = new JButton("\u653E\u3000\u5F03");
		button_1.setBounds(39, 212, 105, 31);
		panel_1.add(button_1);
		
		JButton button_2 = new JButton("\u6CE8\u3000\u518C");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (reg_username.getText().trim().equals("")) {
					javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "用户名不能为空！");
					return;
				}
				if (reg_password1.getText().trim().equals("")) {
					javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "密码不能为空！");
					return;
				}
				if (reg_password2.getText().trim().equals("")) {
					javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "确认密码不能为空！");
					return;
				}
				if (code.getText().trim().equals("")) {
					javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "验证码不能为空！");
					return;
				}
				if (!reg_password1.getText().trim().equals(reg_password2.getText())) {//.trim()????
					javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "两次密码不相等");
					return;
				}
				try {
					Socket socket = new Socket(Config.IP, Config.REG_PORT);
					InputStream input = socket.getInputStream();
					OutputStream output = socket.getOutputStream();
					
					output.write(("{\"type\":\"reg\",\"username\":\"" + reg_username.getText() + "\",\"password\":\"" 
					         + reg_password1.getText() + "\",\"code\":\"" 
					         + code.getText() + "\"}").getBytes());
					output.flush();
					
					byte[] bytes = new byte[1024];
					int len = input.read(bytes);
					String str = new String(bytes, 0, len);
					JSONObject json = JSONObject.fromObject(str);
					if (json.getInt("state") == 0) {
						javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "恭喜你！注册成功！可以登录了");
						reg_username.setText("");
						reg_password1.setText("");
						reg_password2.setText("");
						code.setText("");
					} else if (json.getInt("state") == 1) {
						javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "用户名已存在！");
					} else if (json.getInt("state") == 2) {
						javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "验证码错误，请重新获得！");
					} else if (json.getInt("state") == 3) {
						javax.swing.JOptionPane.showMessageDialog(LoginDialog.this, "未知错误！");
					}
					
					input.close();
					output.close();
					socket.close();
					
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		});
		button_2.setBounds(168, 212, 105, 31);
		panel_1.add(button_2);
	}
}
