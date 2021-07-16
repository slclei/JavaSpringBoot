package com.webcrawler.demo.api;

import com.webcrawler.demo.entity.JdModel;
import com.webcrawler.demo.service.JDServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/JD")
public class JDController {

    private JDServiceImpl jdServiceImpl;

    @Autowired
    public JDController(JDServiceImpl jdServiceImpl) {
        this.jdServiceImpl = jdServiceImpl;
    }

    @GetMapping
    public String hello(){
        return "hello JD";
    }

    @GetMapping("/get")
    public List<JdModel> getAllJd(){
        return jdServiceImpl.getAllJds();
    }

    @RequestMapping("/insert")
    @PostMapping
    public String insert() {
        String word="手机";
        int first=1000;
        int end=4000;
        int number=1;
        jdServiceImpl.insert(word,first,end,number);
        return "Done insert";
    }
}
