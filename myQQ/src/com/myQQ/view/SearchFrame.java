package com.myQQ.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SearchFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchFrame frame = new SearchFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	Vector<String> cols = new Vector<String>();//没有泛型就会报raw type错误
	Vector<String> rows = new Vector<String>();

	/**
	 * Create the frame.
	 */
	public SearchFrame() {
		setTitle("\u67E5\u627E\u597D\u53CB");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 439, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("\u6635\u3000\u79F0\uFF1A");
		label.setBounds(10, 15, 54, 15);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setBounds(74, 7, 244, 32);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("\u67E5\u627E");
		button.setBounds(338, 6, 75, 32);
		contentPane.add(button);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 55, 403, 196);
		contentPane.add(scrollPane);
		
		cols.add("昵称");
		cols.add("在线");
		table = new JTable(rows, cols);
		scrollPane.setViewportView(table);
	}
}
