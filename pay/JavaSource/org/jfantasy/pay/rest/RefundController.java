package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.rest.form.RefundForm01;
import org.jfantasy.pay.service.PayService;
import org.jfantasy.pay.service.RefundService;
import org.jfantasy.pay.service.vo.ToRefund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "refund", description = "退款记录")
@RestController
@RequestMapping("/refunds")
public class RefundController {

    @Autowired
    private RefundService refundService;
    @Autowired
    private PayService payService;

    @ApiOperation("查询退款记录")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Refund> search(Pager<Refund> pager, List<PropertyFilter> filters) {
        return refundService.findPager(pager, filters);
    }

    @ApiOperation(value = "更新退款记录", notes = "该方法只能修改 退款状态 ")
    @RequestMapping(value = "/{sn}", method = RequestMethod.PUT)
    @ResponseBody
    public ToRefund update(@PathVariable("sn") String sn, @RequestBody RefundForm01 form) {
        return payService.refund(sn,form.getStatus(),form.getRemark());
    }

    @ApiOperation("获取退款记录")
    @RequestMapping(value = "/{sn}", method = RequestMethod.GET)
    @ResponseBody
    public Refund view(@PathVariable("sn") String sn) {
        return this.refundService.get(sn);
    }

    @ApiOperation("删除退款记录")
    @RequestMapping(value = "/{sn}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("sn") String sn) {
        this.refundService.delete(sn);
    }

    @ApiOperation("批量删除退款记录")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... sns) {
        this.refundService.delete(sns);
    }

}
