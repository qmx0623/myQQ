package com.myQQ_server.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class UserService {
	/**
	 * ʹ��email�˻����е�½
	 * @param email
	 * @param password
	 * @return
	 * @throws UsernameNotFoundException    �û�������
	 * @throws PasswordException            �������
	 * @throws StateException               �˻�������
	 * @throws SQLException                 ���ݿ�����ʧ��
	 * Connection����--�ڽ�����--prepareStatement(���ݿ�����)--PreparedStatement����--�ڽ�����--executeQuery()--ResultSet����
	 */
	public String loginForEmail(String email, String password)
			throws UsernameNotFoundException, PasswordException, StateException, SQLException {
		return login(email, password, "SELECT * FROM users WHERE email=?");
	}
	
	private String login(String key, String password, String sql)
			throws UsernameNotFoundException, PasswordException, StateException, SQLException {
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, key);  //?����Ϊkey
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getInt("state") == 0) {//�˻�״̬�Ƿ��쳣
					if (rs.getString("password").equals(password)) {//ѯ�������Ƿ���ͬ
						return rs.getString(1);//����select��䷵�صĵ�һ��String�������ͬ��getString("uid")
					} else {
						throw new PasswordException();
					}
				} else {
					throw new StateException();
				}
				
			} else {
				throw new UsernameNotFoundException();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			conn.close();
		}
		
	}
	
	/**
	 * ʹ��email�˻����е�½
	 * @param phone
	 * @param password
	 * @return
	 * @throws UsernameNotFoundException    �û�������
	 * @throws PasswordException            �������
	 * @throws StateException               �˻�������
	 * @throws SQLException                 ���ݿ�����ʧ��
	 */
	public String loginForPhone(String phone, String password)
			throws UsernameNotFoundException, PasswordException, StateException, SQLException {
		return login(phone, password, "SELECT * FROM users WHERE phonenumber=?");
	}
	
	/**
	 * ����Լ��ĺ����б���Ϣ
	 * @param uid   �Լ��ı��
	 * @return      ���ѵ��б���Ϣ
	 */
	public Vector<UserInfo> getFriendsList(String uid) throws SQLException {
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			PreparedStatement pst = conn.prepareStatement(
					"SELECT u.`img`,u.`uid`,u.`netname`,u.`info` FROM fiends f" 
			        + " INNER JOIN users u ON u.`uid`=f.`hyuid` AND f.`uid`=?");
			pst.setString(1, uid);
			ResultSet rs = pst.executeQuery();
			Vector<UserInfo> vector = new Vector<UserInfo>();
			while (rs.next()) {
				UserInfo userInfo = new UserInfo();
				userInfo.setImg(rs.getString(1));
				userInfo.setUid(rs.getString(2));
				userInfo.setNetname(rs.getString(3));
				userInfo.setInfo(rs.getString(4));
				vector.add(userInfo);
			}
			return vector;
			
		} catch (SQLException e) {
			throw e;
		} finally {
			conn.close();
		}
	}
	
	/**
	 * �������ϲ�ѯ
	 * �������ϲ�ѯ
	 * @param uid
	 * @return ������Ϣ
	 * @throws SQLException
	 */
	public UserInfo2 getUserInfo(String uid) throws SQLException {
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			PreparedStatement pst = conn.prepareStatement(
					"SELECT * FROM USERS WHERE UID=?");
			pst.setString(1, uid);
			ResultSet rs = pst.executeQuery();
			UserInfo2 userInfo2 = new UserInfo2();//friendListֻ���UserInfo����Ļ�����Ϣ
			if (rs.next()) {
				userInfo2.setUid(rs.getString("uid"));
				userInfo2.setPhonenumber(rs.getString("phonenumber"));
				userInfo2.setNetname(rs.getString("netname"));
				userInfo2.setInfo(rs.getString("info"));
				userInfo2.setName(rs.getString("name"));
				userInfo2.setImg(rs.getString("img"));
				userInfo2.setBack(rs.getString("back"));
				userInfo2.setSex(rs.getString("sex"));
				userInfo2.setYy(rs.getInt("yy"));
				userInfo2.setMm(rs.getInt("mm"));
				userInfo2.setDd(rs.getInt("dd"));
			}
			return userInfo2;
			
		} catch (SQLException e) {
			throw e;
		} finally {
			conn.close();
		}
	}
	
	public void regUser(String username, String password) throws UsernameException, SQLException {
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			PreparedStatement pst = conn.prepareStatement(
					"SELECT * FROM USERS WHERE phonenumber=? or email=?");
			pst.setString(1, username);
			pst.setString(2, username);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				throw new UsernameException();
			}
			
			if(username.indexOf("@") >= 0) {
				pst = conn.prepareStatement("INSERT INTO users(uid,email,PASSWORD,createtime) VALUES(?,?,?,SYSDATE())");//����sql����﷨Eclipse�����޷�
			} else if (username.trim().length() == 11) {
				pst = conn.prepareStatement("INSERT INTO users(uid,phonenumber,PASSWORD,createtime) VALUES(?,?,?,SYSDATE())");
			}
			
			pst.setString(1, System.currentTimeMillis()+"R"+(int)(Math.random()*10000));
			pst.setString(2, username);
			pst.setString(3, password);
			if (pst.executeUpdate() <= 0) {//С��0����ʧ��
				throw new SQLException();
			}
			
		} catch (SQLException e) {
			throw e;
		} finally {
			conn.close();
		}
	}
	
	public static void main(String[] args) {
		try {
			new UserService().loginForPhone("18913973607", "95271084");
			System.out.println("success!");
		} catch (UsernameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}