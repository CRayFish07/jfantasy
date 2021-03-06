package org.jfantasy.mall.delivery.bean;

import org.jfantasy.common.bean.Area;
import org.jfantasy.common.bean.converter.AreaConverter;
import org.jfantasy.common.bean.databind.AreaDeserializer;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.jackson.JSON;
import org.jfantasy.mall.delivery.bean.databind.DeliveryTypeDeserializer;
import org.jfantasy.mall.delivery.bean.databind.DeliveryTypeSerializer;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 送货信息表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-10-15 下午3:37:40
 */
@ApiModel("送货信息")
@Entity
@Table(name = "MALL_DELIVERY_SHIPPING")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "items", "type"})
public class Shipping extends BaseBusEntity {

    private static final long serialVersionUID = 4315245804828793329L;
    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    @ApiModelProperty("发货编号")
    @Column(name = "SN", nullable = false, unique = true)
    @GenericGenerator(name = "serialnumber", strategy = "serialnumber", parameters = {@Parameter(name = "expression", value = "'SN_' + #DateUtil.format('yyyyMMdd') + #StringUtil.addZeroLeft(#SequenceInfo.nextValue('SHIPPING-SN'), 5)")})
    private String sn;
    @ApiModelProperty("配送方式名称")
    @JsonProperty("typeName")
    @Column(name = "DELIVERY_TYPE_NAME", length = 50)
    private String deliveryTypeName;
    @ApiModelProperty("物流公司名称")
    @JsonProperty("corpName")
    @Column(name = "DELIVERY_CORP_NAME", length = 50)
    private String deliveryCorpName;
    @ApiModelProperty("物流公司网址")
    @JsonProperty("corpURL")
    @Column(name = "DELIVERY_CORP_URL", length = 50)
    private String deliveryCorpUrl;
    @ApiModelProperty("物流单号")
    @Column(name = "DELIVERY_SN", length = 50)
    private String deliverySn;
    @ApiModelProperty("物流费用")
    @Column(name = "DELIVERY_FEE", precision = 10, scale = 2)
    private BigDecimal deliveryFee;
    @ApiModelProperty("收货人姓名")
    @Column(name = "SHIP_NAME", length = 50)
    private String shipName;
    @ApiModelProperty("收货地区信息")
    @Column(name = "SHIP_AREA_STORE", length = 300)
    @Convert(converter = AreaConverter.class)
    private Area shipArea;
    @ApiModelProperty("收货地址")
    @Column(name = "SHIP_ADDRESS", length = 150)
    private String shipAddress;
    @ApiModelProperty("收货邮编")
    @Column(name = "SHIP_ZIP_CODE", length = 10)
    private String shipZipCode;
    @ApiModelProperty("收货手机")
    @Column(name = "SHIP_MOBILE", length = 12)
    private String shipMobile;
    @ApiModelProperty("备注")
    @Column(name = "MEMO", length = 150)
    private String memo;
    @ApiModelProperty("订单类型")
    @Column(name = "ORDER_TYPE", length = 20)
    private String orderType;
    @ApiModelProperty("订单编号")
    @Column(name = "ORDER_SN")
    private String orderSn;
    /**
     * 配送方式
     */
    @ApiModelProperty(hidden = true)
    @JsonProperty("type")
    @JsonSerialize(using = DeliveryTypeSerializer.class)
    @JsonDeserialize(using = DeliveryTypeDeserializer.class)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "DELIVERY_TYPE_ID", foreignKey = @ForeignKey(name = "FK_SHIPPING_DELIVERY_TYPE"))
    private DeliveryType deliveryType;
    /**
     * 物流项
     */
    @ApiModelProperty(name = "items", hidden = true)
    @JsonProperty("items")
    @OneToMany(mappedBy = "shipping", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<DeliveryItem> deliveryItems = new ArrayList<DeliveryItem>();

    public String getDeliveryTypeName() {
        return deliveryTypeName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setDeliveryTypeName(String deliveryTypeName) {
        this.deliveryTypeName = deliveryTypeName;
    }

    public String getDeliveryCorpName() {
        return deliveryCorpName;
    }

    public void setDeliveryCorpName(String deliveryCorpName) {
        this.deliveryCorpName = deliveryCorpName;
    }

    public String getDeliveryCorpUrl() {
        return deliveryCorpUrl;
    }

    public void setDeliveryCorpUrl(String deliveryCorpUrl) {
        this.deliveryCorpUrl = deliveryCorpUrl;
    }

    public String getDeliverySn() {
        return deliverySn;
    }

    public void setDeliverySn(String deliverySn) {
        this.deliverySn = deliverySn;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipZipCode() {
        return shipZipCode;
    }

    public void setShipZipCode(String shipZipCode) {
        this.shipZipCode = shipZipCode;
    }

    public String getShipMobile() {
        return shipMobile;
    }

    public void setShipMobile(String shipMobile) {
        this.shipMobile = shipMobile;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public List<DeliveryItem> getDeliveryItems() {
        return deliveryItems;
    }

    public void setDeliveryItems(List<DeliveryItem> deliveryItems) {
        this.deliveryItems = deliveryItems;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty("收货地区存储")
    public Area getShipArea() {
        return this.shipArea;
    }

    @JsonDeserialize(using = AreaDeserializer.class)
    public void setShipArea(Area shipArea) {
        this.shipArea = shipArea;
    }
}