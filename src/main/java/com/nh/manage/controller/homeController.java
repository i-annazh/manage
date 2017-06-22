package com.nh.manage.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestController;

import com.nh.manage.entity.AnalyseResult;
import com.nh.manage.entity.GoodForecast;
import com.nh.manage.entity.GoodsDetailInfo;
import com.nh.manage.entity.GoodsTrend;
import com.nh.manage.entity.Navigation;
import com.nh.manage.entity.UserTasteResult;
import com.nh.manage.model.AnalyseResultDao;
import com.nh.manage.model.GoodForecastDao;
import com.nh.manage.model.GoodsDetailInfoDao;
import com.nh.manage.model.GoodsTrendDao;
import com.nh.manage.model.NavigationDao;
import com.nh.manage.model.UserTasteDao;
import com.nh.manage.model.UserTasteResultDao;
import com.nh.manage.until.JacksonUtils;
import com.nh.manage.until.Sphinx;

import antlr.StringUtils;

@Controller
@RequestMapping("/manage")
public class homeController {

	/**  
     * 返回html模板.  
     */  
	
	@Resource
	NavigationDao navigationDao;
	@Resource
	GoodsDetailInfoDao goodsDetailInfoDao;
	@Resource
	GoodsTrendDao goodsTrendDao;
	@Resource
	AnalyseResultDao analyseResultDao;
	@Resource
	GoodForecastDao goodForecastDao;
	@Resource
	UserTasteResultDao userTasteResultDao;
   
