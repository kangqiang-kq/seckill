package com.xxxx.seckill.controller;

import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
//@RestController
@RequestMapping("/login")
@Slf4j // slf4j 输出日志
public class LoginController {

	@Autowired
	private IUserService userService;

	@RequestMapping("/toLogin")
	public String toLogin(){
		return "login";
	} // 不要使用RestController，这里需要做页面跳转

	@RequestMapping("/doLogin")
	@ResponseBody // 返回对象
	public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
		return userService.doLogin(loginVo,request,response);
	}
}