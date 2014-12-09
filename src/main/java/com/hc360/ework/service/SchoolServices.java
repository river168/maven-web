package com.hc360.ework.service;

import java.util.List;

import com.hc360.ework.dao.po.SchoolCommunicationRecord;

/**
 * 学校信息接口类
 * 
 * @author jiang
 *
 */
public interface SchoolServices {
	/**  
	*   
	* 查询list 
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014年12月7日 下午8:02:50  
	*/ 
	public List<SchoolCommunicationRecord> getSchoolRecordList(SchoolCommunicationRecord vo) throws Exception;
	
	/**  
	*   
	* 查询某条记录
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014年12月7日 下午11:53:20  
	*/ 
	public SchoolCommunicationRecord getSchoolRecord(int id)throws Exception;
	
	/**  
	*   
	* 保存记录  
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014年12月7日 下午11:53:58  
	*/ 
	public int  saveSchoolRecord(SchoolCommunicationRecord vo)throws Exception;
	
	/**  
	*   
	* 删除记录  
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014年12月7日 下午11:53:58  
	*/ 
	public int  delSchoolRecord(int id)throws Exception;
	
	/**  
	*   
	* 验证手机号码 
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014年12月8日 下午10:52:47  
	*/ 
	public int validatePhone(int id,String phone) throws Exception;
		
}
