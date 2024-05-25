package com.zhaoli.liojbackendjudgeservice.judge.codesandbox.impl;


import com.zhaoli.liojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.zhaoli.liojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.zhaoli.liojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用网上现成的代码沙箱）
 *
 * @author 赵立
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
