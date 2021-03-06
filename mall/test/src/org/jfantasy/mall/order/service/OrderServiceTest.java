package org.jfantasy.mall.order.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.mall.order.bean.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFindPager() throws Exception {
        Pager<Order> pager = new Pager<Order>();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_orderType","medical"));
        filters.add(new PropertyFilter("EQS_shipName",""));
        filters.add(new PropertyFilter("LIKES_sn_OR_shipName","2"));
        Pager<Order> orderPager = this.orderService.findPager(pager,filters);
        assertNotNull(orderPager);
    }

    @Test
    public void testFind() throws Exception {

    }

    @Test
    public void testSave() throws Exception {

    }

    @Test
    public void testSubmitOrder() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testProcessed() throws Exception {

    }

    @Test
    public void testInvalid() throws Exception {

    }

    @Test
    public void testShipping() throws Exception {

    }
}