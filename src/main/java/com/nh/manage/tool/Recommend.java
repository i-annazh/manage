package com.nh.manage.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import com.nh.manage.until.Db;

public class Recommend {
	
	private static Map<String, String> idTelMap = new HashMap<String, String>();
	
	private static String FILEPATH = "d://test.txt";
	
	public static void dateFoFile(){
		
		String n = System.getProperty("line.separator");
		
		 try {
			 
			 List<Object> list = Db.getTaste();
			 File file = new File(FILEPATH);
			 
			 if(!file.exists()){
				file.createNewFile();
			 }
			
			 FileWriter fileWritter = new FileWriter(file.getAbsoluteFile());
			 BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			 
			 for(Object obj:list){
				 Map<String, Object> tempMap = (Map<String, Object>)obj;
				 String id = tempMap.get("id").toString();
				 String tel = tempMap.get("tel").toString();
				 String goods = tempMap.get("good_ids").toString();
				 idTelMap.put(id, tel);
				 
				 if(StringUtils.isBlank(goods)){
					 continue;
				 }
				 
				 String[] array= goods.trim().split(" ");
				 
				 for(String tempgood:array){
					 String writeStr = id +","+tempgood+",1" + n;
					 bufferWritter.write(writeStr);
				 }
			 }
			 bufferWritter.flush();
			 bufferWritter.close();
			 fileWritter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return;
	}
	
	public static void recommendGoods(){
		
		try{
			
			// 1,构建模型
			DataModel dataModel = new FileDataModel(new File("d://test.txt"));
			// 2,计算相似度
			ItemSimilarity itemSimilarity = new UncenteredCosineSimilarity(dataModel);
		
			GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
		    
		    LongPrimitiveIterator it = dataModel.getUserIDs();
		    
		    
		    Db.truncateTasteResult();
		    while(it.hasNext()){
		    
		    	long tempid = it.nextLong();
		    	
		    	List<RecommendedItem> recommendations = recommender.recommend(tempid, 3);
		    	String temp="";
		    	
			    for (RecommendedItem recommendation : recommendations) {
			    	temp = temp + recommendation.getItemID() + " ";
			    }
			    
			    Db.insertIntoTasteResult(String.valueOf(tempid), idTelMap.get(String.valueOf(tempid)), temp.trim());
		    	
		    }
		    
	
		
            
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
//	public static void main(String[] args) {
//		Recommend.dateFoFile();
//		Recommend.recommendGoods();
//	}

}
