package com.webcrawler.demo.entity;

import javax.persistence.*;
import java.util.Objects;

//tell the database that this is an entity
//(name="") can be used to rename this entity
//Database setting is in resources/application.properties
@Entity
@Table(name="JD")
public class JdModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String itemId;
    private String itemName;
    private String itemPrice;
    private String itemImgUrl;
    private String itemUrl;
    private String shopName;
    private String commentNumber;
    private String shopUrl;
    private String crawlerTime;
    private String type;

    public JdModel() {
    }

    public JdModel(String itemId, String itemName, String itemPrice, String itemImgUrl, String itemUrl, String shopName, String commentNumber, String shopUrl, String crawlerTime, String type) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemImgUrl = itemImgUrl;
        this.itemUrl = itemUrl;
        this.shopName = shopName;
        this.commentNumber = commentNumber;
        this.shopUrl = shopUrl;
        this.crawlerTime = crawlerTime;
        this.type = type;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getCrawlerTime() {
        return crawlerTime;
    }

    public void setCrawlerTime(String crawlerTime) {
        this.crawlerTime = crawlerTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
