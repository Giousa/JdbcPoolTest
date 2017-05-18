package cn.itcast.a_hello;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.itcast.utils.JdbcUtil;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class App {

	// 1. Ê¹ÓÃDbUtils×é¼þ¸üÐÂ
		@Test
		public void testUpdate() throws Exception {
			String sql = "delete from teacher where id=10;";
			//1.1 Á¬½Ó
			Connection con = JdbcUtil.getConnection();
			//1.2 ´´½¨ºËÐÄ¹¤¾ßÀà
			QueryRunner qr = new QueryRunner();
			//1.3 ¸üÐÂ
			qr.update(con, sql);
			
			con.close();
		}
		
		// 2. Ê¹ÓÃDbUtils×é¼þ²éÑ¯
		@Test
		public void testQuery() throws Exception {
			String sql = "select * from teacher";
			//1.1 Á¬½Ó
			Connection con = JdbcUtil.getConnection();
			//1.2 ´´½¨ºËÐÄ¹¤¾ßÀà
			QueryRunner qr = new QueryRunner();
			//1.3 ²éÑ¯
			List<Admin> list = qr.query(con, sql, new BeanListHandler<Admin>(Admin.class));
			
			System.out.println(list);
			
			con.close();
		}

}
