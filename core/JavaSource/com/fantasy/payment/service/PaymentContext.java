package com.fantasy.payment.service;

import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.order.OrderDetails;
import com.fantasy.payment.order.OrderDetailsService;
import com.fantasy.payment.product.PaymentProduct;

/**
 * 支付上下文对象
 */
public class PaymentContext {

    private static ThreadLocal<PaymentContext> threadLocal = new ThreadLocal<PaymentContext>();
    /**
     * 支付订单对象
     */
    private OrderDetails orderDetails;
    /**
     * 支付对象
     */
    private Payment payment;
    /**
     * 支付配置
     */
    private PaymentConfig paymentConfig;
    /**
     * 支付产品
     */
    private PaymentProduct paymentProduct;
    /**
     * 支付订单 Service
     */
    private OrderDetailsService orderDetailsService;

    public static PaymentContext newInstall() {
        PaymentContext context = getContext();
        if (context == null) {
            setContext(new PaymentContext());
        }
        return getContext();
    }

    private static void setContext(PaymentContext context) {
        threadLocal.set(context);
    }

    public static PaymentContext getContext() {
        return threadLocal.get();
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        this.paymentConfig = payment.getPaymentConfig();
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setOrderDetailsService(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    public PaymentConfig getPaymentConfig() {
        return paymentConfig;
    }

    public PaymentProduct getPaymentProduct() {
        return paymentProduct;
    }

    public void setPaymentProduct(PaymentProduct paymentProduct) {
        this.paymentProduct = paymentProduct;
    }

    /**
     * 获取支付成功后的异步通知接口地址
     *
     * @return url
     */
    public String getNotifyUrl(String paymentSn) {
        return orderDetailsService.getNotifyUrl(paymentSn);
    }

    /**
     * 获取支付成功后的回调处理URL
     *
     * @return url
     */
    public String getReturnUrl(String paymentSn) {
        return orderDetailsService.getReturnUrl(paymentSn);
    }

    /**
     * 订单查看 URL
     *
     * @param orderSn 订单编号
     * @return url
     */
    public String getShowUrl(String orderSn) {
        return orderDetailsService.getShowUrl(orderSn);
    }

    /**
     * 支付详情页
     *
     * @param paymentSn 用于支付成功后的跳转地址
     * @return url
     */
    public String getShowPaymentUrl(String paymentSn) {
        return orderDetailsService.getShowPaymentUrl(paymentSn);
    }

    /**
     * 支付失败
     *
     * @param payment 支付对象
     */
    public void payFailure(Payment payment) {
        this.orderDetailsService.payFailure(payment);
    }

    /**
     * 支付成功
     *
     * @param payment 支付对象
     */
    public void paySuccess(Payment payment) {
        this.orderDetailsService.paySuccess(payment);
    }
}
