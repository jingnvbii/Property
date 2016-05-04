package com.ctrl.forum.utils;

import com.ctrl.forum.entity.CycleVpEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoUtil {
	/**
	 * 返回demo列表数据
	 * @return
	 */
	public static List<Map<String,Object>> demoData()
	{
		List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
		for (int i = 0; i < 20; i++) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("name","EasyTools简单工具包"+i);
			map.put("imgurl", "http://i12.tietuku.com/5c3c305ea1262ae7.jpg");
			data.add(map);
		}
		return data;
	}
	public static  String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
			"http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
			"http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
			"http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
			"http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};
	/**
	 * 返回广告数据
	 * @return
	 */
	public static List<CycleVpEntity> cycData()
	{
		List<CycleVpEntity> list=new ArrayList<CycleVpEntity>();
		for (int i = 0; i<imageUrls.length; i++) {
			CycleVpEntity cyc=new CycleVpEntity();
			cyc.setIurl(imageUrls[i]);
			cyc.setCurl("www.baidu.com");
			cyc.setTitle("奇怪的标题");
			list.add(cyc);
		}
		return list;
	}
	//public static List<String> 
	

}
