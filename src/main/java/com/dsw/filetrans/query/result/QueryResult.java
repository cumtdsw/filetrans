package com.dsw.filetrans.query.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询结果
 * 
 */
public class QueryResult {

	/** 是否分页 */
	private boolean isPaged = false;

	/** 每页记录条数 */
	private int pageSize = 0;

	/** 当前页，页号从1开始 */
	private int currPage = 1;

	/** 总页数 */
	private int totalPages = 1;

	/** 起始页 */
	private int startPage;

	/** 终止页 */
	private int endPage;

	/** 最大页数 */
	private int maxPages;

	/** 符合条件总个数 */
	private long totalCount;

	/** 查询结果 */
	private List<Object> results = new ArrayList<Object>();

	public boolean getIsPaged() {
		return isPaged;
	}

	public void setIsPaged(boolean isPages) {
		this.isPaged = isPages;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getMaxPages() {
		return maxPages;
	}

	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<Object> getResults() {
		return results;
	}

	public void setResults(List<Object> results) {
		this.results = results;
	}

	public void addResult(Object result) {
		this.results.add(result);
	}
}
