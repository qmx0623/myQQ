package com.myQQ_server.server.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * �������ݿ���������
 * @author QinMX
 * ComboPooledDataSource����--��ֵ��--DataSource����--�ڽ�����--getConnection()--�õ�--Connection����
 */
public class DBManager {
	public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "0987";
	public static final String URL = "jdbc:mysql://127.0.0.1:3306/myqq?characterEncoding=utf8&useSSL=false";
	public static DataSource dataSource = null;
	
	//׼����������Դ C3P0
	static {
		try {
			ComboPooledDataSource pool = new ComboPooledDataSource();
			pool.setDriverClass(DRIVER_NAME);
			pool.setUser(USERNAME);
			pool.setPassword(PASSWORD);
			pool.setJdbcUrl(URL);
			pool.setMaxPoolSize(30);
			pool.setMinPoolSize(5);
			dataSource = pool;//c3p0��DataSource��ComboPooledDataSource����ֵ��jdbc��DataSource����
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�������ӳؼ���ʧ��!");
		}
	}
	
	//ͨ���˷������Connection���Ӷ���
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
