package com.mc.model;

import java.util.List;

public class TodaySalesPromotionResult {

	private List<TodaySalesPromotion> todaySalesPromotions;
	private String amount;//总个数
	private String pages;//总页数
	private String currentPage;//当前页数
	private int size;//每页显示个数
	
	public List<TodaySalesPromotion> getTodaySalesPromotions() {
		return todaySalesPromotions;
	}
	public void setTodaySalesPromotions(
			List<TodaySalesPromotion> todaySalesPromotions) {
		this.todaySalesPromotions = todaySalesPromotions;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		pages = pages;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int i) {
		this.size = i;
	}
	
}
