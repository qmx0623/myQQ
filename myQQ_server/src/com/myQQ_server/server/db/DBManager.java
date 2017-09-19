package com.myQQ_server.server.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 用于数据库连接配置
 * @author QinMX
 * ComboPooledDataSource对象--赋值给--DataSource对象--内建方法--getConnection()--得到--Connection对象
 */
public class DBManager {
	public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "0987";
	public static final String URL = "jdbc:mysql://127.0.0.1:3306/myqq?characterEncoding=utf8&useSSL=false";
	public static DataSource dataSource = null;
	
	//准备加载数据源 C3P0
	static {
		try {
			ComboPooledDataSource pool = new ComboPooledDataSource();
			pool.setDriverClass(DRIVER_NAME);
			pool.setUser(USERNAME);
			pool.setPassword(PASSWORD);
			pool.setJdbcUrl(URL);
			pool.setMaxPoolSize(30);
			pool.setMinPoolSize(5);
			dataSource = pool;//c3p0的DataSource即ComboPooledDataSource对象赋值给jdbc的DataSource对象！
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据连接池加载失败!");
		}
	}
	
	//通过此方法获得Connection连接对象
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
