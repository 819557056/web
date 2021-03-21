package cn.byzk.xframe.web.common.result;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * 使用这个注解就可以返回统一的json格式,  用于类和方法上
 *
 * @author galaxy
 * @date 2019/10/05 16:19
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ResponseBody
public @interface ResultJson {

}
