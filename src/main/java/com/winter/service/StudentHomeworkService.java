package com.winter.service;
import com.winter.model.StudentHomework;

import java.util.List;


/**
 * @author:Jingqi Wu
 * @date: 2020/4/23
 */
public interface StudentHomeworkService {


    /**
     * 学生提交作业
     * @param studentHomework
     * @return
     */
    int addStudentHomework(StudentHomework studentHomework);

    /**
     * @return
     */
    int updateStudentHomework(StudentHomework studentHomework);
    /**
     *  This is the method to be used to list down all
     *  students' homework
     * @param homeworkId
     * @return
     */
    List<StudentHomework> selectByHomeworkId(Long homeworkId);

    /**
     * This is the method to be used to search the student's homework
     *
     * @param id
     * @return
     */
    StudentHomework selectByP(Long id);

    /**
     * 目前是通过studentid,homeworkid查询
     * @param studentHomework
     * @return
     */
    StudentHomework selectByDoublekey(StudentHomework studentHomework);
}
