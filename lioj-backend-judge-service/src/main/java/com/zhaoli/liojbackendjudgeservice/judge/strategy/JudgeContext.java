package com.zhaoli.liojbackendjudgeservice.judge.strategy;

import com.zhaoli.liojbackendmodel.model.codesandbox.JudgeInfo;
import com.zhaoli.liojbackendmodel.model.dto.question.JudgeCase;
import com.zhaoli.liojbackendmodel.model.entity.Question;
import com.zhaoli.liojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 *
 * @author 赵立
 */
@Data
public class JudgeContext {
    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
    /**
     * 输入用例
     */
    private List<String> inputList;
    /**
     * 输出用例
     */
    private List<String> outputList;
    /**
     * 题目信息
     */
    private Question question;
    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCaseList;
    /**
     * 题目提交信息
     */
    private QuestionSubmit questionSubmit;
}
