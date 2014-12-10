
/**
 * Copyright(c) 2000-2013 HC360.COM, All Rights Reserved.
 * Project: ework 
 * Author: Gao xingkun
 * Createdate: ����3:39:30
 * Version: 1.0
 *
 */

package com.web.maven.common.pages;

import java.util.List;

/**
 * Mysql��ҳ
 * @project ework
 * @author Gao xingkun
 * @version 1.0
 * @date 2013-4-24 ����3:39:30   
 */

public class Page {

	/** ��ѯ��� */
	private List<?> lstResult;
	/** ��ҳ��ϢBean */
	private PageBean pageBean;
	
	/**
	 * (��)
	 */
	public Page() {}
	
	/**
	 * ���ݲ�ѯ�������ҳ��Ϣ����
	 * @param lstResult ��ѯ���
	 * @param pageBean ��ҳ��ϢBean
	 */
	public Page(List<?> lstResult, PageBean pageBean) {
		this.lstResult = lstResult;
		this.pageBean = pageBean;
	}
	
	/**
	 * ȡ�ò�ѯ���
	 * @return ��ѯ���
	 */
	public List<?> getLstResult() {
		return lstResult;
	}
	/**
	 * ���ò�ѯ���
	 * @param lstResult ��ѯ���
	 */
	public void setLstResult(List<?> lstResult) {
		this.lstResult = lstResult;
	}
	
	/**
	 * ȡ�÷�ҳ��ϢBean
	 * @return ��ҳ��ϢBean
	 */
	public PageBean getPageBean() {
		return pageBean;
	}
	/**
	 * ���÷�ҳ��ϢBean
	 * @param pageBean ��ҳ��ϢBean
	 */
	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}
}