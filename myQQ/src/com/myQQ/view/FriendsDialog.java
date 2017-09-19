package com.myQQ.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.myQQ.view.util.Config;

import net.sf.json.JSONObject;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 好友信息列表
 * @author QinMX
 *
 */
public class FriendsDialog extends JDialog {
	
	JLabel myNetName = new JLabel("New user");
	JLabel myFace = new JLabel(new ImageIcon("face0/37.png"));
	JLabel myInfo = new JLabel("");

	/**
	 * Launch the application.
	 */
	//主函数一定要注释，因为static！！！！！该类在还没登录就已经运行了！！！！！
//	public static void main(String[] args) {
//		try {
//			FriendsDialog dialog = new FriendsDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	//{"dd":0,"mm":0,"yy":0,"img":"1","sex":"","phonenumber":"18913973607","back":"","uid":"448280929","netname":"小轩轩","name":"","email":"","info":"我爱小灰灰"}
	public void updata() {
		JSONObject jsonObject = JSONObject.fromObject(Config.own_json_data);
		
		this.setTitle(jsonObject.getString("netname") + "myQQ");
		
		myNetName.setText(jsonObject.getString("netname"));
		myInfo.setText(jsonObject.getString("info"));
		myFace.setIcon(new ImageIcon("face0/" + jsonObject.getString("img") + ".png"));
	}

	/**
	 * Create the dialog.
	 */
	public FriendsDialog() {
		setBounds(100, 100, 300, 602);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(5, 5));//设置间距
		
		
		myFace.setPreferredSize(new Dimension(55, 55)); //设置label大小
		panel.add(myFace, BorderLayout.WEST);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(5, 5));
		
		
		panel_1.add(myInfo, BorderLayout.SOUTH);
		
		
		myNetName.setFont(new Font("微软雅黑", Font.PLAIN, 28));
		panel_1.add(myNetName, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.WEST);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JButton button = new JButton("\u8BBE\u7F6E");
		panel_3.add(button, BorderLayout.CENTER);
		
		JButton btnNewButton = new JButton("\u67E5\u627E");
		panel_3.add(btnNewButton, BorderLayout.EAST);
		
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.EAST);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JButton button_1 = new JButton("\u9000\u51FA");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		panel_4.add(button_1);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("\u6211\u7684\u597D\u53CB", null, panel_5, null);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_5.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(new FriendsListJPanel());//添加好友列表Panel
		
		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("\u7FA4\u804A\u5929", null, panel_6, null);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		updata();
	}

}
