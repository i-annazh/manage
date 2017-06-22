package com.nh.manage.tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nh.manage.until.Db;

class Bean{
	int goodId;
	double score;
	
	public int getGoodId() {
		return goodId;
	}

	public void setGoodId(int goodId) {
		this.goodId = goodId;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Bean(int goodId, double score){
		this.goodId = goodId;
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "" + goodId + " " + score;
	}
}

class BeanSort implements Comparator<Bean>{

	@Override
	public int compare(Bean o1, Bean o2) {
		// TODO Auto-generated method stub
		
		if(o1.getScore() > o2.getScore()){
			return -1;
		}else if(o1.getScore() == o2.getScore()){
			return 0;
		}else{
			return 1;
		}
		
		
	}}

public class GoodsForecast {
	

	public static void process(){
		List<Object> list = Db.getGoodsIdByTime();
		long timenow = System.currentTimeMillis()/1000L;
		List<Bean> beanList = new ArrayList<Bean>();
		
		for(Object obj:list){
			
			Map<String, Object> map = (Map<String, Object>)obj;
			int goodId = Integer.parseInt(map.get("good_id").toString());
			
			if(0 == goodId){
				continue;
			}

			List<Object> trendLIst = Db.getTrendByGoodsId(goodId);
			
			//剔除重复的数据 每天自取一条记录
			
			if(trendLIst.size() > 1){
				
				for(int i = 1; i < trendLIst.size(); ++i){
					Map<String, Object> trendNowMap = (Map<String,Object>)trendLIst.get(i);
					Map<String, Object> trendBefMap = (Map<String,Object>)trendLIst.get(i-1);
					int now = Integer.parseInt(trendNowMap.get("create_time").toString());
					int bef = Integer.parseInt(trendBefMap.get("create_time").toString());
					
					if((now -bef) < 24*60*60){
						trendBefMap.put("remove", 1);
					}
				}
			}
			
			//System.out.println(trendLIst.toString());
		
			trendLIst = trendLIst.stream().filter(Object->!Object.toString().contains("remove")).collect(Collectors.toList());
			
			//System.out.println(trendLIst.toString());
			
			double score = 0.1d;
			

			for(int i = 1; i < trendLIst.size(); ++i){
				double rate = 0d;
				int tempscore = 0;
				
				Map<String, Object> trendNowMap = (Map<String,Object>)trendLIst.get(i);
				Map<String, Object> trendBefMap = (Map<String,Object>)trendLIst.get(i-1);
				int comment_numnow = Integer.parseInt(trendNowMap.get("comment_num").toString());
				int comment_numbef = Integer.parseInt(trendBefMap.get("comment_num").toString());
				
				int time = Integer.parseInt(trendNowMap.get("create_time").toString());
				rate = (double)time/(double)timenow;
				
				float price_numnow = Float.parseFloat(trendNowMap.get("price").toString());
				float price_numbef = Float.parseFloat(trendBefMap.get("price").toString());
				
				if(comment_numnow > comment_numbef){
					tempscore = tempscore + 1;
				}
				
				if(price_numnow < price_numbef){
					tempscore = tempscore + 2;
				}
				
				score = score + tempscore*rate;
			}
			
			beanList.add(new Bean(goodId,score));

			
			//break;
		}
		
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); 
		Collections.sort(beanList, new BeanSort());
		
		
		System.out.println(beanList.toString());
		
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(currentTime);
		   
		//save
		String goodIds = "";
		for(int i  =0; i < beanList.size() &&  i < 3; i++){
			goodIds = goodIds + beanList.get(i).getGoodId() + " ";
		}
		
		Db.insertIntoGoodsFor(goodIds.trim(), dateString);
		
	}
	
	public static void main(String[] args) {
		GoodsForecast.process();
	}

}
