package com.xxxx.seckill.exception;

import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor // 空参构造、全参构造
public class GlobalException extends RuntimeException {
	private RespBeanEnum respBeanEnum;
}