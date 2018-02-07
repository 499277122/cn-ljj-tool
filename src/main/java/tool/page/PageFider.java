package tool.page;

import java.io.Serializable;
import java.util.List;

public class PageFider<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	// 第几页
	public int number;
	// 每页多少条
	public int size;
	// 总共多少页
	public int totalPages;
	// 总共多少条
	public int totalElements;

	public List<T> content;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	@Override
	public String toString() {
		return "PageFider [number=" + number + ", size=" + size + ", totalPages=" + totalPages + ", totalElements="
				+ totalElements + ", content=" + content + "]";
	}

}