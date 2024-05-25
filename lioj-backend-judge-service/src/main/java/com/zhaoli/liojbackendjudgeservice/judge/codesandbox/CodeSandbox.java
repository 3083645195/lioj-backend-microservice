package com.zhaoli.liojbackendjudgeservice.judge.codesandbox;


import com.zhaoli.liojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.zhaoli.liojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 *
 * @author 赵立
 */
public interface CodeSandbox {
    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
