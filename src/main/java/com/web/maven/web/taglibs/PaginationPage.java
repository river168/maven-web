package com.web.maven.web.taglibs;
/***
 * 分页公共组件
 * @author cj
 * **/

public class PaginationPage {
	public  int pageSize =10; //每页显示行数
	private int totalCount = 0; //总行数
	private int totalPage = 0; //总页数
	private static int page = 1; //从页面传过来的页数
	private boolean hasNextPage=false;//是否有下一页
	private boolean hasPreviousPage=false;//是否有前一页
	public  static String pagination ="pagination"; //保存分页对象的key的名称
	public  static String jumpPage ="jumpPage"; 



	/**
	 * @return Returns the currPage.
	 */
	public static int getCurrPage() {
		return page;
	}

	/**
	 * @return Returns the totalPage.
	 */
	public int getTotalPage() {
		totalPage = totalCount/pageSize;
		if(totalCount%pageSize > 0 || totalPage == 0){
			totalPage += 1;
		}
		return totalPage;
	}



	public  int getPage() {
		return page;
	}

	public  void setPage(String str) {
		if(str != null && str.length() > 0){	
			PaginationPage.page = Integer.parseInt(str);
		}else{
			PaginationPage.page = 1;
		}
		
	}

	/**
	 * @return Returns the totalCount.
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount The totalCount to set.
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return Returns the hasNextPage.
	 */
	public boolean isHasNextPage() {
		if(getCurrPage() >= getTotalPage()){
			hasNextPage = false;
		}else{
			hasNextPage = true;
		}
		return hasNextPage;
	}

	/**
	 * @return Returns the hasPreviousPage.
	 */
	public boolean isHasPreviousPage() {
		if((getCurrPage() -1)>0) {
            hasPreviousPage=true;
        }else{
            hasPreviousPage=false;
        }
		return hasPreviousPage;
	}
	
	/**
	 * 获得下一页的页数
	 * @return
	 */
	public int getNextPage(){
		return page + 1;
	}
	
	/**
	 * 获得上一页的页数
	 * @return
	 */
	public int getPreviousPage(){
		return page - 1;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	/**
	 * 取得起始显示记录号
	 * @return 起始显示记录号
	 */  
	public int  getStartNo(){
		return (getPage()-1)*pageSize+1;
	}
	
	
	/**
	 * 取得结束显示记录号
	 * @return 结束显示记录号
	 */
	public int getEndNo() {
		return Math.min(getPage() * getPageSize(), totalCount);
	}
	
	
	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}
}
