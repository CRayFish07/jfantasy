package com.fantasy.mall.stock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.mall.delivery.bean.DeliveryItem;
import com.fantasy.mall.delivery.bean.Shipping;
import com.fantasy.mall.goods.bean.Product;
import com.fantasy.mall.goods.dao.ProductDao;
import com.fantasy.mall.stock.bean.Stock;
import com.fantasy.mall.stock.bean.WarningSettings;
import com.fantasy.mall.stock.dao.StockDao;

/**
 * @Author lsz
 * @Date 2013-11-28 下午4:51:46
 * 
 */
@Service
@Transactional
public class StockService {

	@Autowired
	private StockDao stockDao;

	@Autowired
	private ProductDao productDao;

	/**
	 * 保存
	 * 
	 * @param stock
	 * @return
	 */
	public Stock save(Stock stock) {
		this.stockDao.save(stock);// 保存库存记录
		return stock;
	}

	/**
	 * 查询
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
	public Pager<Stock> findPager(Pager<Stock> pager, List<PropertyFilter> filters) {
		return this.stockDao.findPager(pager, filters);
	}

	/**
	 * 出库
	 * 
	 * @功能描述
	 * @param shipping
	 */
	public void outStock(Shipping shipping) {
		for (DeliveryItem deliveryItem : shipping.getDeliveryItems()) {
			Stock stock = new Stock();
			stock.setProduct(deliveryItem.getProduct());
			stock.setStatus(false);
			stock.setChangeNumber(deliveryItem.getQuantity());
			stock.setRemark("销售出库:对应订单号:" + shipping.getOrder().getSn());
			// 为了让其出发 aop
			SpringContextUtil.getBeanByType(StockService.class).save(stock);
		}
	}

	/**
	 * 库存预警保存
	 * 
	 * @param id
	 * @param count
	 */
	public void productWarnSave(String expression, Long[] ids) {
		for (Long id : ids) {
			Product product = this.productDao.get(id);
			if (product != null) {
				WarningSettings settings = product.getWarningSettings();
				if(settings == null){
					settings = new WarningSettings();
					product.setWarningSettings(settings);
				}
				settings.setExpression(expression);
				this.productDao.save(product);
			}
		}
	}

}
