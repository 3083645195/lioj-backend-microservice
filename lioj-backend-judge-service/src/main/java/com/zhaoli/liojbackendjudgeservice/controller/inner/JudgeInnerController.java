package com.zhaoli.liojbackendjudgeservice.controller.inner;

import com.zhaoli.liojbackendjudgeservice.judge.JudgeService;
import com.zhaoli.liojbackendmodel.model.entity.QuestionSubmit;
import com.zhaoli.liojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * JudgeFeignClient 服务内部调用的接口
 *
 * @author 赵立
 */
@RestController()
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {
    @Resource
    private JudgeService judgeService;

    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    @Override
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
