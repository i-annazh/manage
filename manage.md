#客户端程序manage系统代码说明
##系统功能介绍
客户端程序manage是用户访问本程序的入口；这部分内容基于B/S结构，采用Thymeleaf和bootstrap两个界面框架进行设计，总体风格简洁美观。

###1.商品个性推荐
商品个性推荐主要是根据用户间相似的行为做推荐，主要实现步骤如下：


- 种入cookie：当用户注册或登入时系统将用户输入的手机号码植入到该用户的浏览器cookie中。如果用户不清除浏览记录，则系统cookie时会一直存在。如果用户清空了浏览记录，则下次登入后会继续推荐。
- 记录用户浏览行为：用户关注或者取消关注某商品，都会存入数据库作为用户浏览行为的关键信息。
- 用户浏览行为转换为文件：系统使用mahout算法来计算用户行为相似度，mahout是一种相似度算法，一般用于处理分析大数据，能处理上亿级数据量，但算法的输入数据要求是文本或excel格式文件；因此需要将数据从数据库中提取并转换为文本格式。
- 推荐商品：这里使用mahout的余弦相似度算法UncenteredCosineSimilarity来计算用户的行为相似度[10] ，形成推荐表并把推荐结果写回数据库。比如有用户A、B，A用户关注了商品1、2、3、5、8，B用户关注了商品1、2、3.此时算法会认为A用户和B用户是同一类用户，并把A浏览过而B未浏览过的商品5、8推荐给B。当然，这只是两个数据，当数据量很大时，意味着A会与其他所有用户进行行为相似比较并返回相似度最高的推荐值。
- 个性推荐结果页面呈现：此时，用户可以查看系统推荐的商品并对推荐的商品加关注或者不关注。

###2.商品广泛推荐
商品广泛推荐的应用场景在于当用户已经在查看商品具体的评价信息时，我们根据该款商品的名称去搜索引擎中查找最相似的三条数据，显示在该页面下方，用户如果感兴趣，点击链接便可以直接看到该商品的详细信息。

实现代码如下：

 ` String sql = "select * from goods_detail_info where match('@rtf_name \""+ key +"\"/1') limit 1,3"; ` 

###3.热销商品预测
系统每天爬取一次商品的信息，如果商品的价格或者总评数没有变化，则数据不做修改，如果价格有变化或总评数增加系统会将新的数据存入数据库。热销商品预测功能是根据以往的销量和价格来预测商品的热销程度并依照计算的结果对商品进行排序。预测算法主要根据价格、总评数和时间三个维度来预测某商品的热销程度；如果商品价格下降或总评数增加，并且时间间隔越近，所得的权重越大，得到的结果越大说明该商品越热销。

具体代码实现如下：

	//用数据库中最后更新时间/当前时间：时间越近权重越大
	rate = (double)time/(double)timenow;
	//评论数比之前多，总分加1
	if(comment_numnow > comment_numbef){
		tempscore = tempscore + 1;
	}
	//价格比之前低，总分加2
	if(price_numnow < price_numbef){
		tempscore = tempscore + 2;
	}
	//计算总热销度
	score = score + tempscore*rate;

###4.商品聚类分析
商品聚类分析主要用k-means聚类算法给所有商品分类。程序中把总评数、好评率、好评数、中评数、差评数和价格当聚类指标。经过聚类算法分析后得到某一类：比如是好评数、好评率、中评数、差评数都很高而价格比较低的这一类商品供有这方面需求的用户搜索。聚类k值表示聚成几类，k必须大于等于2[11]。因为聚成一类毫无意义。K的取值取决于聚类的均方误差，当均误差趋于稳定时聚类结果最优。

###5.按品牌名字搜索
系统最基础最不可或缺的功能就是按名字搜索。搜索功能是所有系统必须具备的功能；搜索可以提高系统的效率，节约时间成本。本系统提供的搜索功能都基于中文搜索引擎sphinx/coreseek实现，使系统可以提供准确、高效、便捷的搜索功能。比如用户在搜索框输入电视机，所有系统的电视机都会显示在列表中供用户查看。系统还会自动分词，以提供最准确的搜索答案。

###6.数据爬取交互过程
当用户点击对某个url的爬取命令时，客户端程序manage会向爬虫程序crawler发请求，crawler接收到请求后开始启动一个线程任务，判断数据源是否来自亚马逊，如果是，则先更新类目再进行列表页和详情页的爬取。如果不是，则会直接进行列表页和详情页数据的爬取，并保存至数据库。期间，线程会返回一个状态码给manage程序，告知命令是否下发成功。

###7.搜索引擎程序配置
写到这里，我们的主要内容是如何配置搜索引擎。但是为什么要用搜索引擎呢？如果只是为了按名字品牌搜索的话为什么不用SQL语句中的select命令直接查询呢？那么究竟搜索引擎与select命令有啥区别呢？这是我们首先要解决的问题。

经过比较我们发现，数据库属于顺序查找，如要依据条件精准查找或者模糊查询，都会一条条记录从头到尾依次查找，这就是 select命令查找。但数据库依此方法查询关键字的速度慢，效率低，难以满足大规模并发量的查询。此次毕设，我们在用户与数据源（例如mysql）之间加入搜索引擎。引擎属于索引查找，就是把非结构化的数据中的内容提取出来一部分重新组织，让它变的有结构化，这部分我们提取出来的数据就叫做“索引”；用户在查询的时候能够依据索引快速锁定数据的位置，从而提高检索速度。此外引擎具有扩展分词功能，这样能推荐给用户最想要的搜索结果。
####7.1Sphinx配置
- sphinx数据源配置

	>sphinx数据源配置主要配置数据来源的一些基本信息，比如数据库类型、服务器ip、用户、密码和端口等；也能配置一些复杂的增删改查操作。
- sphinx对应的索引配置
	
	>sphinx上的某个需要被搜索的表可以和数据库中的表保持一致，也可以不同。在该表上建立索引字段只要在字段前加上rt_开头就行。接下来就是配置一些数据库来源信息、端口号、日志存放路径、并发查询数和连接超时时间等信息。
	
####7.2中文分词Coreseek配置
中文分词配置主要使用在用户输入商品进行搜索的过程中。如果用户输入“苹果手机”，分词算法会做分词处理分成“苹果”、“手机”和“苹果手机”三个匹配。搜索引擎会将最终的搜索结果按搜索量进行排序并返回给用户。

##系统代码介绍
###界面控制爬取代码介绍

 	`public class AjaxController {
	
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
	  ` 
###商品个性推荐代码介绍
	 `public class Recommend {
	
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
			// 2,用余弦相似度计算相似度
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

	}  ` 
###热销商品预测代码介绍
 	`class Bean{
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
	  ` 