    @RequestMapping("/index")  
    public String indexHtml(HttpServletRequest request){
    	return "index";
    	
    }
    @RequestMapping("/about")
    public String aboutHtml(){
    	return "about";
    }
    @RequestMapping("/contact")
    public String contactHtml(){
    	return "contact";
    }
    //热销商品推荐
    @RequestMapping("/goodforecast")
    public String contactHtml(Model model,HttpServletRequest request){
    	String goodids=null;
    	List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    	
    	List<Map<String, Object>> list = new ArrayList();
    	Map<String, Object> map = new HashMap();
    	map.put("id", 1);
    	map.put("name", "test");
    	map.put("price", 1);
    	map.put("item_url", "test");
    	list.add(map);
    	
    	//自选推荐日期
    	String chooseDate = request.getParameter("searcheKey");
    	if(null != chooseDate){
    		chooseDate = chooseDate.replace("-", "").trim();
        	System.out.println("--------------------"+chooseDate+"-------------------------");
        	
        	List<GoodForecast> goodBeanList= goodForecastDao.findGoodForecastDaoBycreateTime(Integer.parseInt(chooseDate));
        	
        	if(null == goodBeanList||goodBeanList.size() == 0){
        		System.out.println("没有查到相关数据");
        	}else {
	        	for(GoodForecast n:goodBeanList){
	        		goodids = n.getGood_ids();
	        	}
	        	String[] good_id = goodids.split(" ");
	        	
	        	
	        	for(int i=0; i< good_id.length;i++){
	        		
	        		List<GoodsDetailInfo> gdList = goodsDetailInfoDao.findOrderByGoodId(Integer.parseInt(good_id[i]));
	        		
	        		if(null != gdList && 0 != gdList.size()){
	        			GoodsDetailInfo gd = gdList.get(0);
	        			Map<String, Object> temMap = new HashMap<String, Object>();
	        			temMap.put("id", good_id[i]);
	        			temMap.put("name", gd.getName());
	        			temMap.put("price", gd.getPrice());
	        			temMap.put("item_url", gd.getItem_url());
	        			mapList.add(temMap);
	        		}
	        	}
        	}
    	}else{
    	
    	
    	Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(currentTime);
		System.out.println(dateString);
		
		
		
		
    	List<GoodForecast> goodBeanList= goodForecastDao.findGoodForecastDaoBycreateTime(Integer.parseInt(dateString));
    	
    	if(null == goodBeanList||goodBeanList.size() == 0){
    		System.out.println("没有查到相关数据");
    	}
    	for(GoodForecast n:goodBeanList){
    		goodids = n.getGood_ids();
    	}
    	String[] good_id = goodids.split(" ");
    	//List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    	
    	for(int i=0; i< good_id.length;i++){
    		
    		List<GoodsDetailInfo> gdList = goodsDetailInfoDao.findOrderByGoodId(Integer.parseInt(good_id[i]));
    		
    		if(null != gdList && 0 != gdList.size()){
    			GoodsDetailInfo gd = gdList.get(0);
    			Map<String, Object> temMap = new HashMap<String, Object>();
    			temMap.put("id", good_id[i]);
    			temMap.put("name", gd.getName());
    			temMap.put("price", gd.getPrice());
    			temMap.put("item_url", gd.getItem_url());
    			mapList.add(temMap);
    		}
    	}
    }
    	model.addAttribute("prods", mapList);
    			
    	return "goodforecast";
    }
    @RequestMapping("/single")
    public String singleHtml(Model model,HttpServletRequest request){
    	//1.判断cookie是否存在
    	//存在2.获取cookie里的tel
    	//3.查询显示在界面上
    	String  tel = null;
    	
    	List<Map<String, Object>> list = new ArrayList();
    	Map<String, Object> map = new HashMap();
    	map.put("id", 1);
    	map.put("name", "test");
    	map.put("price", 1);
    	map.put("item_url", "test");
    	list.add(map);
    	
    	if(null != request.getAttribute("cookieTel")){
    		tel = request.getAttribute("cookieTel").toString();
    		System.out.println("--------------------"+ tel);
    		
    		
    		List<UserTasteResult> utList = userTasteResultDao.findGoodidsByTel(tel);
    		
    		
    		if(null !=utList && utList.size() > 0){
    			UserTasteResult userTasteResultBean = utList.get(0);
    				String goodids = userTasteResultBean.getGood_ids();
		    		
		        	List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		        	
		        	if(org.apache.commons.lang.StringUtils.isNotBlank(goodids)){
			    		String[] good_id = goodids.split(" ");

			        	for(int i=0; i< good_id.length;i++){
			        		
			        		List<GoodsDetailInfo> gdList = goodsDetailInfoDao.findOrderByGoodId(Integer.parseInt(good_id[i]));
			        		
			        		if(null != gdList && 0 != gdList.size()){
			        			GoodsDetailInfo gd = gdList.get(0);
			        			Map<String, Object> temMap = new HashMap<String, Object>();
			        			temMap.put("id", good_id[i]);
			        			temMap.put("name", gd.getName());
			        			temMap.put("price", gd.getPrice());
			        			temMap.put("item_url", gd.getItem_url());
			        			mapList.add(temMap);
			        		}
			        	}
		        	
		        	}
    			
		        	model.addAttribute("prods", mapList);
    			}
        	
    	}else{
    		System.out.println("当前用户没有登入过，请点击关注按钮输入手机号码登入");
    	}
    	
    	
    	

    	return "single";
    }
    @RequestMapping("/GoodsDetails")
    public String goodsdetailsHtml(Model model, HttpServletRequest request){
    	String url = request.getParameter("Url");
    	System.out.println(url);
    	List<Map<String, Object>> list = new ArrayList();
    	Map<String, Object> map = new HashMap();
    	map.put("id", 1);
    	map.put("name", "test");
    	map.put("price", 1);
    	map.put("item_url", "test");
    	map.put("img_url", "test");
    	map.put("item_from", "test");
    	list.add(map);
    	
    	//model.addAttribute("prods", list);
    	
    	List<GoodsDetailInfo> resultLists = null;
    	
    	resultLists = goodsDetailInfoDao.findOrderByUrl(url);
    	//resultList = navigationDao.findAll();
    	List<Map<String, Object>> finalList = new ArrayList<Map<String, Object>>();
    	for(GoodsDetailInfo n:resultLists){
    		System.out.println(n.toString());
    		finalList.add(n.toMap());
    	}
    	
//    	Navigation v =  navigationDao.findOrderBySourceName("京东");
//    	System.out.println(v.toString());
    
	    		
    	model.addAttribute("prods", finalList);
    	
    	return "GoodsDetails";
    }
    @RequestMapping("/GoodsTrend")
    public String navigationHtml(Model model, HttpServletRequest request){
    	String goodId = request.getParameter(String.valueOf("goodId"));
    	int goodid = Integer.parseInt(goodId);
    	
    	GoodsDetailInfo goodBean= goodsDetailInfoDao.findOrderByGoodId(goodid).get(0);
    	String goodName = goodBean.getName();
    	String itemUrl = goodBean.getItem_url();
    	System.out.println(itemUrl);

    	List<Map<String, Object>> list = new ArrayList();
    	Map<String, Object> map = new HashMap();
    	map.put("good_id", 1);
    	map.put("name", goodName);
    	map.put("comment_num", "0");
    	map.put("good_rate", "0.0");
    	map.put("good_comment_num", "0");
    	map.put("mid_comment_num", "0");
    	map.put("bad_comment_num", "0");
    	map.put("price", "test");
    	map.put("item_url", itemUrl);
    	list.add(map);
    	
    	List<GoodsTrend> resultList = goodsTrendDao.findOrderByGoodsId(goodid);
    	List<Map<String, Object>> finalList = new ArrayList<Map<String, Object>>();
    	if(null != resultList && resultList.size() > 0){
    		Map<String, Object> tempMap = resultList.get(resultList.size() -1).toMap();
    		tempMap.put("item_url", itemUrl);
    		tempMap.put("name", goodName);
    		finalList.add(tempMap);
    		
    	}

    	model.addAttribute("prods", finalList);
    	model.addAttribute("prodas", Sphinx.searchMayLike(goodName));
    	return "GoodsTrend";
    }
    @RequestMapping("/navigate")
    public String navigateHtml(Model model, HttpServletRequest request){
    	String name = request.getParameter("name");
    	
    	List<Map<String, Object>> list = new ArrayList();
    	Map<String, Object> map = new HashMap();
    	map.put("id", 1);
    	map.put("source", "test");
    	map.put("category", "test");
    	map.put("url", "test");    	
    	list.add(map);
    	
    	//model.addAttribute("prods", list);
    	
    	List<Navigation> resultList = null;
    	if(name.equals("jd")){
    		resultList = navigationDao.findOrderBySourceName("京东");
 
    	}
    	else if(name.equals("yhd")){
    		resultList = navigationDao.findOrderBySourceName("一号店");
    		
    	}else if(name.equals("amazon")){
    		resultList = navigationDao.findOrderBySourceName("亚马逊");
    	}else if(name.equals("tmall")){
    		resultList = navigationDao.findOrderBySourceName("天猫");
    	}else{
    		System.out.println("----------------------------------------------------------------");
    	}
    	
    	if(null == resultList){
    		resultList = new ArrayList<>();
    	}
//    	else{
//    		// 做个测试  怀疑数据过大导致
//    		resultList = resultList.subList(0, 1);
  //  	}
    	
    	
    	
    	//resultList = navigationDao.findAll();
    	for(Navigation n:resultList){
    		System.out.println(n.toString());
    	}
    	
//    	Navigation v =  navigationDao.findOrderBySourceName("京东");
//    	System.out.println(v.toString());
    	model.addAttribute("prods", resultList);
    	return "navigate";
    }
    
