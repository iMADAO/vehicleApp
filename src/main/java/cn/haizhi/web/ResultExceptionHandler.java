package cn.haizhi.web;

import cn.haizhi.enums.ResultEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.view.ResultView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Date: 2018/1/8
 * Author: Richard
 */

public class ResultExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) {
        System.out.println("经过异常处理！");
        //如果是自定义异常，否则打印系统异常
        ResultView resultView = new ResultView();
        if (exception instanceof MadaoException){
            MadaoException madaoException = (MadaoException) exception;
            resultView.setCode(madaoException.getCode());
            resultView.setHint(madaoException.getMessage());
            resultView.setData(madaoException.getId());
            System.out.println(madaoException.getMessage());
        }else{
            resultView.setCode(ResultEnum.ERROR_RESULT.getCode());
            resultView.setHint(ResultEnum.ERROR_RESULT.getHint());
            exception.printStackTrace();
        }
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.flush();
            printWriter.print(new ObjectMapper().writeValueAsString(resultView));
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
