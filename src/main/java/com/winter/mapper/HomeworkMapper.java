package com.winter.mapper;

import com.winter.model.Homework;

import java.util.List;

public interface HomeworkMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Homework record);

    int insertSelective(Homework record);

    Homework selectByPrimaryKey(Long id);
    List<Homework> selectAll();
    int updateByPrimaryKeySelective(Homework record);

    int updateByPrimaryKeyWithBLOBs(Homework record);

    int updateByPrimaryKey(Homework record);
}