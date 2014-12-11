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
	 * 通信录页面
	 * 
	 * @author heJiang
	 * @version 1.0
	 * @since 2014年12月7日 下午6:55:32
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
			log.error("打开通讯录页面失败，" + e);
		}
		map.put("pageBean", pageBean);
		map.put("recordList", list);
		return "school/record";
	}

	/**
	 * 
	 * 查询某条记录
	 * 
	 * @author heJiang
	 * @version 1.0
	 * @since 2014年12月7日 下午11:48:20
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
				log.error("查询通讯录页面失败，" + e);
			}
		} else {
			log.error("查询通讯录页面失败，参数获取失败");
		}
		return JSONSerializer.toJSON(vo).toString();
	}

	/**
	 * 
	 * 保存某条记录
	 * 
	 * @author heJiang
	 * @version 1.0
	 * @since 2014年12月7日 下午11:48:47
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
			log.error("保存通讯录页面失败," + e);
		}
		return "redirect:record";
	}

	/**
	 * 
	 * 删除某条记录
	 * 
	 * @author heJiang
	 * @version 1.0
	 * @since 2014年12月8日 上午12:00:52
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
				log.error("删除通讯录页面失败," + e);
			}
		} else {
			log.error("删除通讯录页面失败,获取消息失败");
		}
		return "redirect:record";
	}
	
	/**  
	*   
	* 验证手机号码 
	*   
	* @author heJiang
	* @version 1.0  
	* @since 2014年12月8日 下午10:51:05  
	*/ 
	@ResponseBody
	@RequestMapping("/validatePhone")
	public String validatePhone(ModelMap map, String id,String phone){
		int n=0;
		try {
			n=schoolServices.validatePhone(Integer.parseInt(id), phone);
		} catch (Exception e) {
			log.error("验证失败，"+e);
		}
		return ""+n;
	}
}
