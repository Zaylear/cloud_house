package com.rbi.admin.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageData<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	private int pageNo;
	private int pageSize;
	private int totalPage;
	private int totalRecord;
	private List<T> contents = new ArrayList<T>();

	public PageData() {
	}

	public PageData(int pageNo, int pageSize, int totalPage, int totalRecord, List<T> contents) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.totalRecord = totalRecord;
		this.contents = contents;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public List<T> getContents() {
		return contents;
	}

	public void setContents(ArrayList<T> contents) {
		this.contents = contents;
	}

	public void setContents(List<T> contents) {
		this.contents = contents;
	}
	

}
