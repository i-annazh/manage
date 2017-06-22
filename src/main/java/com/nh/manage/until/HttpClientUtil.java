package com.nh.manage.until;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class HttpClientUtil {
	public static final String charset = "utf-8";
	
	private static HttpClient httpClient = null;
	
	private static MultiThreadedHttpConnectionManager conn_manager = null;
	
	private static HttpConnectionManagerParams cmanager_params = null;
	
	private static HttpClientUtil instance = new HttpClientUtil();
	
	/*
	 * Http初始化连接池 
	 */
	static{
		
		if (conn_manager == null)
			conn_manager = new MultiThreadedHttpConnectionManager();
		
		if (cmanager_params == null)
			cmanager_params = new HttpConnectionManagerParams();
	
		//最大连接数
		cmanager_params.setDefaultMaxConnectionsPerHost(10);
		cmanager_params.setMaxTotalConnections(20);
				
		//设置连接超时时间(单位毫秒) 
		cmanager_params.setConnectionTimeout(3000);
		
		//设置读数据超时时间(单位毫秒)
		cmanager_params.setSoTimeout(3000);
		conn_manager.setParams(cmanager_params);
		httpClient = new HttpClient(conn_manager);
		
	}
	
	static public HttpClientUtil getInstance(){
		return instance;
	}
	
	/**
	 * 以get方式提交参数访问API
	 * @param url
	 * @param paramMap
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 * @throws Exception
	 */
	public String doGet(String url, Map<String, String> paramMap) throws HttpException, IOException{
		GetMethod method = null;
		try{
			method = new GetMethod(url);
			NameValuePair[] parametersBody = new NameValuePair[paramMap.size()];
			NameValuePair nvPair = new NameValuePair();
			Iterator<String> iterator = paramMap.keySet().iterator();
			String name = null;
			int i=0;
			while(iterator.hasNext()){
				name = iterator.next();
				nvPair = new NameValuePair(name, paramMap.get(name));
				parametersBody[i++] = nvPair;
			}
			method.setQueryString(parametersBody);
			httpClient.executeMethod(method);
			
//			String str = new String(method.getResponseBody(),charset);
			InputStream resStream = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(resStream));
			StringBuffer resBuffer = new StringBuffer();
			String lines = "";
			while((lines = br.readLine()) != null){
				lines = new String(lines.getBytes(), charset);
				resBuffer.append(lines);
			}
			String str = resBuffer.toString();
			
			return str;
			
		}finally{
			if(method != null){
				method.releaseConnection();
			}
		}
	}
	
	/**
	 * 以post方式提交参数访问API
	 * @param url
	 * @param paramMap
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public String doPost(String url, Map<String, String> paramMap) throws UnsupportedEncodingException, IOException{
		PostMethod method = null;
		try{
			method = new PostMethod(url);
			if(paramMap != null && paramMap.size() > 0){
				NameValuePair[] parametersBody = new NameValuePair[paramMap.size()];
				NameValuePair nvPair = new NameValuePair();
				Iterator<String> iterator = paramMap.keySet().iterator();
				String name = null;
				int i=0;
				while(iterator.hasNext()){
					name = iterator.next();
					nvPair = new NameValuePair(name, paramMap.get(name));
					parametersBody[i++] = nvPair;
				}
				method.setRequestBody(parametersBody);
			}
			
			httpClient.executeMethod(method);
			
//			String str = new String(postMethod.getResponseBody(),charset);
			InputStream resStream = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(resStream));
			StringBuffer resBuffer = new StringBuffer();
			String lines = "";
			while((lines = br.readLine()) != null){
				lines = new String(lines.getBytes(), charset);
				resBuffer.append(lines);
			}
			String str = resBuffer.toString();
			
			return str;
			
		}finally{
			if(method != null){
				method.releaseConnection();
			}
		}
	}	
}
