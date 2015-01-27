package com.qzsitu.stourway.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.qzsitu.stourway.domain.User;
import com.qzsitu.stourway.exception.StourWAYException;
import com.qzsitu.stourway.service.AccountService;
import com.qzsitu.stourway.util.LunarCalendar;
import com.qzsitu.stourway.xmod.system.XModResponse;
import com.qzsitu.stourway.xmod.system.XModService;


@Controller
public class MainController {
	@Autowired 
	private ServletContext servletContext;
	@Autowired
	private AccountService accountService;
	//首页
	@RequestMapping(value="/")
	public String getIndex(HttpServletRequest request, Model model, HttpSession session) {
		String today = "";
		today += "今天：" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		today += " 农历： " + new LunarCalendar().toString();
		model.addAttribute("today", today);
		
		if(session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			model.addAttribute("userName", user.getName());
			return "index";
		}
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		if(id == null)
			return "login";
		
		User user = accountService.checkUser(id, password);
		if(user != null) {
			session.setAttribute("user", user);
			model.addAttribute("userName", user.getName());
			return "index";
		} else {
			model.addAttribute("msg","用户名或密码错误，请重新输入");
			return "login";
		}
	}
	//退出系统
	@RequestMapping(value="/logout")
	public String getLogout(HttpServletRequest request, Model model, HttpSession session) {
		session.setAttribute("user", null);
		return "login";
	}

	
	@RequestMapping(value="/chPassword")
	public String chPassword(HttpServletRequest request, Model model){
		return "chPassword";
	}
	
	@RequestMapping(value = "/changePassword")
	@ResponseBody
	public String changePassword(HttpServletRequest request, Model model,
			HttpSession session) {
		User user = (User) session.getAttribute("user");
		String id = user.getId();
		String password = request.getParameter("password");
		String newpassword;
		if (request.getParameter("password1") == null) {
			return "3";
		}
		if (request.getParameter("password1").equals(
				request.getParameter("password2"))) {
			newpassword = request.getParameter("password1");
		} else {
			return "1";
		}
		user = accountService.checkUser(id, password);
		if (user != null) {
			accountService.resetPassword(id, newpassword);
			return "0";
		} else {
			return "2";
		}
	}
	
	
	@RequestMapping(value="/xmod/{modName}/{function}")
	public String xmodPage(HttpServletRequest request, @PathVariable String modName, @PathVariable String function,
			Model model, HttpSession session) throws StourWAYException {
		User user = (User) session.getAttribute("user");
		
		ApplicationContext ctx=WebApplicationContextUtils.getWebApplicationContext(servletContext);
		modName = modName.substring(0, 1).toLowerCase() + modName.substring(1, modName.length());
		Object xmodService = (Object)ctx.getBean(modName + "Service");
		
		XModResponse rs = new XModResponse();
		try {
			Method modMethod = xmodService.getClass().getMethod(function, new Class[]{HttpServletRequest.class, User.class});
			modMethod.setAccessible(true);
			rs = (XModResponse) modMethod.invoke(xmodService, new Object[] {request, user});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAllAttributes(rs.getAttributtes());
		return "/xmod/" + modName + "/" + rs.getPageReturn();
	}
	
	@RequestMapping(value="/xmod/ajax/{modName}/{function}")
	@ResponseBody
	public Object xmodAjax(HttpServletRequest request, @PathVariable String modName, @PathVariable String function, 
			Model model, HttpSession session)
			 throws StourWAYException {
		User user = (User) session.getAttribute("user");

		ApplicationContext ctx=WebApplicationContextUtils.getWebApplicationContext(servletContext);
		modName = modName.substring(0, 1).toLowerCase() + modName.substring(1, modName.length());
		XModService xmodService = (XModService)ctx.getBean(modName + "Service");
		
		XModResponse rs = new XModResponse();
		try {
			Method modMethod = xmodService.getClass().getMethod(function, new Class[]{HttpServletRequest.class, User.class});
			modMethod.setAccessible(true);
			rs = (XModResponse)modMethod.invoke(xmodService, new Object[] {request, user});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs.getAjaxReturn();
	}
	
	//没有权限异常跳转
	@RequestMapping(value="/exception/noPermission")
	public String noPermission(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(200);
		return "exception/noPermission";  
	}
	//登陆失效跳转
	@RequestMapping(value="/exception/noSession")
	public String noSession(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(200);
		return "exception/noSession";  
	}
	
	//Ajax没有权限异常跳转
	@RequestMapping(value="/exception/ajaxNoPermission")
	public void ajaxNoPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendError(1001);
	}
}
