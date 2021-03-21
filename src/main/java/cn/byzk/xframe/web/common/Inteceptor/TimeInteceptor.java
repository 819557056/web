package cn.byzk.xframe.web.common.Inteceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class TimeInteceptor implements HandlerInterceptor {
    

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {

        long beginTime = System.currentTimeMillis();
        request.setAttribute("beginTime", beginTime);

        return true;


    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        long beginTime = (long) httpServletRequest.getAttribute("beginTime");

        long endTime = System.currentTimeMillis();

        HandlerMethod hm = (HandlerMethod) o;
        String s = hm.toString();
        int b = s.indexOf("> ");
        int e = s.indexOf("(");
        s = s.substring(b+2, e);
        log.info("[{}] 执行时间: {} ms.", s, (endTime - beginTime));
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
