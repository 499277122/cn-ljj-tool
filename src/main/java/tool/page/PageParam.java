package tool.page;

import java.io.Serializable;

public class PageParam implements Serializable {

	private static final long serialVersionUID = 1L;

	// 页码，起始值 1
	private int page = 1;

	// 每页显示行
	private int rows = 10;

	// 排序字段
	private String sort;

	// 排序规则asc/desc
	private String order;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

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

	@Override
	public String toString() {
		return "PageParam [page=" + page + ", rows=" + rows + ", sort=" + sort + ", order=" + order + "]";
	}
}
