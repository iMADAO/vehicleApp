package cn.haizhi.inters;


import cn.haizhi.bean.User;
import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.util.Const;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {


    //进入 Handler方法之前执行
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //1、判断用户是否登录
        User user = (User) httpServletRequest.getSession().getAttribute(Const.CURRENT_USER);
        System.out.println(user + "----------------------------------");
        //2、取不到用户信息
        if(null == user){
            throw new MadaoException(ErrorEnum.USER_NOT_LOGIN);
            //返回需要登录的异常
//            ResultView resultView = new ResultView();
//            resultView.setCode(UserEnum.TOLOGIN_RESULT.getCode());
//            resultView.setHint(UserEnum.TOLOGIN_RESULT.getHint());
//            try {
//                httpServletResponse.setCharacterEncoding("UTF-8");
//                httpServletResponse.setContentType("application/json; charset=UTF-8");
//                PrintWriter printWriter = httpServletResponse.getWriter();
//                printWriter.flush();
//                printWriter.print(new ObjectMapper().writeValueAsString(resultView));
//                printWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        //返回值决定handler是否执行，true执行,false不执行
        return true;
    }

    //进入Handler方法之后，返回modelAndView之前执行
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    //执行Handler完成执行此方法
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
