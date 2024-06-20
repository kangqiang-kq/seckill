package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private MQSender mqSender;


	@RequestMapping("/info")
	@ResponseBody
	public RespBean info(User user) {
		return RespBean.success(user);
	}



	 @RequestMapping("/mq")
	 @ResponseBody
	 public void mq() {
	 	mqSender.send("Hello");
	 }



	 @RequestMapping("/mq/fanout")
	 @ResponseBody
	 public void mq01() {
	 	mqSender.send("Hello");
	 }


	 @RequestMapping("/mq/direct01")
	 @ResponseBody
	 public void mq02() {
	 	mqSender.send01("Hello,Red");
	 }


	 @RequestMapping("/mq/direct02")
	 @ResponseBody
	 public void mq03() {
	 	mqSender.send02("Hello,Green");
	 }



	 @RequestMapping("/mq/topic01")
	 @ResponseBody
	 public void mq04() {
	 	mqSender.send03("Hello,Red");
	 }



	 @RequestMapping("/mq/topic02")
	 @ResponseBody
	 public void mq05() {
	 	mqSender.send04("Hello,Green");
	 }



	 @RequestMapping("/mq/header01")
	 @ResponseBody
	 public void mq06() {
	 	mqSender.send05("Hello,Header01");
	 }


	 @RequestMapping("/mq/header02")
	 @ResponseBody
	 public void mq07() {
	 	mqSender.send06("Hello,Header02");
	 }

}
