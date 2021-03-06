package com.winter.service;

import com.winter.model.Homework;

import java.util.List;

/**
 * @author:Jingqi Wu
 * @date: 2020/4/23
 */
public interface HomeworkService {


    /**
     * 老师发布作业
     * @param homework
     * @return
     */
    int addHomework(Homework homework);
    /**
     *  This is the method to be used to list down all homework
     * @return
     */
    List<Homework> selectAll();
    List<Homework> selectByTeacherId(String teacherId);
    List<Homework> selectByTeacherName(String teacherName);
    Homework selectByPK(Long id);
}
