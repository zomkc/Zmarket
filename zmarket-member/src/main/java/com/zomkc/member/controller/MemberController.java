package com.zomkc.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.zomkc.common.exception.BizCodeEnum;
import com.zomkc.member.exception.PhoneException;
import com.zomkc.member.exception.UsernameException;
import com.zomkc.member.vo.MemberUserLoginVo;
import com.zomkc.member.vo.MemberUserRegisterVo;
import com.zomkc.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zomkc.member.entity.MemberEntity;
import com.zomkc.member.service.MemberService;
import com.zomkc.common.utils.PageUtils;
import com.zomkc.common.utils.R;



/**
 * 会员
 *
 * @author zomkc
 * @email leifengyang@gmail.com
 * @date 2023-02-16 09:01:04
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping(value = "/register")
    public R register(@RequestBody MemberUserRegisterVo vo) {

        try {
            memberService.register(vo);
        } catch (PhoneException e) {
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.getCode(),BizCodeEnum.PHONE_EXIST_EXCEPTION.getMessage());
        } catch (UsernameException e) {
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.getCode(),BizCodeEnum.USER_EXIST_EXCEPTION.getMessage());
        }

        return R.ok();
    }


    @PostMapping(value = "/login")
    public R login(@RequestBody MemberUserLoginVo vo) {

        MemberEntity memberEntity = memberService.login(vo);

        if (memberEntity != null) {
            return R.ok().setData(memberEntity);
        } else {
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getMessage());
        }
    }


//    @PostMapping(value = "/oauth2/login")
//    public R oauthLogin(@RequestBody SocialUser socialUser) throws Exception {
//
//        MemberEntity memberEntity = memberService.login(socialUser);
//
//        if (memberEntity != null) {
//            return R.ok().setData(memberEntity);
//        } else {
//            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getMessage());
//        }
//    }

//    @PostMapping(value = "/weixin/login")
//    public R weixinLogin(@RequestParam("accessTokenInfo") String accessTokenInfo) {
//
//        MemberEntity memberEntity = memberService.login(accessTokenInfo);
//        if (memberEntity != null) {
//            return R.ok().setData(memberEntity);
//        } else {
//            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getMessage());
//        }
//    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
