package com.web.maven.web.taglibs;
/***
 * ��ҳ�������
 * @author cj
 * **/

public class PaginationPage {
	public  int pageSize =10; //ÿҳ��ʾ����
	private int totalCount = 0; //������
	private int totalPage = 0; //��ҳ��
	private static int page = 1; //��ҳ�洫������ҳ��
	private boolean hasNextPage=false;//�Ƿ�����һҳ
	private boolean hasPreviousPage=false;//�Ƿ���ǰһҳ
	public  static String pagination ="pagination"; //�����ҳ�����key������
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
	 * �����һҳ��ҳ��
	 * @return
	 */
	public int getNextPage(){
		return page + 1;
	}
	
	/**
	 * �����һҳ��ҳ��
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
	 * ȡ����ʼ��ʾ��¼��
	 * @return ��ʼ��ʾ��¼��
	 */  
	public int  getStartNo(){
		return (getPage()-1)*pageSize+1;
	}
	
	
	/**
	 * ȡ�ý�����ʾ��¼��
	 * @return ������ʾ��¼��
	 */
	public int getEndNo() {
		return Math.min(getPage() * getPageSize(), totalCount);
	}
	
	
	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}
}
