package com.mc.model;

public class TodaySalesPromotion {

	private String goods;
	private String price;
	private String item_no;
	private String nowPrice;
	
	public String getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getItem_no() {
		return item_no;
	}
	public void setItem_no(String itemNo) {
		item_no = itemNo;
	}
	
}
