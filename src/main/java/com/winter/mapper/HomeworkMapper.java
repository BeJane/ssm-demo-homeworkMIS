package com.winter.mapper;

import com.winter.model.Homework;

import java.util.List;

public interface HomeworkMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Homework record);

    int insertSelective(Homework record);

    List<Homework> selectAll();
    List<Homework> selectByTeacherId(String teacherId);

    List<Homework> selectByTeacherName(String teacherName);
    Homework selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Homework record);

    int updateByPrimaryKeyWithBLOBs(Homework record);

    int updateByPrimaryKey(Homework record);
}