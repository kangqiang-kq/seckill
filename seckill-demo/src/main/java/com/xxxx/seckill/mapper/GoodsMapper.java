package com.xxxx.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.vo.GoodsVo;

import java.util.List;


public interface GoodsMapper extends BaseMapper<Goods> {

	// 商品裂变信息
	List<GoodsVo> findGoodsVo();

	// 详情
	GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
