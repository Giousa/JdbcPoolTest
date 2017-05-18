package cn.itcast.a_hello;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * �Զ������ӳ�, ��������
 * ����ʵ�֣�
	1.  MyPool.java  ���ӳ��࣬   
	2.  ָ��ȫ�ֲ�����  ��ʼ����Ŀ���������������ǰ���ӡ�   ���ӳؼ���
	3.  ���캯����ѭ������3������
	4.  дһ���������ӵķ���
	5.  ��ȡ����
	------>  �жϣ� ���������ӣ� ֱ����
	 ------>                ����û�����ӣ�
	------>                 �жϣ��Ƿ�ﵽ����������� �ﵽ���׳��쳣��û�дﵽ�����������
			�����µ�����
	6. �ͷ�����
	 ------->  ���ӷŻؼ�����(..)
 *
 */
public class MyPool {

	private int init_count = 3;		// ��ʼ��������Ŀ
	private int max_count = 6;		// ���������
	private int current_count = 0;  // ��¼��ǰʹ��������
	// ���ӳ� ��������еĳ�ʼ�����ӣ�
	private LinkedList<Connection> pool = new LinkedList<Connection>();
	
	
	//1.  ���캯���У���ʼ�����ӷ������ӳ�
	public MyPool() {
		// ��ʼ������
		for (int i=0; i<init_count; i++){
			// ��¼��ǰ������Ŀ
			current_count++;
			// ����ԭʼ�����Ӷ���
			Connection con = createConnection();
			// �����Ӽ������ӳ�
			pool.addLast(con);
		}
	}
	
	//2. ����һ���µ����ӵķ���
	private Connection createConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// ԭʼ��Ŀ�����
			final Connection con = DriverManager.getConnection("jdbc:mysql:///test", "root", "123456");
			
			/**********��con�������**************/
			
			// ��con������������
			Connection proxy = (Connection) Proxy.newProxyInstance(
					
					con.getClass().getClassLoader(),    // �������
					//con.getClass().getInterfaces(),   // ��Ŀ�������һ����������ʱ�� 
					new Class[]{Connection.class},      // Ŀ�����ʵ�ֵĽӿ�
					
					new InvocationHandler() {			// ������con���󷽷���ʱ�� �Զ�������������
						@Override
						public Object invoke(Object proxy, Method method, Object[] args)
								throws Throwable {
							// ��������ֵ
							Object result = null;
							// ��ǰִ�еķ����ķ�����
							String methodName = method.getName();
							
							// �жϵ�ִ����close������ʱ�򣬰����ӷ������ӳ�
							if ("close".equals(methodName)) {
								System.out.println("begin:��ǰִ��close������ʼ��");
								// ���ӷ������ӳ� (�ж�..)
								pool.addLast(con);
								System.out.println("end: ��ǰ�����Ѿ��������ӳ��ˣ�");
							} else {
								// ����Ŀ����󷽷�
								result = method.invoke(con, args);
							}
							return result;
						}
					}
			);
			return proxy;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//3. ��ȡ����
	public Connection getConnection(){
		
		// 3.1 �ж����ӳ����Ƿ�������, ��������ӣ���ֱ�Ӵ����ӳ�ȡ��
		if (pool.size() > 0){
			return pool.removeFirst();
		}
		
		// 3.2 ���ӳ���û�����ӣ� �жϣ����û�дﵽ�����������������
		if (current_count < max_count) {
			// ��¼��ǰʹ�õ�������
			current_count++;
			// ��������
			return createConnection();
		}
		
		// 3.3 �����ǰ�Ѿ��ﵽ������������׳��쳣
		throw new RuntimeException("��ǰ�����Ѿ��ﵽ���������Ŀ ��");
	}
	
	
	//4. �ͷ�����
	public void realeaseConnection(Connection con) {
		// 4.1 �жϣ� �ص���Ŀ���С�ڳ�ʼ�����ӣ��ͷ������
		if (pool.size() < init_count){
			pool.addLast(con);
		} else {
			try {
				// 4.2 �ر� 
				current_count--;
				con.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static void main(String[] args) throws SQLException {
		MyPool pool = new MyPool();
		System.out.println("��ǰ����: " + pool.current_count);  // 3
		
		// ʹ������
		pool.getConnection();
		pool.getConnection();
		Connection con4 = pool.getConnection();
		Connection con3 = pool.getConnection();
		Connection con2 = pool.getConnection();
		Connection con1 = pool.getConnection();
		
		// �ͷ�����, ���ӷŻ����ӳ�
//		pool.realeaseConnection(con1);
		/*
		 * ϣ�������ر����ӵ�ʱ��Ҫ�����ӷ������ӳأ���������Connection�ӿڵ�close����ʱ��ϣ������pool.addLast(con);������
		 * 																			�����ӷ������ӳ�
		 * ���1��ʵ��Connection�ӿڣ���дclose����
		 * ���2����̬����
		 */
		con1.close();
		
		// �ٻ�ȡ
		pool.getConnection();
		
		System.out.println("���ӳأ�" + pool.pool.size());      // 0
		System.out.println("��ǰ����: " + pool.current_count);  // 3
	}
	
}














