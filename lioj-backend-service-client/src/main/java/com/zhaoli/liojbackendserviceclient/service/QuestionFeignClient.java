package com.zhaoli.liojbackendserviceclient.service;

import com.zhaoli.liojbackendmodel.model.entity.Question;
import com.zhaoli.liojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 立
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2024-05-06 19:51:43
 */
@FeignClient(name = "lioj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    /**
     * 根据id获取题目信息
     *
     * @param questionId
     * @return
     */
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    /**
     * 根据 id 获取题目提交信息
     *
     * @param questionSubmitId
     * @return
     */
    @GetMapping("/question_submitId/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    /**
     * 修改题目提交信息
     *
     * @param questionSubmit
     * @return
     */
    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitByid(@RequestBody QuestionSubmit questionSubmit);
}
