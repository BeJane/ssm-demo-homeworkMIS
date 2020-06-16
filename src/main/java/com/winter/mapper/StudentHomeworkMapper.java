package com.winter.mapper;

import com.winter.model.StudentHomework;

import java.util.List;

public interface StudentHomeworkMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StudentHomework record);

    int insertSelective(StudentHomework record);

    StudentHomework selectByPrimaryKey(Long id);
    List<StudentHomework> selectByHomeworkId(Long hid);

    int updateByPrimaryKeySelective(StudentHomework record);

    int updateByPrimaryKeyWithBLOBs(StudentHomework record);

    int updateByPrimaryKey(StudentHomework record);
}