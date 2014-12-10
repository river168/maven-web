package com.web.maven.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.maven.dao.SchoolCommunicationRecordMapper;
import com.web.maven.dao.po.SchoolCommunicationRecord;
import com.web.maven.service.SchoolServices;

/**
 * 学校信息实现类
 * 
 * @author jiang
 * 
 */
@Service
public class SchoolServicesImpl implements SchoolServices {
	@Autowired
	private SchoolCommunicationRecordMapper schoolDao;

	public List<SchoolCommunicationRecord> getSchoolRecordList(
			SchoolCommunicationRecord vo) throws Exception {
		return schoolDao.getSchoolList(vo);
	}

	public SchoolCommunicationRecord getSchoolRecord(int id) throws Exception {
		return schoolDao.selectByPrimaryKey(id);
	}

	public int saveSchoolRecord(SchoolCommunicationRecord vo) throws Exception {
		int n = 0;
		if (vo.getId() != null) {
			n = schoolDao.updateByPrimaryKeySelective(vo);
		} else {
			n = schoolDao.insertSelective(vo);
		}
		return n;
	}

	public int delSchoolRecord(int id) throws Exception {
		return schoolDao.delRecordBystates(id);
	}

	public int validatePhone(int id, String phone) throws Exception {
		return schoolDao.validatePhone(id, phone);
	}

}
