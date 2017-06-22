package com.nh.manage.until;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.nh.manage.conf.ConstantI;
import com.nh.manage.template.DefaultMapRowMapper;
import com.nh.manage.template.JDBCTemplate;

public class Sphinx {
	
	
	public static List<Map<String, Object>> search(String key, String hidden, String radio){
		
		
		Connection con = getSphinxCon();
		String sort = " ORDER BY score desc , price asc ";
		
		JDBCTemplate jdbcTemplate = null;
		jdbcTemplate = new JDBCTemplate(con);
		
		if(hidden != ""){
			
			if("1".equals(radio)){
				sort = " ORDER BY price asc ";
			}
			
			if("2".equals(radio)){
				sort = " ORDER BY score desc ";
			}
			
		}

		String sql = "select * from goods_detail_info where match('@rtf_name "+ key +"') " + sort;
		 
		try {
			DefaultMapRowMapper defaultRowMapper = new DefaultMapRowMapper();
			List<Map<String, Object>> list = (List<Map<String, Object>>) jdbcTemplate.queryForList(sql, defaultRowMapper);
			return list;
		} catch (Exception e) {
			System.out.println(e);
		} finally{
			if(null != con){
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
	
	
	public static List<Map<String, Object>> searchMayLike(String key){
		
		
		Connection con = getSphinxCon();
		
		JDBCTemplate jdbcTemplate = null;
		jdbcTemplate = new JDBCTemplate(con);

		String sql = "select * from goods_detail_info where match('@rtf_name \""+ key +"\"/1') limit 1,3";
		 
		try {
			DefaultMapRowMapper defaultRowMapper = new DefaultMapRowMapper();
			List<Map<String, Object>> list = (List<Map<String, Object>>) jdbcTemplate.queryForList(sql, defaultRowMapper);
			return list;
		} catch (Exception e) {
			System.out.println(e);
		} finally{
			if(null != con){
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
	
	public static void allPush(){
		
		Connection  con = getSqlCon();
		JDBCTemplate jdbcTemplate = null;
		jdbcTemplate = new JDBCTemplate(con);
		
		String sql = "select * from goods_detail_info where status_flag = 0";
		List<Object>  objList = null;
		try {
			DefaultMapRowMapper defaultRowMapper = new DefaultMapRowMapper();
			objList = jdbcTemplate.queryForList(sql, defaultRowMapper);
			
			if(null == objList || 0 == objList.size()){
				return;
			}
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
		
		// 查询到了数据
		con = getSphinxCon();
		jdbcTemplate = new JDBCTemplate(con);
		try {
			
			for(Object obj:objList){
				Map<String, Object> map = (Map<String, Object>)obj;
				long id = Long.parseLong(map.get("id").toString());
				String name = (String) map.get("name");
				float price= Float.parseFloat(map.get("price").toString());
				String merchant_name = (String) map.get("merchant_name");
				
				if(null == merchant_name){
					merchant_name = "";
				}
				String item_url = (String) map.get("item_url");
				String item_extend = (String) map.get("item_extend");
				int status_flag= 0;
				long create_time = Long.parseLong(map.get("create_time").toString());
				long update_time = Long.parseLong(map.get("update_time").toString());
				String item_from = (String) map.get("item_from");
				int score = Integer.parseInt(map.get("score").toString());
				
				System.out.println(id + name + price + merchant_name + item_url + item_extend + status_flag + create_time + update_time + item_from + score);
				
				
				insertIntoSphinx(id, name, price,
						merchant_name, item_url, item_extend,
						status_flag, create_time, update_time,
						item_from,  score, con);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (null != con) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return ;
	}
	
	private static Connection getSphinxCon(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(ConstantI.SPHINX_URL, "", "");
		} catch (Exception e) {
			System.out.print(e);
			return null;
		}
	}
	
	private static Connection getSqlCon(){
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			
			return DriverManager.getConnection(ConstantI.MYSQL_URL, 
					ConstantI.MYSQL_USER, 
					ConstantI.MYSQL_PWD);
			
		} catch (Exception e) {
			System.out.print(e);
			return null;
		}
	}
	
	
	private static void insertIntoSphinx(
			long id,
			String name,
			float price,
			String merchant_name,
			String item_url,
			String item_extend,
			int status_flag,
			long create_time,
			long update_time,
			String item_from,
			int score,
			Connection con){
		
		JDBCTemplate jdbcTemplate = null;
		jdbcTemplate = new JDBCTemplate(con);
		int rt_uniquekey = hashFunc(item_url);

		String sql = "INSERT INTO goods_detail_info VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 
		try {
			jdbcTemplate.execute("delete from goods_detail_info where id =" + id);
			jdbcTemplate.insert(sql, new Object[]{id, name, merchant_name, 
				String.valueOf(rt_uniquekey),
				status_flag, create_time, update_time, score, price, name,
				merchant_name, item_url, item_extend, item_from});
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return; 
	}
	
	private static int hashFunc(String key){  
	    int arraySize = 11113;          //数组大小一般取质数  
	    int hashCode = 0;  
	    for(int i=0;i<key.length();i++){        //从字符串的左边开始计算  
	        int letterValue = key.charAt(i) - 96;//将获取到的字符串转换成数字，比如a的码值是97，则97-96=1 就代表a的值，同理b=2；  
	        hashCode = ((hashCode << 5) + letterValue) % arraySize;//防止编码溢出，对每步结果都进行取模运算  
	    }  
	    return hashCode;  
	}
	
//	public static void main(String[] args) {
//		System.out.println(JacksonUtils.getJsonString(Sphinx.searchMayLike("海澜之家透气棉麻夏季舒适五分裤")));
//		//Sphinx.allPush();
//	}

}