    @RequestMapping("/GoodInfo")
    public String goodInfoHtml(Model model, HttpServletRequest request){
    	//String name = request.getParameter("name");
    	String category = request.getParameter("category");
    	//name = "%" +name+ "%";
    	//System.out.println(name);
    	System.out.println(category);
    	
    	List<Map<String, Object>> list = new ArrayList();
    	Map<String, Object> map = new HashMap();
    	map.put("id", 1);
    	map.put("name", "test");
    	map.put("price", "test"); 
    	map.put("score", "test");
    	map.put("item_url", "test");
    	list.add(map);
    	
//    	List<GoodsDetailInfo> resultList = null;
//    	if(name.equals("jd")){
//    		resultList = goodsDetailInfoDao.findOrderByUrlName(name);
// 
//    	}
//    	else if(name.equals("yhd")){
//    		resultList = goodsDetailInfoDao.findOrderByUrlName(name);
//    	
//    		
//    	}else if(name.equals("amazon")){
//    		resultList = goodsDetailInfoDao.findOrderByUrlName(name);
//    	}else if(name.equals("tmall")){
//    		resultList = goodsDetailInfoDao.findOrderByUrlName(name);
//    	}
    	//多表查询
    	List<AnalyseResult> analyselist = null;
    	if(category.equals("4")){
    		analyselist = analyseResultDao.findOrderByCategory(Integer.parseInt(category));
    	}else if(category.equals("3")){
    		analyselist = analyseResultDao.findOrderByCategory(Integer.parseInt(category));
    	}else if(category.equals("2")){
    		analyselist = analyseResultDao.findOrderByCategory(Integer.parseInt(category));
    	}else if(category.equals("1")){
    		analyselist = analyseResultDao.findOrderByCategory(Integer.parseInt(category));
    	}else if(category.equals("0")){
    		analyselist = analyseResultDao.findOrderByCategory(Integer.parseInt(category));
    	}
    	
    	
    	System.out.println(analyselist.toString());
    	List<Map<String, Object>> finalList = new ArrayList<Map<String, Object>>();
    	
    	for(AnalyseResult temp:analyselist){
    		Map<String, Object> tempMap = temp.toMap();
    		int good_id = temp.getGood_id();
    		
    		List<GoodsDetailInfo> gdList = goodsDetailInfoDao.findOrderByGoodId(good_id);
    		
    		if(null == gdList || 0 == gdList.size()){
    			break;
    		}
    		GoodsDetailInfo goodBean = gdList.get(0);
    		int id = goodBean.getId();
    		String goodname = goodBean.getName();
    		int score = goodBean.getScore();
    		String item_url = goodBean.getItem_url();
    		tempMap.put("id", id);
    		tempMap.put("name", goodname);
    		tempMap.put("score", score);
    		tempMap.put("item_url", item_url);
    		
    		
    		
    		finalList.add(tempMap);
    	}
   
    	
    	
    	model.addAttribute("prods", finalList);
    	return "GoodInfo";
	}
    
    @RequestMapping("/GoodBySphinx")
    public String goodBySphinxHtml(Model model, HttpServletRequest request){
    	//获取参数name
    	 String searchValue = request.getParameter("searcheKey");
    	 String hidden = "";
    	 String radio = "";
    	 
    	 if("1".equals(request.getParameter("flag"))){
    		 hidden = request.getParameter("flag");
    	 }
    	 
    	 if(null != request.getParameter("radio")){
    		 radio = request.getParameter("radio");
    	 }
    	 System.out.println(searchValue + hidden + radio);
    	    
    	List<Map<String, Object>> objList = Sphinx.search(searchValue, hidden, radio);
    	
    	if(objList == null || objList.size() == 0){
    		System.out.println("没有搜索到相关数据");
    	}
 
    	for(Map<String, Object> n:objList ){
    		System.out.println(JacksonUtils.getJsonString(n));
    	}
    	model.addAttribute("prods", objList);
    	model.addAttribute("searcheKey", searchValue);
    	model.addAttribute("flag", hidden);
    	model.addAttribute("radio", radio);
    	
    	return "GoodBySphinx";
    }
   
}
