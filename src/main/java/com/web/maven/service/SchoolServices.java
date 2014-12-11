package com.web.maven.service;

import java.util.List;

import com.web.maven.common.pages.PageBean;
import com.web.maven.dao.po.SchoolCommunicationRecord;

/**
 * ѧУ��Ϣ�ӿ���
 * 
 * @author jiang
 *
 */
public interface SchoolServices {
	/**  
	*   
	* ��ѯlist 
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014��12��7�� ����8:02:50  
	*/ 
	public List<SchoolCommunicationRecord> getSchoolRecordList(PageBean PageBean,SchoolCommunicationRecord vo) throws Exception;
	
	/**  
	*   
	* ��ѯĳ����¼
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014��12��7�� ����11:53:20  
	*/ 
	public SchoolCommunicationRecord getSchoolRecord(int id)throws Exception;
	
	/**  
	*   
	* �����¼  
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014��12��7�� ����11:53:58  
	*/ 
	public int  saveSchoolRecord(SchoolCommunicationRecord vo)throws Exception;
	
	/**  
	*   
	* ɾ����¼  
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014��12��7�� ����11:53:58  
	*/ 
	public int  delSchoolRecord(int id)throws Exception;
	
	/**  
	*   
	* ��֤�ֻ����� 
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014��12��8�� ����10:52:47  
	*/ 
	public int validatePhone(int id,String phone) throws Exception;
		
}
