package com.zhaoli.liojbackenduserservice.controller.inner;

import com.zhaoli.liojbackendmodel.model.entity.User;
import com.zhaoli.liojbackendserviceclient.service.UserFeignClient;
import com.zhaoli.liojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * UserFeignClient 服务内部调用的接口
 *
 * @author 赵立
 */
@RestController()
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {
    @Resource
    private UserService userService;

    /**
     * 根据 id 获取用户
     *
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

    /**
     * 根据 id 获取用户列表
     *
     * @param idList
     * @return
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList) {
        return userService.listByIds(idList);
    }
}
