package com.zhaoli.liojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.zhaoli.liojbackendcommon.common.ErrorCode;
import com.zhaoli.liojbackendcommon.exception.BusinessException;
import com.zhaoli.liojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.zhaoli.liojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.zhaoli.liojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.zhaoli.liojbackendjudgeservice.judge.strategy.JudgeContext;
import com.zhaoli.liojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.zhaoli.liojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.zhaoli.liojbackendmodel.model.codesandbox.JudgeInfo;
import com.zhaoli.liojbackendmodel.model.dto.question.JudgeCase;
import com.zhaoli.liojbackendmodel.model.entity.Question;
import com.zhaoli.liojbackendmodel.model.entity.QuestionSubmit;
import com.zhaoli.liojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.zhaoli.liojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 赵立
 */
@Service
public class JudgeServiceImpl implements JudgeService {
    @Value("${codesandbox.type:example}")
    private String type;
    @Resource
    private QuestionFeignClient questionFeignClient;
    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long questionqSubmitId) {
        //1.传入题目的提交 id，获取到对应的题目、提交信息(包含代码、编程语言等)
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionqSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目信息不存在");
        }
        //2.如果不为“待判题”状态,就不用重复执行了
        if (!Objects.equals(questionSubmit.getStatus(), QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        //3.更改判题状态（题目提交状态）为“判题中”，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionqSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean updateResult = questionFeignClient.updateQuestionSubmitByid(questionSubmitUpdate);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题状态更新失败");
        }
        //4.调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        //获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        //调用沙箱
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        //5.根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);
        //执行判题
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        //todo 可能还有其他的异常情况
        //6.修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionqSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        updateResult = questionFeignClient.updateQuestionSubmitByid(questionSubmitUpdate);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题状态更新失败");
        }
        //从数据库中取出最新的状态
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionId);
        return questionSubmitResult;
    }
}
