package com.web.maven.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomController {
	/** 
	 * 首页地址 ---------
	 * @author hejiang
	 * @version 1.0
	 * @date 2014年11月14日 下午6:12:48
	 * @param request
	 * @return
	 * @return String
	 */
	@RequestMapping("/index")
   public String index(HttpServletRequest request){
	   return "index";
   }
}
