package com.web.maven.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.maven.common.pages.PageBean;
import com.web.maven.dao.po.SchoolCommunicationRecord;

public interface SchoolCommunicationRecordMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(SchoolCommunicationRecord record);

	int insertSelective(SchoolCommunicationRecord record);

	SchoolCommunicationRecord selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SchoolCommunicationRecord record);

	int updateByPrimaryKey(SchoolCommunicationRecord record);

	/**
	 * 
	 * 获取通讯录记录
	 * 
	 * @author heJiang
	 * @version 1.0
	 * @since 2014年12月7日 下午8:00:24
	 */
	List<SchoolCommunicationRecord> getSchoolPageList(@Param("pageBean")PageBean PageBean,@Param("vo") SchoolCommunicationRecord vo);

	/**
	 * 
	 * 删除通讯录记录
	 * 
	 * @author heJiang
	 * @version 1.0
	 * @since 2014年12月7日 下午8:00:24
	 */
	int delRecordBystates(Integer id);

	/**
	 * 
	 * 验证手机号码
	 * 
	 * @author heJiang
	 * @version 1.0
	 * @since 2014年12月8日 下午10:53:37
	 */
	int validatePhone(@Param("id")Integer id,@Param("phone")String phone);
}