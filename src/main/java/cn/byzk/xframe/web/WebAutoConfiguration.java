package cn.byzk.xframe.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class WebAutoConfiguration implements DisposableBean {
    @Override
    public void destroy() throws Exception {

    }
}
