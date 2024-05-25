package com.zhaoli.liojbackendjudgeservice.judge;


import com.zhaoli.liojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.zhaoli.liojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.zhaoli.liojbackendjudgeservice.judge.strategy.JudgeContext;
import com.zhaoli.liojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.zhaoli.liojbackendmodel.model.codesandbox.JudgeInfo;
import com.zhaoli.liojbackendmodel.model.entity.QuestionSubmit;
import com.zhaoli.liojbackendmodel.model.enums.QuestionSubmintLanguageEnum;
import org.springframework.stereotype.Service;

/**
 * 判题管理(简化调用)
 *
 * @author 赵立
 */
@Service
public class JudgeManager {
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if(QuestionSubmintLanguageEnum.JAVA.getValue().equals(language)){
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
