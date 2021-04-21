
package com.leduo.mall.service.impl;

import com.leduo.mall.common.Constants;
import com.leduo.mall.common.ServiceResultEnum;
import com.leduo.mall.controller.vo.LeDuoMallUserVO;
import com.leduo.mall.dao.MallUserMapper;
import com.leduo.mall.entity.MallUser;
import com.leduo.mall.service.LeDuoMallUserService;
import com.leduo.mall.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class LeDuoMallUserServiceImpl implements LeDuoMallUserService {

    @Autowired
    private MallUserMapper mallUserMapper;

    @Override
    public PageResult getNewBeeMallUsersPage(PageQueryUtil pageUtil) {
        List<MallUser> mallUsers = mallUserMapper.findMallUserList(pageUtil);
        int total = mallUserMapper.getTotalMallUsers(pageUtil);
        PageResult pageResult = new PageResult(mallUsers, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String register(String loginName, String password) {
        if (mallUserMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        MallUser registerUser = new MallUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        if (mallUserMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5, HttpSession httpSession) {
        MallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null && httpSession != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //昵称太长 影响页面展示
            if (user.getNickName() != null && user.getNickName().length() > 7) {
                String tempNickName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tempNickName);
            }
            LeDuoMallUserVO leDuoMallUserVO = new LeDuoMallUserVO();
            BeanUtil.copyProperties(user, leDuoMallUserVO);
            //设置购物车中的数量
            httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, leDuoMallUserVO);
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public LeDuoMallUserVO updateUserInfo(MallUser mallUser, HttpSession httpSession) {
        LeDuoMallUserVO userTemp = (LeDuoMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallUser userFromDB = mallUserMapper.selectByPrimaryKey(userTemp.getUserId());
        if (userFromDB != null) {
            userFromDB.setNickName(LeDuoMallUtils.cleanString(mallUser.getNickName()));
            userFromDB.setAddress(LeDuoMallUtils.cleanString(mallUser.getAddress()));
            userFromDB.setIntroduceSign(LeDuoMallUtils.cleanString(mallUser.getIntroduceSign()));
            if (mallUserMapper.updateByPrimaryKeySelective(userFromDB) > 0) {
                LeDuoMallUserVO leDuoMallUserVO = new LeDuoMallUserVO();
                userFromDB = mallUserMapper.selectByPrimaryKey(mallUser.getUserId());
                BeanUtil.copyProperties(userFromDB, leDuoMallUserVO);
                httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, leDuoMallUserVO);
                return leDuoMallUserVO;
            }
        }
        return null;
    }

    @Override
    public Boolean lockUsers(Integer[] ids, int lockStatus) {
        if (ids.length < 1) {
            return false;
        }
        return mallUserMapper.lockUserBatch(ids, lockStatus) > 0;
    }
}
