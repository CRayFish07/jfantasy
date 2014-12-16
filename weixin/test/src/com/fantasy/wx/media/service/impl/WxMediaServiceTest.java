package com.fantasy.wx.media.service.impl;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.wx.media.bean.WxMedia;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class WxMediaServiceTest {

    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private WxMediaService wxMediaService;

    @After
    public void tearDown() throws Exception {
        Pager<WxMedia> pager=wxMediaService.findPager(new Pager<WxMedia>(), new ArrayList<PropertyFilter>());
        List<Long> list=new ArrayList<Long>();
        for(WxMedia media:pager.getPageItems()){
            list.add(media.getId());
        }
        if(list.size()>0){
            Long[] ids=list.toArray(new Long[list.size()]);
            wxMediaService.delete(ids);
        }
    }
    @Test
    public void testFindPager() throws Exception {
        File file=new File(WxMediaServiceTest.class.getResource("mm.jpeg").getPath());
        String rename=Long.toString(new Date().getTime())+Integer.toString(new Random().nextInt(900000)+100000)+"."+ WebUtil.getExtension(file.getName());
        FileDetail fileDetail=fileUploadService.upload(file, FileUtil.getMimeType(file), rename, "test");
        Assert.isNull(fileDetail);

        WxMedia media=wxMediaService.mediaUpload(fileDetail, "image");
        Assert.isNull(media);

        Pager<WxMedia> pager=wxMediaService.findPager(new Pager<WxMedia>(), new ArrayList<PropertyFilter>());
        Assert.isNull(pager.getPageItems());

        WxMedia wxMedia=wxMediaService.getMedia(media.getMediaId());
        Assert.isNull(wxMedia);

        FileDetail fileDetail1=wxMediaService.mediaDownload(wxMedia.getMediaId(), "test");
        Assert.isNull(fileDetail1);
    }
}