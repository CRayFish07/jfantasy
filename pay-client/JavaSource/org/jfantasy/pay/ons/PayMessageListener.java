package org.jfantasy.pay.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.pay.order.OrderService;
import org.jfantasy.pay.order.entity.PaymentDetails;
import org.jfantasy.pay.order.entity.RefundDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class PayMessageListener implements MessageListener {

    @Autowired
    private OrderService orderService;

    @Override
    public Action consume(Message message, ConsumeContext context) {
        if ("payment".equals(message.getKey())) {
            PaymentDetails details = JSON.deserialize(Arrays.toString(message.getBody()), PaymentDetails.class);
            assert details != null;
            orderService.on(details.getOrderKey(), details, details.getMemo());
        } else if ("refund".equals(message.getKey())) {
            RefundDetails details = JSON.deserialize(Arrays.toString(message.getBody()), RefundDetails.class);
            assert details != null;
            orderService.on(details.getOrderKey(), details, details.getMemo());
        }
        return Action.CommitMessage;
    }

}