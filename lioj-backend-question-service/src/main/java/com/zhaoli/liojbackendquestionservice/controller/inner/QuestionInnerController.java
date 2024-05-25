package com.zhaoli.liojbackendquestionservice.controller.inner;

import com.zhaoli.liojbackendmodel.model.entity.Question;
import com.zhaoli.liojbackendmodel.model.entity.QuestionSubmit;
import com.zhaoli.liojbackendquestionservice.service.QuestionService;
import com.zhaoli.liojbackendquestionservice.service.QuestionSubmitService;
import com.zhaoli.liojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * QuestionFeignClient 服务内部调用的接口
 *
 * @author 赵立
 */
@RestController()
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 根据id获取题目信息
     *
     * @param questionId
     * @return
     */
    @GetMapping("/get/id")
    @Override
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    /**
     * 根据 id 获取题目提交信息
     *
     * @param questionSubmitId
     * @return
     */
    @GetMapping("/question_submitId/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    /**
     * 修改题目提交信息
     *
     * @param questionSubmit
     * @return
     */
    @PostMapping("/question_submit/update")
    @Override
    public boolean updateQuestionSubmitByid(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}
