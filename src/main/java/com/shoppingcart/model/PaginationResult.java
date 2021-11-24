package com.shoppingcart.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

public class PaginationResult<E> {
	
	private int totalRecords;
	
	private int currentPage;
	
	private List<E> list;
	
	private int maxResult;
	
	private int totalPages;
	
	private int maxNavigationPage;
	
	private List<Integer> navigationPages;

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getMaxNavigationPage() {
		return maxNavigationPage;
	}

	public void setMaxNavigationPage(int maxNavigationPage) {
		this.maxNavigationPage = maxNavigationPage;
	}

	public List<Integer> getNavigationPages() {
		return navigationPages;
	}

	public void setNavigationPages(List<Integer> navigationPages) {
		this.navigationPages = navigationPages;
	}
	
	public PaginationResult(Query<?> query, int page, int maxResult, int maxNavigationPage) {
		int pageIndex = page - 1 < 0 ? 0 : page - 1;
		int fromRecordIndex = pageIndex * maxResult;
		int maxRecordIndex = fromRecordIndex + maxResult;
		
		ScrollableResults resultScroll = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
		
		List<E> results = new ArrayList<E>();//= List<ProductInfo> results = new ArrayList<ProductInfo>();
		
		boolean hasResult = resultScroll.first();
		
		if(hasResult) {
			
			hasResult = resultScroll.scroll(fromRecordIndex);
			if(hasResult) {
				do {
					E record = (E) resultScroll.get(0);
					results.add(record);
					
				}while(resultScroll.next() // có nhiệm vụ kiểm tra phần tử kế tiếp có tồn tại hay ko, nếu có trỏ tới phần tử kế tiếp
						&& resultScroll.getRowNumber() >= fromRecordIndex
						&& resultScroll.getRowNumber() < maxRecordIndex);
			}
			
			resultScroll.last();
		}
		
		this.totalRecords = resultScroll.getRowNumber() + 1;
		this.currentPage = pageIndex + 1;
		this.list = results;
		this.maxResult = maxResult;
		
		this.totalPages = (this.totalPages / this.maxResult) + 1;
		this.maxNavigationPage = maxNavigationPage;
		
		if(maxNavigationPage < this.totalPages) {
			this.maxNavigationPage = maxNavigationPage;
		}
		
		this.getNavigationPages();
	}
	
	private void calcNavigationPages() {
		this.navigationPages = new ArrayList<Integer>();
		
		int current = this.currentPage > this.totalPages ? this.totalPages : this.currentPage;
		
		int begin = current - this.maxNavigationPage / 2;
		int end = current + this.maxNavigationPage / 2;
		
		//Trang đầu tiên
		this.navigationPages.add(1);
		if(begin > 2) {
			
			this.navigationPages.add(-1);
		}
		
		for(int i = begin; i < end; i++) {
			if(i > 1 && i < this.totalPages) {
				this.navigationPages.add(i);
			}
		}
		
		if(end < this.totalPages - 2) {
			//Dùng cho
			this.navigationPages.add(-1);
		}
		//Trang cuối cùng
		this.navigationPages.add(this.totalPages);
		
	}

}
