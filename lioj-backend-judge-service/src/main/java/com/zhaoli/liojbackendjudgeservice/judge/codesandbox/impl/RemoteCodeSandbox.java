package com.zhaoli.liojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.zhaoli.liojbackendcommon.common.ErrorCode;
import com.zhaoli.liojbackendcommon.exception.BusinessException;
import com.zhaoli.liojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.zhaoli.liojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.zhaoli.liojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱（实际调用接口的沙箱）
 *
 * @author 赵立
 */
@Slf4j
public class RemoteCodeSandbox implements CodeSandbox {
    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secretKey3083645195";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        try {
            System.out.println("远程代码沙箱");
            String url = "http://localhost:8090/executeCode";
            String jsonStr = JSONUtil.toJsonStr(executeCodeRequest);
            String responseStr = HttpUtil.createPost(url)
                    .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                    .body(jsonStr)
                    .execute()
                    .body();
            if (StringUtils.isBlank(responseStr)) {
                throw new BusinessException(ErrorCode.API_REQUST_ERROR, "executeCode remoteSandbox error,message=" + responseStr);
            }
            return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
        } catch (Exception e) {
            log.error("调用远程代码沙箱失败", e);
            return null;
        }

    }
}
