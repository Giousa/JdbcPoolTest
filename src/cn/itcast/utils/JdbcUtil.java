package cn.itcast.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 1. �������� 2. �ر�
 * 
 * @author Jie.Yuan
 * 
 */
public class JdbcUtil {

	// ���Ӳ���
	// private String url = "jdbc:mysql://localhost:3306/jdbc_demo";
	private static String url = "jdbc:mysql:///test";
	private static String user = "root";
	private static String password = "123456";

	/**
	 * �������Ӷ���
	 */
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * �ر�
	 */
	public static void closeAll(Connection con, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();  // �����쳣���� Alt + shift + z 
				rs = null;   // �������������ڻ�����Դ
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (con != null && !con.isClosed()) {
				con.close();
				con = null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}










