package cn.byzk.xframe.web.common.result;

import cn.byzk.xframe.web.common.pojo.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;

/**
 * 当类和方法使用了@ResponseResultBody,  放接口的返回进行统一处理
 * <p>
 * 对异常请求进行统一的处理
 *
 * @author galaxy
 * @date 2019/10/05 16:19
 */
@Slf4j
@RestControllerAdvice
// @Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseResultBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final Class<? extends Annotation> ANNOTATION_TYPE = ResultJson.class;

    @Autowired
    private ObjectMapper objectMapper;

    /** 判断类或者方法是否使用了 @ResponseResultBody */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        //return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ANNOTATION_TYPE) || returnType.hasMethodAnnotation(ANNOTATION_TYPE);
        return true;
    }

    /** 当类或者方法使用了 @ResponseResultBody 就会调用这个方法 */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {


        //获取返回值类型
        //String returnClassType = returnType.getParameterType().getSimpleName();

        if (body instanceof BaseResponse) {
            BaseResponse baseResponse = (BaseResponse) body;
            StringBuilder sb = new StringBuilder();
            sb.append("请求的服务端方法:").append(((ServletServerHttpRequest) request).getServletRequest().getRequestURI());
            return Result.failure(baseResponse.getCode(), baseResponse.getMessage(), sb.toString());
        } else if (body instanceof Result) {
            return body;
        } else {
            return Result.success(body);
        }




//        if (body instanceof Result) {
//            return body;
//        }
//        return Result.success(body);
    }


}
