package cn.byzk.xframe.web.common.exception.handler;

import cn.byzk.xframe.web.common.constant.enums.ArgumentResponseEnum;
import cn.byzk.xframe.web.common.constant.enums.CommonResponseEnum;
import cn.byzk.xframe.web.common.constant.enums.ServletResponseEnum;
import cn.byzk.xframe.web.common.exception.BaseException;
import cn.byzk.xframe.web.common.exception.RaException;
import cn.byzk.xframe.web.common.i18n.UnifiedMessageSource;
import cn.byzk.xframe.web.common.pojo.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * <p>?????????????????????</p>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
@Slf4j
@Component
@ControllerAdvice
@RestControllerAdvice
//@ConditionalOnWebApplication
//@ConditionalOnMissingBean(UnifiedExceptionHandler.class)
public class UnifiedExceptionHandler {
    /**
     * ????????????
     */
    private final static String ENV_PROD = "dev";

    @Autowired
    private UnifiedMessageSource unifiedMessageSource;

    /**
     * ????????????
     */
    @Value("${spring.profiles.active}")
    private String profile;

    /**
     * ?????????????????????
     *
     * @param e ??????
     * @return
     */
    public String getMessage(BaseException e) {
        String code = "response." + e.getResponseEnum().toString();
        String message = unifiedMessageSource.getMessage(code, e.getArgs());

        if (message == null || message.isEmpty()) {
            return e.getMessage();
        }

        return message;
    }

    /**
     * ????????????
     *
     * @param e ??????
     * @return ????????????
     */
    @ExceptionHandler(value = RaException.class)
    @ResponseBody
    public ErrorResponse handleBusinessException(BaseException e) {
        log.error(e.getMessage(), e);

        return new ErrorResponse(e.getResponseEnum().getCode(), getMessage(e));
    }

    /**
     * ???????????????
     *
     * @param e ??????
     * @return ????????????
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public ErrorResponse handleBaseException(BaseException e) {
        log.error(e.getMessage(), e);

        return new ErrorResponse(e.getResponseEnum().getCode(), getMessage(e));
    }



    /**
     * Controller?????????????????????
     *
     * @param e ??????
     * @return ????????????
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            // BindException.class,
            //MethodArgumentNotValidException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    @ResponseBody
    public ErrorResponse handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        int code = CommonResponseEnum.SERVER_ERROR.getCode();
        try {
            ServletResponseEnum servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
            code = servletExceptionEnum.getCode();
        } catch (IllegalArgumentException e1) {
            log.error("class [{}] not defined in enum {}", e.getClass().getName(), ServletResponseEnum.class.getName());
        }

        if (ENV_PROD.equals(profile)) {
            // ??????????????????, ????????????????????????????????????????????????, ??????404.
            code = CommonResponseEnum.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(CommonResponseEnum.SERVER_ERROR);
            String message = getMessage(baseException);
            return new ErrorResponse(code, message);
        }

        return new ErrorResponse(code, e.getMessage());
    }


    /**
     * ??????????????????
     *
     * @param e ??????
     * @return ????????????
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ErrorResponse handleBindException(BindException e) {
        log.error("????????????????????????", e);

        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * ????????????(Valid)??????????????????????????????????????????????????????????????????
     *
     * @param e ??????
     * @return ????????????
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse handleValidException(MethodArgumentNotValidException e) {
        log.error("????????????????????????", e);

        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * ????????????????????????
     *
     * @param bindingResult ????????????
     * @return ????????????
     */
    private ErrorResponse wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();

        for (ObjectError error : bindingResult.getAllErrors()) {
            msg.append(", ");
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());

        }

        return new ErrorResponse(ArgumentResponseEnum.VALID_ERROR.getCode(), msg.substring(2));
    }

    /**
     * ???????????????
     *
     * @param e ??????
     * @return ????????????
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorResponse handleException(Exception e) {
        log.error(e.getMessage(), e);
        int code = CommonResponseEnum.SERVER_ERROR.getCode();

        if (ENV_PROD.equals(profile)) {
            // ??????????????????, ????????????????????????????????????????????????, ???????????????????????????.
            BaseException baseException = new BaseException(CommonResponseEnum.SERVER_ERROR);
            String message = getMessage(baseException);
            return new ErrorResponse(code, message);
        }

        if (e instanceof NullPointerException) {
            code = CommonResponseEnum.NULL_POINT_ERROR.getCode();
            return new ErrorResponse(code, CommonResponseEnum.NULL_POINT_ERROR.getMessage());
        }

        if (e instanceof ArrayIndexOutOfBoundsException) {
            code = CommonResponseEnum.ARRAY_INDEX_OUT_ERROR.getCode();
            return new ErrorResponse(code, CommonResponseEnum.ARRAY_INDEX_OUT_ERROR.getMessage());
        }



        return new ErrorResponse(CommonResponseEnum.SERVER_ERROR.getCode(), e.getMessage());
    }

}
