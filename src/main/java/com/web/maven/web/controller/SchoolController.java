package com.web.maven.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.maven.common.pages.PageBean;
import com.web.maven.dao.po.SchoolCommunicationRecord;
import com.web.maven.service.SchoolServices;

@Controller
@RequestMapping("/school")
public class SchoolController {
	@Autowired
	private SchoolServices schoolServices;
	Logger log = Logger.getLogger("SchoolController");

	/**
	 * 
	 * ͨ��¼ҳ��
	 * 
	 * @author heJiang
	 * @version 1.0
	 * @since 2014��12��7�� ����6:55:32
	 */
	@RequestMapping("/record")
	public String getSchoolRecord(ModelMap map, SchoolCommunicationRecord vo,PageBean pageBean) {
		List<SchoolCommunicationRecord> list = null;
		try {
			if(pageBean==null){
				pageBean=new PageBean();
			}
			list = schoolServices.getSchoolRecordList(pageBean,vo);
		} catch (Exception e) {
			log.error("��ͨѶ¼ҳ��ʧ�ܣ�" + e);
		}
		map.put("pageBean", pageBean);
		map.put("recordList", list);
		return "school/record";
	}

	/**
	 * 
	 * ��ѯĳ����¼
	 * 
	 * @author heJiang
	 * @version 1.0
	 * @since 2014��12��7�� ����11:48:20
	 */
	@ResponseBody
	@RequestMapping(value ="/getRecord",method = RequestMethod.GET,produces= "text/plain;charset=GBK")
	public String getRecord(ModelMap map,HttpServletRequest request) {
		SchoolCommunicationRecord vo = null;
		String id=request.getParameter("id");
		if (id != null && !"".equals(id.trim())) {
			try {
				vo = schoolServices.getSchoolRecord(Integer.parseInt(id));
			} catch (Exception e) {
				log.error("��ѯͨѶ¼ҳ��ʧ�ܣ�" + e);
			}
		} else {
			log.error("��ѯͨѶ¼ҳ��ʧ�ܣ�������ȡʧ��");
		}
		return JSONSerializer.toJSON(vo).toString();
	}

	/**
	 * 
	 * ����ĳ����¼
	 * 
	 * @author heJiang
	 * @version 1.0
	 * @since 2014��12��7�� ����11:48:47
	 */
	@RequestMapping("/saveRecord")
	public String saveRecord(ModelMap map, SchoolCommunicationRecord vo) {
		try {
			vo.setPubdate(new Date());
			int n = schoolServices.saveSchoolRecord(vo);
			if (n > 0) {
				map.put("flag", "ok");
			}
		} catch (Exception e) {
			log.error("����ͨѶ¼ҳ��ʧ��," + e);
		}
		return "redirect:record";
	}

	/**
	 * 
	 * ɾ��ĳ����¼
	 * 
	 * @author heJiang
	 * @version 1.0
	 * @since 2014��12��8�� ����12:00:52
	 */
	@RequestMapping("/delRecord")
	public String deleteRecord(ModelMap map, String id) {
		if (id != null && !"".equals(id.trim())) {
			try {
				int n = schoolServices.delSchoolRecord(Integer.parseInt(id));
				if (n > 0) {
					map.put("flag", "ok");
				}
			} catch (Exception e) {
				log.error("ɾ��ͨѶ¼ҳ��ʧ��," + e);
			}
		} else {
			log.error("ɾ��ͨѶ¼ҳ��ʧ��,��ȡ��Ϣʧ��");
		}
		return "redirect:record";
	}
	
	/**  
	*   
	* ��֤�ֻ����� 
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014��12��8�� ����10:51:05  
	*/ 
	@ResponseBody
	@RequestMapping("/validatePhone")
	public String validatePhone(ModelMap map, String id,String phone){
		int n=0;
		try {
			n=schoolServices.validatePhone(Integer.parseInt(id), phone);
		} catch (Exception e) {
			log.error("��֤ʧ�ܣ�"+e);
		}
		return ""+n;
	}
}
