package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.mapper.OrderMapper;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillGoods;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.utils.MD5Util;
import com.xxxx.seckill.utils.UUIDUtil;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

	@Autowired
	private ISeckillGoodsService seckillGoodsService;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private ISeckillOrderService seckillOrderService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 功能描述: 秒杀
	 *
	 * @param:
	 */
	@Transactional
	@Override
	public Order seckill(User user, GoodsVo goods) {
		ValueOperations valueOperations = redisTemplate.opsForValue();
		//秒杀商品表减库存
		SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id",
				goods.getId()));
		seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
		// sql扣减库存，如果大于0，才会进行扣减操作
		boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = " +
				"stock_count-1").eq(
				"goods_id", goods.getId()).gt("stock_count", 0));
		//		1.seckillGoodsService.update(...): 这是一个更新操作，调用了seckillGoodsService中的update方法，用于更新秒杀商品信息。
		//		2.new UpdateWrapper&lt;SeckillGoods&gt;(): 这里创建了一个更新条件的包装器，用于构建更新条件。
		//		3..setSql("stock_count = stock_count-1"): 这段代码设置了要执行的更新操作，将商品库存减少1。这里使用了原生的 SQL 语句来执行更新操作。
		//		4..eq("goods_id", goods.getId()): 这是一个等于条件，用于指定要更新的商品的 ID。
		//		5..gt("stock_count", 0): 这是一个大于条件，用于确保商品库存大于0，以避免库存为负值。

		if (seckillGoods.getStockCount() < 1) {
			//判断是否还有库存
			valueOperations.set("isStockEmpty:" + goods.getId(), "0");
			return null;
		}
		//生成订单
		Order order = new Order();
		order.setUserId(user.getId());
		order.setGoodsId(goods.getId());
		order.setDeliveryAddrId(0L);
		order.setGoodsName(goods.getGoodsName());
		order.setGoodsCount(1);
		order.setGoodsPrice(seckillGoods.getSeckillPrice());
		order.setOrderChannel(1);
		order.setStatus(0);
		order.setCreateDate(new Date());
		orderMapper.insert(order);
		//生成秒杀订单
		SeckillOrder seckillOrder = new SeckillOrder();
		seckillOrder.setUserId(user.getId());
		seckillOrder.setOrderId(order.getId());
		seckillOrder.setGoodsId(goods.getId());
		seckillOrderService.save(seckillOrder);
		valueOperations.set("order:" + user.getId() + ":" + goods.getId(), seckillOrder);
		return order;
	}


	@Override
	public OrderDetailVo detail(Long orderId) {
		if (orderId == null) {
			throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
		}
		Order order = orderMapper.selectById(orderId);
		GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
		OrderDetailVo detail = new OrderDetailVo();
		detail.setOrder(order);
		detail.setGoodsVo(goodsVo);
		return detail;
	}



	@Override
	public String createPath(User user, Long goodsId) {
		String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
		redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str, 60, TimeUnit.SECONDS);
		return str;
	}

	/**
	 * 功能描述:校验秒杀地址
	 */
	@Override
	public boolean checkPath(User user, Long goodsId, String path) {
		if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
			return false;
		}
		String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
		return path.equals(redisPath);
	}


	@Override
	public boolean checkCaptcha(User user, Long goodsId, String captcha) {
		if (StringUtils.isEmpty(captcha) || user == null || goodsId < 0) {
			return false;
		}
		String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
		return captcha.equals(redisCaptcha);
	}
}
