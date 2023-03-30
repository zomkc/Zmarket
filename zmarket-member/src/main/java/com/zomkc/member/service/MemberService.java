package com.zomkc.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.member.entity.MemberEntity;
import com.zomkc.member.exception.PhoneException;
import com.zomkc.member.exception.UsernameException;
import com.zomkc.member.vo.MemberUserLoginVo;
import com.zomkc.member.vo.MemberUserRegisterVo;
import com.zomkc.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-16 09:01:04
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberUserRegisterVo vo);
    /**
     * 判断邮箱是否重复
     * @param phone
     * @return
     */
    void checkPhoneUnique(String phone) throws PhoneException;

    /**
     * 判断用户名是否重复
     * @param userName
     * @return
     */
    void checkUserNameUnique(String userName) throws UsernameException;

    MemberEntity login(MemberUserLoginVo vo);

    //MemberEntity login(SocialUser socialUser) throws Exception;
}

