package com.myQQ.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class ProfileFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel label;
	private JTextField textField_2;
	private JLabel label_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProfileFrame frame = new ProfileFrame();
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
	public ProfileFrame() {
		setTitle("\u4E2A\u4EBA\u8D44\u6599");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 468, 397);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setPreferredSize(new Dimension(60, 60));
		lblNewLabel.setBounds(10, 10, 60, 60);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(80, 10, 364, 35);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(80, 49, 364, 21);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u4E2A\u4EBA\u8D44\u6599", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(20, 88, 410, 273);
		contentPane.add(panel);
		panel.setLayout(null);
		
		label = new JLabel("\u771F\u5B9E\u59D3\u540D\uFF1A");
		label.setBounds(10, 29, 69, 30);
		panel.add(label);
		
		textField_2 = new JTextField();
		textField_2.setBounds(84, 30, 125, 30);
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		label_1 = new JLabel("\u6027\u522B\uFF1A");
		label_1.setBounds(241, 26, 57, 36);
		panel.add(label_1);
		
		JRadioButton radioButton = new JRadioButton("\u7537");
		radioButton.setBounds(301, 33, 47, 23);
		panel.add(radioButton);
		
		JRadioButton radioButton_1 = new JRadioButton("\u5973");
		radioButton_1.setBounds(350, 33, 47, 23);
		panel.add(radioButton_1);
		
		JLabel label_2 = new JLabel("\u51FA\u751F\u5E74\u6708\uFF1A");
		label_2.setBounds(10, 77, 69, 30);
		panel.add(label_2);
		
		JList list = new JList();
		list.setBounds(120, 99, 1, 1);
		panel.add(list);
		
		JLabel label_3 = new JLabel("\u5E74");
		label_3.setBounds(174, 85, 21, 15);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("\u6708");
		label_4.setBounds(277, 85, 21, 15);
		panel.add(label_4);
		
		JLabel label_5 = new JLabel("\u65E5");
		label_5.setBounds(376, 85, 21, 15);
		panel.add(label_5);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(209, 80, 57, 25);
		panel.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(84, 80, 80, 25);
		panel.add(comboBox_1);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(309, 80, 57, 25);
		panel.add(comboBox_2);
		
		JLabel label_6 = new JLabel("\u5907\u3000\u3000\u6CE8\uFF1A");
		label_6.setBounds(10, 130, 69, 15);
		panel.add(label_6);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(84, 130, 305, 122);
		panel.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
	}
}
