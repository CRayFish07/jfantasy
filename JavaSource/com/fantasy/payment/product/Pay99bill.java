package com.fantasy.payment.product;

import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.system.util.SettingUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 快钱支付
 */

public class Pay99bill extends AbstractPaymentProduct {

    public static final String PAYMENT_URL = "https://www.99bill.com/gateway/recvMerchantInfoAction.htm";// 支付请求URL
    public static final String RETURN_URL = "/shop/payment!payreturn.action";// 回调处理URL

    @Override
    public String getPaymentUrl() {
        return PAYMENT_URL;
    }

    @Override
    public String getPaymentSn(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return null;
        }
        String orderId = httpServletRequest.getParameter("orderId");
        if (StringUtils.isEmpty(orderId)) {
            return null;
        }
        return orderId;
    }

    @Override
    public BigDecimal getPaymentAmount(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return null;
        }
        String payAmount = httpServletRequest.getParameter("payAmount");
        if (StringUtils.isEmpty(payAmount)) {
            return null;
        }
        return new BigDecimal(payAmount);
    }

    public boolean isPaySuccess(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return false;
        }
        String payResult = httpServletRequest.getParameter("payResult");
        return StringUtils.equals(payResult, "10");
    }

    @Override
    public Map<String, String> getParameterMap(PaymentConfig paymentConfig, String paymentSn, BigDecimal paymentAmount, HttpServletRequest httpServletRequest) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String dateString = simpleDateFormat.format(new Date());
        String totalAmountString = paymentAmount.multiply(new BigDecimal(100)).setScale(0).toString();

        String inputCharset = "1";// 字符集编码（1：UTF-8、2：GBK、3：GB2312）
        String bgUrl = SettingUtil.get("website", "ShopUrl") + RETURN_URL + "?paymentsn=" + paymentSn;// 回调处理URL
        String version = "v2.0";// 网关版本
        String language = "1";// 显示语言种类（1：中文）
        String signType = "1";// 签名类型（1：MD5）
        String merchantAcctId = paymentConfig.getBargainorId();// 收款方账号
        AtomicReference<String> orderId = new AtomicReference<String>(paymentSn);// 支付编号
        AtomicReference<String> orderAmount = new AtomicReference<String>(totalAmountString);// 总金额（单位：分）
        AtomicReference<String> orderTime = new AtomicReference<String>(dateString);// 订单提交时间
        AtomicReference<String> productName = new AtomicReference<String>(paymentSn);// 商品名称
        AtomicReference<String> productDesc = new AtomicReference<String>(paymentSn);// 商品描述
        String payType = "00";// 支付方式（00：显示所有支付方式、10：只显示银行卡支付方式、11：只显示电话银行支付方式、12：只显示快钱账户支付方式、13：只显示线下支付方式、14：只显示B2B支付方式）
        String redoFlag = "0";// 同一订单重复提交（1：禁止、0：允许）
        String key = paymentConfig.getBargainorKey();// 密钥

        // 生成签名
        Map<String, String> signMap = new LinkedHashMap<String, String>();
        signMap.put("inputCharset", inputCharset);
        signMap.put("bgUrl", bgUrl);
        signMap.put("version", version);
        signMap.put("language", language);
        signMap.put("signType", signType);
        signMap.put("merchantAcctId", merchantAcctId);
        signMap.put("orderId", orderId.get());
        signMap.put("orderAmount", orderAmount.get());
        signMap.put("orderTime", orderTime.get());
        signMap.put("productName", productName.get());
        signMap.put("productDesc", productDesc.get());
        signMap.put("payType", payType);
        signMap.put("redoFlag", redoFlag);
        signMap.put("key", key);
        String signMsg = DigestUtils.md5Hex(getParameterString(signMap)).toUpperCase();

        // 参数处理
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("inputCharset", inputCharset);
        parameterMap.put("bgUrl", bgUrl);
        parameterMap.put("version", version);
        parameterMap.put("language", language);
        parameterMap.put("signType", signType);
        parameterMap.put("merchantAcctId", merchantAcctId);
        parameterMap.put("orderId", orderId.get());
        parameterMap.put("orderAmount", orderAmount.get());
        parameterMap.put("orderTime", orderTime.get());
        parameterMap.put("productName", productName.get());
        parameterMap.put("productDesc", productDesc.get());
        parameterMap.put("payType", payType);
        parameterMap.put("redoFlag", redoFlag);
        parameterMap.put("signMsg", signMsg);

        return parameterMap;
    }

    @Override
    public boolean verifySign(PaymentConfig paymentConfig, HttpServletRequest httpServletRequest) {
        // 获取参数
        String merchantAcctId = httpServletRequest.getParameter("merchantAcctId");
        String version = httpServletRequest.getParameter("version");
        String language = httpServletRequest.getParameter("language");
        String signType = httpServletRequest.getParameter("signType");
        String payType = httpServletRequest.getParameter("payType");
        String bankId = httpServletRequest.getParameter("bankId");
        String orderId = httpServletRequest.getParameter("orderId");
        String orderTime = httpServletRequest.getParameter("orderTime");
        String orderAmount = httpServletRequest.getParameter("orderAmount");
        String dealId = httpServletRequest.getParameter("dealId");
        String bankDealId = httpServletRequest.getParameter("bankDealId");
        String dealTime = httpServletRequest.getParameter("dealTime");
        String payAmount = httpServletRequest.getParameter("payAmount");
        String fee = httpServletRequest.getParameter("fee");
        String payResult = httpServletRequest.getParameter("payResult");
        String errCode = httpServletRequest.getParameter("errCode");
        String signMsg = httpServletRequest.getParameter("signMsg");

        // 验证支付签名
        Map<String, String> signMap = new LinkedHashMap<String, String>();
        signMap.put("merchantAcctId", merchantAcctId);
        signMap.put("version", version);
        signMap.put("language", language);
        signMap.put("signType", signType);
        signMap.put("payType", payType);
        signMap.put("bankId", bankId);
        signMap.put("orderId", orderId);
        signMap.put("orderTime", orderTime);
        signMap.put("orderAmount", orderAmount);
        signMap.put("dealId", dealId);
        signMap.put("bankDealId", bankDealId);
        signMap.put("dealTime", dealTime);
        signMap.put("payAmount", payAmount);
        signMap.put("fee", fee);
        signMap.put("payResult", payResult);
        signMap.put("errCode", errCode);
        signMap.put("key", paymentConfig.getBargainorKey());
        return StringUtils.equals(signMsg, DigestUtils.md5Hex(getParameterString(signMap)).toUpperCase());
    }

    @Override
    public String getPaynotifyMessage() {
        return null;
    }

    @Override
    public String getPayreturnMessage(String paymentSn) {
        return "<result>1</result><redirecturl>" + SettingUtil.get("website", "ShopUrl") + RESULT_URL + "?paymentsn=" + paymentSn + "</redirecturl>";
    }

}