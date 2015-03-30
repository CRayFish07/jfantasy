package com.fantasy.cms.service;import com.fantasy.attr.storage.bean.Attribute;import com.fantasy.attr.storage.bean.AttributeType;import com.fantasy.attr.storage.bean.AttributeVersion;import com.fantasy.attr.storage.bean.Converter;import com.fantasy.attr.storage.service.AttributeService;import com.fantasy.attr.storage.service.AttributeTypeService;import com.fantasy.attr.storage.service.AttributeVersionService;import com.fantasy.attr.storage.service.ConverterService;import com.fantasy.attr.framework.converter.FileDetailTypeConverter;import com.fantasy.attr.framework.util.VersionUtil;import com.fantasy.cms.bean.Article;import com.fantasy.cms.bean.ArticleCategory;import com.fantasy.cms.bean.Content;import com.fantasy.file.bean.FileDetail;import com.fantasy.file.service.FileUploadService;import com.fantasy.framework.dao.Pager;import com.fantasy.framework.dao.hibernate.PropertyFilter;import com.fantasy.framework.util.common.DateUtil;import com.fantasy.framework.util.common.file.FileUtil;import com.fantasy.framework.util.htmlcleaner.HtmlCleanerUtil;import com.fantasy.framework.util.ognl.OgnlUtil;import junit.framework.Assert;import org.apache.commons.logging.Log;import org.apache.commons.logging.LogFactory;import org.hibernate.criterion.Criterion;import org.hibernate.criterion.Restrictions;import org.htmlcleaner.TagNode;import org.junit.After;import org.junit.Before;import org.junit.Test;import org.junit.runner.RunWith;import org.springframework.test.context.ContextConfiguration;import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;import org.springframework.beans.factory.annotation.Autowired;import java.io.File;import java.io.FileNotFoundException;import java.io.IOException;import java.lang.reflect.Array;import java.util.ArrayList;import java.util.List;@RunWith(SpringJUnit4ClassRunner.class)@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})public class CmsServiceTest {    private static Log logger = LogFactory.getLog(CmsServiceTest.class);    @Autowired    private CmsService cmsService;    @Autowired    private AttributeVersionService attributeVersionService;    @Autowired    private ConverterService converterService;    @Autowired    private AttributeTypeService attributeTypeService;    @Autowired    private AttributeService attributeService;    @Autowired    private FileUploadService fileUploadService;    public String getText(){        try {            TagNode root = HtmlCleanerUtil.htmlCleaner(CmsServiceTest.class.getResource("article.xml"), "utf-8");            TagNode text = HtmlCleanerUtil.findFristTagNode(root, "//div[@class='feed-text']");            return HtmlCleanerUtil.getAsString(text);        }catch (Exception ex){            logger.error(ex.getMessage(),ex);            return "";        }    }    @Before    public void setUp() throws Exception{        ArticleCategory category = new ArticleCategory();        category.setCode("TEST");        category.setName("测试分类");        category.setLayer(1);        category.setDescription("测试文章分类");        this.cmsService.save(category);        Article article = new Article();        article.setTitle("test-测试文章");        article.setSummary("测试文章摘要");        article.setContent(new Content(this.getText()));        article.setCategory(category);        this.cmsService.save(article);    }    private void addArticleVersion() {        Converter converter = new Converter();        converter.setName("图片转换器");        converter.setTypeConverter(FileDetailTypeConverter.class.getName());        converter.setDescription("");        converterService.save(converter);        converter = converterService.findUnique(Restrictions.eq("name", "图片转换器"), Restrictions.eq("typeConverter", FileDetailTypeConverter.class.getName()));        logger.debug(converter);        Assert.assertNotNull(converter);        AttributeType attributeType = new AttributeType();        attributeType.setName("图片数据类型");        attributeType.setDataType(FileDetail[].class.getName());        attributeType.setConverter(converter);        attributeType.setDescription("");        attributeTypeService.save(attributeType);        attributeType = attributeTypeService.findUnique(Restrictions.eq("name", "图片数据类型"));        logger.debug(attributeType);        Assert.assertNotNull(attributeType);        Attribute attribute = new Attribute();        attribute.setCode("images");        attribute.setName("多张图片");        attribute.setDescription("");        attribute.setAttributeType(attributeType);        attribute.setNonNull(true);        attribute.setNotTemporary(false);        attributeService.save(attribute);        attributeVersionService.save(Article.class.getName(),"1.0",attribute);    }    @After    public void tearDown() throws Exception {        this.testDelete();    }    @Test    public void testSaveImages(){        for(Converter converter : converterService.find(Restrictions.eq("name", "图片转换器"), Restrictions.eq("typeConverter", FileDetailTypeConverter.class.getName()))){            this.converterService.delete(converter.getId());        }        AttributeVersion version = this.attributeVersionService.findUniqueByTargetClassName(Article.class.getName(), "1.0");        if (version != null) {            this.attributeVersionService.delete(version.getId());        }        //添加动态bean定义        this.addArticleVersion();        Article article = VersionUtil.createDynaBean(Article.class, "1.0");        article.setTitle("测试动态图片");        article.setSummary("测试动态图片");        article.setContent(new Content("测试动态图片"));        article.setCategory(this.cmsService.get("TEST"));        try {            File file = new File(BannerServiceTest.class.getResource("banner_1.jpg").getFile());            String mimeType = FileUtil.getMimeType(file);            FileDetail fileDetail = fileUploadService.upload(file, mimeType, file.getName(), "test");            VersionUtil.getOgnlUtil(article.getVersion().getAttributes().get(0).getAttributeType()).setValue("images",article,fileDetail.getFileManagerId() + ":" + fileDetail.getAbsolutePath());        } catch (FileNotFoundException e) {            logger.error(e.getMessage(), e);        } catch (IOException e) {            logger.error(e.getMessage(), e);        }        logger.debug(article);        this.cmsService.save(article);        article = this.cmsService.get(article.getId());        Object images = VersionUtil.getOgnlUtil(article.getVersion().getAttributes().get(0).getAttributeType()).getValue("images",article);        Assert.assertEquals(1, Array.getLength(images));        this.cmsService.delete(article.getId());        version = attributeVersionService.findUniqueByTargetClassName(Article.class.getName(), "1.0");        if (version == null) {            for(Converter converter : converterService.find(Restrictions.eq("name", "图片转换器"), Restrictions.eq("typeConverter", FileDetailTypeConverter.class.getName()))){                this.converterService.delete(converter.getId());            }        }else {            for (Attribute attribute : version.getAttributes()) {                this.converterService.delete(attribute.getAttributeType().getConverter().getId());            }            this.attributeVersionService.delete(version.getId());        }    }    public void testDelete(){        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();        filters.add(new PropertyFilter("EQS_category.code","TEST"));        List<Article> articles = this.cmsService.find(filters,"id","asc",10);        for(Article article : articles){            this.cmsService.delete(article.getId());        }        ArticleCategory category = this.cmsService.get("TEST");        this.cmsService.delete(category.getCode());    }    @Test    public void findPager() {        Pager<Article> pager = new Pager<Article>();        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();        filters.add(new PropertyFilter("EQS_category.code", "TEST"));        pager = cmsService.findPager(pager, filters);        Assert.assertTrue(pager.getTotalCount() > 0);        for(Article article : pager.getPageItems()) {            article = this.cmsService.get(article.getId());            logger.debug(article);            logger.debug(article.getContent().toString());        }    }    @Test    public void save() {        List<Article> articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNotNull("version")}, 1);        if (!articles.isEmpty()) {            Article article = this.cmsService.get(articles.get(0).getId());            if (!article.getAttributeValues().isEmpty()) {                Attribute attribute = article.getAttributeValues().get(0).getAttribute();                OgnlUtil.getInstance().setValue(attribute.getCode(), article, "123456");                article.setTitle("JUnit测试修改标题-" + DateUtil.format("yyyy-MM-dd"));                cmsService.save(article);            }        }        articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNull("version")}, 1);        if (!articles.isEmpty()) {            Article article = this.cmsService.get(articles.get(0).getId());            article.setTitle("JUnit测试修改标题-" + DateUtil.format("yyyy-MM-dd"));            cmsService.save(article);        }    }    @Test    public void get() {        List<Article> articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNotNull("version")}, 1);        if (!articles.isEmpty()) {            Article article = this.cmsService.get(articles.get(0).getId());            //test not null            Assert.assertNotNull(article);            //test attrubuteValues not null            Assert.assertNotNull(article.getAttributeValues());        }        articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNull("version")}, 1);        if (!articles.isEmpty()) {            Article article = this.cmsService.get(articles.get(0).getId());            //test not null            Assert.assertNotNull(article);        }    }}