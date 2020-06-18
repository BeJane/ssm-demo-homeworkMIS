package com.winter.service;

import com.winter.mapper.HomeworkMapper;
import com.winter.model.Homework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:Jingqi Wu
 * @date: 2020/6/16
 */
@Service(value = "homeworkService")
public class HomeworkServiceImpl implements HomeworkService {
    @Autowired
    HomeworkMapper homeworkMapper;

    /**
     * 老师发布作业
     *
     * @param homework
     * @return
     */
    @Override
    public int addHomework(Homework homework) {
        return homeworkMapper.insert(homework);
    }

    /**
     * This is the method to be used to list down all homework
     *
     * @return
     */
    @Override
    public List<Homework> selectAll() {
        return homeworkMapper.selectAll();
    }

    @Override
    public List<Homework> selectByTeacherId(String teacherId) {

        return homeworkMapper.selectByTeacherId(teacherId);
    }

    @Override
    public List<Homework> selectByTeacherName(String teacherName) {
        return homeworkMapper.selectByTeacherName(teacherName);
    }

    @Override
    public Homework selectByPK(Long id) {
        return homeworkMapper.selectByPrimaryKey(id);
    }
}
