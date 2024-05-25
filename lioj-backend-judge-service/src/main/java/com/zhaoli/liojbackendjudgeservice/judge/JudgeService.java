package com.zhaoli.liojbackendjudgeservice.judge;


import com.zhaoli.liojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 *
 * @author 赵立
 */
public interface JudgeService {
    /**
     * 判题
     *
     * @param questionqSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionqSubmitId);
}
