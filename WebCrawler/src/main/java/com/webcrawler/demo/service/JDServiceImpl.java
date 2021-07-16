package com.webcrawler.demo.service;

import com.webcrawler.demo.dao.JdDao;
import com.webcrawler.demo.parse.JdParse;
import com.webcrawler.demo.entity.JdModel;
import com.webcrawler.demo.util.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class JDServiceImpl {
    @Autowired
    private JdDao jdDao;

    public JDServiceImpl(JdDao jdDao) {
        this.jdDao = jdDao;
    }

    public int insert(String word, int first, int end, int number){
        //set key word
        String keyWord=word;
        //set price range
        int firstPrice=first;
        int endPrice=end;

        //set total pages to be fetched
        int sumPage=number;


        //get each page
        for (int i=1;i<=sumPage;i++){
            //used to store html content
            String html=null;

            try{
                //get page i content
                html= HttpRequest.getRawHtml(keyWord,i,firstPrice,endPrice);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


            //get data list from html page
            List<JdModel> dataList = JdParse.getData(html,keyWord);


            //print out all data
            for (JdModel jd: dataList){
                System.out.println("itemId:" + jd.getItemId() + "\t" + "itemName:" + jd.getItemName() + "\t" + "itemPrice:" + jd.getItemPrice() + "\tcommentnumber:" + jd.getCommentNumber() +"\timgurl:"+jd.getItemImgUrl()+ "\titemurl:" + jd.getItemUrl() + "\tshopname:" + jd.getShopName() + "\tshopurl:" + jd.getShopUrl() + "\tcrawl_time:" + jd.getCrawlerTime()+"\ttype:"+jd.getType());
            }
            jdDao.saveAll(dataList);
        }
        return 0;
    }

    public List<JdModel> getAllJds(){
        return (List<JdModel>) jdDao.findAll();
    }
}
