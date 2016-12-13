package com.extend;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hyc.bean.ICCardTime;

/**
 * 获取系统当前日期，时间， 星期
 * 
 */

public class DataString {


	public static String StringData() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日 \nE \taa HH:mm");
		String str = formatter.format(curDate);
		// System.out.println(str);
		return str;
	}

	public static String StringData1() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String str = formatter.format(curDate);
		// System.out.println(str);
		return str;
	}
	public static String StringData2() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String str = formatter.format(curDate);
		// System.out.println(str);
		return str;
	}
	
	// 判断当前时间是不是在定义的打卡时间内
	public boolean isSwipingCard(List<ICCardTime> cardTimelist) {
		DateFormat df = new SimpleDateFormat("HH:mm");
		try {
			if (cardTimelist != null) {
				if (cardTimelist.size() > 0) {
					for (int i = 0; i < cardTimelist.size(); i++) {
						Date d1 = df.parse(cardTimelist.get(i).getStarttime());
						Date d2 = df.parse(cardTimelist.get(i).getEndtime());
						Date d3 = df.parse(StringData2());

						if (d3.getTime() <= d2.getTime()
								&& d3.getTime() >= d1.getTime()) {
							System.out.println("isSwipingCard=true限制刷卡时间");
							return true;
						}

					}
				}else {
					System.out.println("isSwipingCard=true没限制刷卡时间");
					return true;
				}
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("isSwipingCard=false");
		return false;
	}

}
