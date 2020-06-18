package com.winter.service;

import com.winter.mapper.StudentHomeworkMapper;
import com.winter.model.StudentHomework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:Jingqi Wu
 * @date: 2020/6/16
 */
@Service(value = "studentHomeworkService")
public class StudentHomeworkServiceImpl implements StudentHomeworkService {

    @Autowired
    private StudentHomeworkMapper studentHomeworkMapper;

    /**
     * 学生提交作业
     *
     * @param studentHomework
     * @return
     */
    @Override
    public int addStudentHomework(StudentHomework studentHomework) {
        return studentHomeworkMapper.insert(studentHomework);
    }

    /**
     * This is the method to be used to list down all
     * students' homework
     *
     * @param homeworkId
     * @return
     */
    @Override
    public List<StudentHomework> selectByHomeworkId(Long homeworkId) {
        return studentHomeworkMapper.selectByHomeworkId(homeworkId) ;
    }

    /**
     * 目前是通过studentid,homeworkid查询
     *
     * @param studentHomework
     * @return
     */
    @Override
    public StudentHomework  selectByDoublekey(StudentHomework studentHomework) {
        return studentHomeworkMapper.selectByDoublekey(studentHomework);
    }

    /**
     * This is the method to be used to search the student's homework
     *
     * @return
     */
    @Override
    public  StudentHomework selectByP(Long id) {
        return studentHomeworkMapper.selectByPrimaryKey(id);
    }

    /**
     * @param studentHomework
     * @return
     */
    @Override
    public int updateStudentHomework(StudentHomework studentHomework) {
        return studentHomeworkMapper.updateByPrimaryKeySelective(studentHomework);
    }
}
