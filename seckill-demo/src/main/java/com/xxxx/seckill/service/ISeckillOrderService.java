package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;


public interface ISeckillOrderService extends IService<SeckillOrder> {


	Long getResult(User user, Long goodsId);
}
