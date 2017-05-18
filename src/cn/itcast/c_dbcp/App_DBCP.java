package cn.itcast.c_dbcp;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

public class App_DBCP {

	// 1. Ӳ���뷽ʽʵ�����ӳ�
	@Test
	public void testDbcp() throws Exception {
		// DBCP���ӳغ�����
		BasicDataSource dataSouce = new BasicDataSource();
		// ���ӳز������ã���ʼ������������������� / �����ַ������������û�������
		dataSouce.setUrl("jdbc:mysql:///test");			//���ݿ������ַ���
		dataSouce.setDriverClassName("com.mysql.jdbc.Driver");  //���ݿ�����
		dataSouce.setUsername("root");							//���ݿ������û�
		dataSouce.setPassword("123456"); 							//���ݿ���������
		dataSouce.setInitialSize(3);  // ��ʼ������
		dataSouce.setMaxActive(6);	  // �������
		dataSouce.setMaxIdle(3000);   // ������ʱ��
		
		// ��ȡ����
		Connection con = dataSouce.getConnection();
		con.prepareStatement("delete from teacher where id=3").executeUpdate();
		// �ر�
		con.close();
	}
	
	@Test
	// 2. ���Ƽ������÷�ʽʵ�����ӳ�  ,  ����ά��
	public void testProp() throws Exception {
		// ����prop�����ļ�
		Properties prop = new Properties();
		// ��ȡ�ļ���
		InputStream inStream = App_DBCP.class.getResourceAsStream("db.properties");
		// �������������ļ�
		prop.load(inStream);
		// ����prop���ã�ֱ�Ӵ�������Դ����
		DataSource dataSouce = BasicDataSourceFactory.createDataSource(prop);
		
		// ��ȡ����
		Connection con = dataSouce.getConnection();
		con.prepareStatement("delete from teacher where id=4").executeUpdate();
		// �ر�
		con.close();
	}
}












