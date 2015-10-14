package com.fantasy.common.order;

import com.fantasy.common.service.AreaService;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.payment.bean.Payment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestOrderDetailsService extends AbstractOrderService implements InitializingBean {

    @Autowired
    private OrderServiceFactory orderServiceFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setNotifyUrlTemplate("http://test.jfantasy.org/payment/notify/{paymentSn}");
        this.setReturnUrlTemplate("http://test.jfantasy.org/payment/return/{paymentSn}");
        this.setShowPaymentUrlTemplate("http://test.jfantasy.org/payment/{paymentSn}");
        orderServiceFactory.register("TEST", this);
    }

    @Override
    public Order loadOrderBySn(final String sn) {
        return new Order() {

            @Override
            public String getSN() {
                return sn;
            }

            @Override
            public String getType() {
                return "TEST";
            }

            @Override
            public String getSubject() {
                return "测试订单";
            }

            @Override
            public BigDecimal getTotalFee() {
                return BigDecimal.valueOf(0.01);
            }

            @Override
            public BigDecimal getPayableFee() {
                return BigDecimal.valueOf(0.01);
            }

            @Override
            public boolean isPayment() {
                return true;
            }

            @Override
            public List<OrderItem> getOrderItems() {
                return new ArrayList<OrderItem>() {
                    {
                        this.add(new OrderItem() {
                            @Override
                            public String getSn() {
                                return "SN000001";
                            }

                            @Override
                            public String getName() {
                                return "这个是测试订单项";
                            }

                            @Override
                            public Integer getQuantity() {
                                return 1;
                            }
                        });
                    }
                };
            }

            @Override
            public ShipAddress getShipAddress() {
                ShipAddress address = new ShipAddress();
                address.setArea(SpringContextUtil.getBeanByType(AreaService.class).get("430103"));
                address.setName("王五");
                address.setAddress("天心区308号");
                address.setMobile("159218471");
                address.setZipCode("415000");
                return address;
            }

        };
    }

    @Override
    public void payFailure(Payment payment) {
        LOG.debug("订单支付失败");
    }

    @Override
    public void paySuccess(Payment payment) {
        LOG.debug("订单支付成功");
    }

}