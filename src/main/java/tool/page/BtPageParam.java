package tool.page;

import java.io.Serializable;

/**
 * bootstrap-table分页参数
 * 
 * @author yi.wang
 * @date 2016年11月11日
 */
public class BtPageParam implements Serializable {

	private static final long serialVersionUID = 1L;

	// 多少条开始
	private int offset = 0;

	// 每页显示行
	private int limit = 10;

	// 排序字段
	private String sort;

	// 排序规则asc/desc
	private String order;

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "BtPageParam [offset=" + offset + ", limit=" + limit + ", sort=" + sort + ", order=" + order + "]";
	}
}
