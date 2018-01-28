package com.dsw.filetrans.query.condition;

import java.util.ArrayList;
import java.util.List;

import com.dsw.filetrans.query.Order;
import com.dsw.filetrans.query.OrderType;


/**
 * 定义查询条件基类
 */
public class BaseCondition {

    /** 是否分页，默认不分页 */
    private boolean isPaged = false;

    /** 每页记录条数，默认每页20条 */
    private int pageSize = 20;

    /** 当前页，页号从1开始 */
    private int currPage = 1;

    /** 最大页数 */
    private int maxPages = 1;

    /** 是否查询总页数，默认为查询, 该项只有在分页的情况下有效 */
    private boolean isQueryTotalPages = false;
    
    /** 排序字段 */
    private List<Order> orders = new ArrayList<Order>();
    
    /** 包含的查询字段，当 includeFields == null 或 includeFields.length < 1时返回全部字段 */
    private String[] includeFields;
    
    /** 搜索关键字, 当不为空时，查询solr，否则查询数据库 */
    private String searchKeyword;
    
    public Order[] getOrders() {
		return orders.toArray(new Order[0]);
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public void addOrder(String name, OrderType type) {
        addOrder(new Order(name, type));
    }
    
    public void addOrder(Order order) {
        orders.add(order);
    }
    
    public boolean isPaged() {
        return isPaged;
    }

    public void setPaged(boolean isPaged) {
        this.isPaged = isPaged;
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

    public boolean isQueryTotalPages() {
        return isQueryTotalPages;
    }

    public void setQueryTotalPages(boolean isQueryTotalPages) {
        this.isQueryTotalPages = isQueryTotalPages;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    public String[] getIncludeFields() {
        return includeFields;
    }

    public void setIncludeFields(String[] includeFields) {
        this.includeFields = includeFields;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
}
