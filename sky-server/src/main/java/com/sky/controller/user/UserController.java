package com.sky.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("/user/user")
@Slf4j
@Api(tags = "User Related Api")
public class UserController {

    @Autowired
    private WeChatProperties weChatProperties;

    public static final String REQUEST_PATH = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @ApiOperation("1. User WX Login")
    @Transactional
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("WeChat user login starting: {}", userLoginDTO);

        // find openid by request wxserver
        String openid = getOpenid(userLoginDTO.getCode());

        // login successful checking
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // new user checking
        User user = userService.selectByOpenid(openid);
        if(user == null){
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userService.insert(user);
        }

        // generate customize JWT token
        HashMap claims = new HashMap();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        // generate LoginVO
        UserLoginVO userLoginVO = new UserLoginVO().builder()
                .id(user.getId())
                .openid(openid)
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    /**
     * find openid(wxserver request) by authentication code(wx.login)
     * @param authenticationCode
     * @return
     */
    public String getOpenid(String authenticationCode){
        // request wx server
        // resource: https://developers.weixin.qq.com/doc/offiaccount/en/OA_Web_Apps/Wechat_webpage_authorization.html
        HashMap map = new HashMap();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", authenticationCode);
        map.put("grant_type", "authorization_code");

        String json = HttpClientUtil.doGet(REQUEST_PATH, map);
        log.info("WeChat server login response: {}", json);

        // parse json value
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        log.info("OpenId: {}", openid);

        return openid;
    }

}
