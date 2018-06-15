package cn.haizhi.util;

import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.exception.MadaoException;
import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

    public static void SendMessage(String phone,HttpSession session){
        //1、检验手机号码是否为空
        if (phone==null && phone==""){
            throw new MadaoException(ErrorEnum.PARAM_ERROR);
        }
        //2、判断手机号码的格式是否正确
        Pattern pattern = Pattern.compile("^((17[0-9])|(14St [0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.matches()){
            throw new MadaoException(ErrorEnum.PARAM_ERROR);
        }

        String account = "N7911438";
        String pswd = "ChuangLan168";
        //请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
        String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
        //产生随机验证码
        String smsCode = (""+Math.random()*1000000).substring(0,6);
        //把验证码保存到session里面
        session.setAttribute(Const.VALIDATECODE,smsCode);
        session.setAttribute(Const.USER_PHONE, phone);
        //设置session的有效时间为300秒
        session.setMaxInactiveInterval(5*60);
        // 短信内容
        //String msg = "【便易菜市】尊敬的用户，您好，您正在注册便易菜市APP，验证码为：" +"56564"+ "，若非本人操作请忽略此短信。";
        String msg = "【253云通讯】尊敬的用户，您好，您正在注册便易菜市APP,验证码为："+smsCode+"，若非本人操作请忽略此短信。";

        //状态报告
        String report= "true";
        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone,report);
        String requestJson = JSON.toJSONString(smsSingleRequest);
        System.out.println("before request string is: " + requestJson);
        String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
        System.out.println("response after request result is :" + response);
        SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
        System.out.println("response  toString is :" + smsSingleResponse);
    }

}
