package com.xxxx.seckill.vo;

import com.xxxx.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 登录参数
 */
@Data
public class LoginVo {
	@NotNull
	@IsMobile
	private String mobile; // 手机号

	@NotNull
	@Length(min = 32)
	private String password;

}