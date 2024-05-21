package com.zhaoli.liojbackendmodel.model.dto.questionsubmit;

import com.zhaoli.liojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author zhaoli
 * @from
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {
    /**
     * 编程语言
     */
    private String language;
    /**
     * 判题状态
     */
    private Integer status;
    /**
     * 题目 id
     */
    private Long questionId;
    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}