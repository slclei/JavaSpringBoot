package com.webcrawler.demo.parse;

import com.webcrawler.demo.entity.JdModel;

import java.util.ArrayList;
import java.util.List;

import com.webcrawler.demo.util.TimeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class JdParse {
    public static List<JdModel> getData(String html, String keyword){
        //store data i the list
        List<JdModel> data=new ArrayList<JdModel>();

        //parse html content with Jsoup
        Document doc =Jsoup.parse(html);

        //get content from html tag
        //Need changed for other web sites
        Elements elements=doc.select("li[class=gl-item]");

        for (Element ele: elements){
            //get item attributions with class name
            String itemId = ele.attr("data-sku");
            System.out.println(itemId);
            String itemPrice = ele.select("div[class=p-price]").select("strong").select("i").text();
            //get books
            String itemName = ele.select("div[class~=p-name?]").select("em").text();
            String commentNumber = ele.select("div[class=p-commit]").select("strong").text();
            //plan to choose images' uri; use href in tag a instead.
            String imgurl =ele.select("div[class=p-img]").select("a").attr("href");
            boolean b=imgurl.startsWith("https:");
            if (b==false){
                imgurl="https:"+imgurl;
            }

            String itemUrl="https://item.jd.com/"+itemId+".html";
            String type=keyword;
            String shopName="";
            String shopUrl="";

            //get shop name and url if possible; or default value
            if (ele.select("div[class=p-shop]").select("a[class=curr-shop]").text().length() != 0) {
                shopName = ele.select("div[class=p-shop]").select("a[class=curr-shop]").text();
                shopUrl = "https:" + ele.select("div[class=p-shop]").select("a[class=curr-shop]").attr("href");
            } else {
                shopName = "京东自营";
                shopUrl = "https://mall.jd.com/index-" + ele.select("div[class=p-shop]").attr("data-shopid") + ".html";
            }

            String crawlTime = TimeUtils.GetNowDate("yyyy-MM-dd HH:mm:ss");
            //create a Jd model
            JdModel jdModel=new JdModel();
            //set Jdmodel value
            jdModel.setItemId(itemId);
            jdModel.setItemName(itemName);
            jdModel.setItemPrice(itemPrice);
            jdModel.setCommentNumber(commentNumber);
            jdModel.setItemImgUrl(imgurl);
            jdModel.setItemUrl(itemUrl);
            jdModel.setShopName(shopName);
            jdModel.setShopUrl(shopUrl);
            jdModel.setCrawlerTime(crawlTime);
            jdModel.setType(type);
            //store each model in data list
            data.add(jdModel);
        }

        return data;
    }
}
