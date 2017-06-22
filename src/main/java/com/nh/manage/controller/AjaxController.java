package com.nh.manage.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.httpclient.HttpException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nh.manage.entity.UserTaste;
import com.nh.manage.model.UserTasteDao;
import com.nh.manage.tool.GoodsForecast;
import com.nh.manage.tool.Recommend;
import com.nh.manage.until.HttpClientUtil;
import com.nh.manage.until.JacksonUtils;
import com.nh.manage.until.Sphinx;


@RestController
@RequestMapping("/ajax")
public class AjaxController {
	
	@Resource
	UserTasteDao userTasteDao;
	
	@RequestMapping(value = "/sendupdate",method = RequestMethod.GET)
    public String sendUpdateUrl(HttpServletRequest request) {
		
		//这里我们收到url 像 crawler下发爬取命令 
		String url = request.getParameter("url");
		String source = request.getParameter("source");
		String category = request.getParameter("category");
		
		System.out.println(url);
		try {
			//调用http请求让 craw收到命令执行爬取
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("act", "1");
			paramMap.put("source", java.net.URLEncoder.encode(source, "utf-8"));
			paramMap.put("url", java.net.URLEncoder.encode(url, "utf-8"));
			paramMap.put("category", java.net.URLEncoder.encode(category, "utf-8"));
		
		
			HttpClientUtil.getInstance().doGet("http://127.0.0.1:5000/common", paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String,String> map  = new HashMap<String, String>();
		map.put("status", "0");
		map.put("result", "ok");
		
        return JacksonUtils.getJsonString(map);
	}
	

	@RequestMapping(value = "/push",method = RequestMethod.GET)
    public String push(HttpServletRequest request) {
		
		Sphinx.allPush();
		
		Map<String,String> map  = new HashMap<String, String>();
		map.put("status", "0");
		map.put("result", "ok");
		
        return JacksonUtils.getJsonString(map);
	}
	//离线调用mahout计算相似度来个性推荐
	@RequestMapping(value = "/mahoutCalc",method = RequestMethod.GET)
    public String mahoutCalc(HttpServletRequest request) {
		
		
		Recommend.dateFoFile();
		Recommend.recommendGoods();
		
		Map<String,String> map  = new HashMap<String, String>();
		map.put("status", "0");
		map.put("result", "ok");
		
        return JacksonUtils.getJsonString(map);
	}
	
	@RequestMapping(value = "/calc",method = RequestMethod.GET)
    public String calc(HttpServletRequest request) {
		
		GoodsForecast.process();
		
		Map<String,String> map  = new HashMap<String, String>();
		map.put("status", "0");
		map.put("result", "ok");
		
        return JacksonUtils.getJsonString(map);
	}
	
	@Transactional
	@RequestMapping(value = "/taste",method = RequestMethod.GET)
    public String taste(HttpServletRequest request) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(currentTime);
		System.out.println(dateString);
		
		String tel = null;
		Map<String,String> map  = new HashMap<String, String>();
		Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        if(null!=cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        
        if(cookieMap.containsKey("tel")){
            Cookie cookie = (Cookie)cookieMap.get("tel");
            tel = cookie.getValue();
            System.out.println(cookie.getValue());
           
        }else{
        	map.put("status", "1000");
        	return JacksonUtils.getJsonString(map);
        }

		String act = request.getParameter("act");
		String good_ids = request.getParameter("goodid");
		
		System.out.println(act+" "+good_ids+" "+tel );
		
		UserTaste userBean = userTasteDao.findByTel(tel).get(0);
		String oldGoodIds=userBean.getGoodIds();
		System.out.println(oldGoodIds);
		
		if(act.equals("add")){
			
			if(oldGoodIds.contains(good_ids)){
				// 重复关注
				map.put("status", "1001");
				map.put("error", "重复关注");
				return JacksonUtils.getJsonString(map);
			}
			good_ids = oldGoodIds+" "+good_ids;
			System.out.println("good_ids = " + good_ids);
			System.out.println("tel = " + tel);
			
			
			userTasteDao.updateGoodIds(good_ids,Integer.parseInt(dateString), tel);
			map.put("status", "0");
			map.put("result", "ok");
		}else if(act.equals("del")){
			if(!oldGoodIds.contains(good_ids)){
				//您还未关注
				map.put("status", "1002");
				map.put("error", "您还未关注，请先关注");
				return JacksonUtils.getJsonString(map);
			}else{
				good_ids = oldGoodIds.replace(good_ids, "");
				good_ids = good_ids.replace("  ", " ").trim();

				userTasteDao.updateGoodIds(good_ids,Integer.parseInt(dateString), tel);
				map.put("status", "0");
				map.put("result", "ok");
				
			}
			
		}
	
        return JacksonUtils.getJsonString(map);
	}
	
	@RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(currentTime);
		System.out.println(dateString);
		
		String tel = request.getParameter("tel");
		System.out.println(tel);
		Cookie cookie = new Cookie("tel",tel);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*30*3);
        
        response.addCookie(cookie);

        UserTaste userTaste = new UserTaste();
        
        userTaste.setTel(tel);
        userTaste.setGoodIds("");
        userTaste.setMac("");
        userTaste.setCreate_time(Integer.parseInt(dateString));
        userTaste.setUpdate_time(Integer.parseInt(dateString));
        userTasteDao.save(userTaste);
        
        
		Map<String,String> map  = new HashMap<String, String>();
		
		map.put("status", "0");
		map.put("result", "ok");
	
        return JacksonUtils.getJsonString(map);
	}

}
