package com.mc.model;

import java.util.List;

public class TodaySalesPromotionResult {

	private List<TodaySalesPromotion> todaySalesPromotions;
	private String amount;//�ܸ���
	private String pages;//��ҳ��
	private String currentPage;//��ǰҳ��
	private int size;//ÿҳ��ʾ����
	
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
