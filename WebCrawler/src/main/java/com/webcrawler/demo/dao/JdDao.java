package com.webcrawler.demo.dao;

import com.webcrawler.demo.entity.JdModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface JdDao extends PagingAndSortingRepository<JdModel, Long> {

}
