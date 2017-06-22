package com.nh.manage.until;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.ResultSetMetaData;

import com.nh.manage.conf.ConstantI;
import com.nh.manage.template.DefaultMapRowMapper;
import com.nh.manage.template.JDBCTemplate;
import com.nh.manage.template.RowMapper;


public class Db {
	
	public static Connection getSqlCon(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(ConstantI.MYSQL_URL, ConstantI.MYSQL_USER, ConstantI.MYSQL_PWD);
		} catch (Exception e) {
			System.out.print(e);
			return null;
		}
	}
	
	public static List<Object> getGoodsIdByTime(){
		
		long time = System.currentTimeMillis()/1000L;
		time = time - 30*24*60*60;
		
		System.out.println(time);
		String sql = "select good_id from goods_trend where create_time >  ? group by good_id";

		Connection con = getSqlCon();
		JDBCTemplate jdbcTemplate = null;
		jdbcTemplate = new JDBCTemplate(con);

		try {
			
			DefaultMapRowMapper defaultRowMapper = new DefaultMapRowMapper();
			return jdbcTemplate.queryForList(sql, new Object[]{time}, defaultRowMapper);
			 
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (null != con) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	
	public static List<Object> getTrendByGoodsId(int goodId){
		
		long time = System.currentTimeMillis()/1000L;
		time = time - 30*24*60*60;
		
		System.out.println(time);
		String sql = "select comment_num,price,create_time from goods_trend where good_id = ? and create_time > ? order by id asc";
		//select comment_num,price,create_time from goods_trend where good_id = 1 and create_time > 1491552074 order by id asc
		Connection con = getSqlCon();
		JDBCTemplate jdbcTemplate = null;
		jdbcTemplate = new JDBCTemplate(con);

		try {
			
			DefaultMapRowMapper defaultRowMapper = new DefaultMapRowMapper();
			return jdbcTemplate.queryForList(sql, new Object[]{goodId, time}, defaultRowMapper);
			 
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (null != con) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	public static void insertIntoGoodsFor(String goodIds, String time) {

		Connection con = getSqlCon();
		JDBCTemplate jdbcTemplate = null;
		jdbcTemplate = new JDBCTemplate(con);

		String sql = "INSERT INTO crawler.goods_forecast(good_ids, create_time) VALUES (?,?)";

		try {
			jdbcTemplate.insert(sql, new Object[] {goodIds, time});
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (null != con) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return;
	}
	
	
	public static List<Object> getTaste(){
		
		long time = System.currentTimeMillis()/1000L;
		time = time - 30*24*60*60;
		
		System.out.println(time);
		String sql = "select id,tel,good_ids from user_taste";
		Connection con = getSqlCon();
		JDBCTemplate jdbcTemplate = null;
		jdbcTemplate = new JDBCTemplate(con);

		try {
			
			DefaultMapRowMapper defaultRowMapper = new DefaultMapRowMapper();
			return jdbcTemplate.queryForList(sql, defaultRowMapper);
			 
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (null != con) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	
	public static void truncateTasteResult(){
		Connection con = getSqlCon();
		JDBCTemplate jdbcTemplate = null;
		jdbcTemplate = new JDBCTemplate(con);
		
	
		try {
			jdbcTemplate.execute("truncate crawler.user_taste_result");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (null != con) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return;
	}
	
	public static void insertIntoTasteResult(String user_id, String tel,String good_ids) {

		Connection con = getSqlCon();
		JDBCTemplate jdbcTemplate = null;
		jdbcTemplate = new JDBCTemplate(con);
		
		String sql = "INSERT INTO crawler.user_taste_result(user_id, tel, mac, good_ids,create_time) VALUES (?,?,?,?,?)";
		try {
			jdbcTemplate.insert(sql, new Object[] {user_id, tel, "", good_ids, (int)(System.currentTimeMillis()/1000L)});
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (null != con) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return;
	}

}
