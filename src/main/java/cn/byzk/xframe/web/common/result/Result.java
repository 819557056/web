package cn.byzk.xframe.web.common.result;

import cn.byzk.xframe.web.common.result.exception.ResultStatus;
import lombok.Getter;
import lombok.ToString;


/**
 * 后端返回给前端的json格式
 *  todo 返回格式参考 org.springframework.boot.web.servlet.error.DefaultErrorAttributes
 *
 * @author galaxy
 * @date 2019/10/05 16:19
 */
@Getter
@ToString
public class Result<T> {
    /** 业务错误码 */
    private Integer code;
    /** 信息描述 */
    private String mssage;
    /** 返回参数 */
    private T data;

    private Result(ResultStatus resultStatus, T data) {
        this.code = resultStatus.getCode();
        this.mssage = resultStatus.getMessage();
        this.data = data;
    }

    private Result(int code, String msg, T data) {
        this.code = code;
        this.mssage = msg;
        this.data = data;
    }

    /** 业务成功返回业务代码和描述信息 */
    public static Result<Void> success() {
        return new Result<Void>(ResultStatus.SUCCESS, null);
    }

    /** 业务成功返回业务代码,描述和返回的参数 */
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultStatus.SUCCESS, data);
    }

    /** 业务成功返回业务代码,描述和返回的参数 */
    public static <T> Result<T> success(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return success(data);
        }
        return new Result<T>(resultStatus, data);
    }

    /** 业务异常返回业务代码和描述信息 */
    public static <T> Result<T> failure() {
        return new Result<T>(ResultStatus.INTERNAL_SERVER_ERROR, null);
    }

    /** 业务异常返回业务代码,描述和返回的参数 */
    public static <T> Result<T> failure(ResultStatus resultStatus) {
        return failure(resultStatus, null);
    }

    public static <T> Result<T> failure(int code, String msg) {
        return failure(code, msg, null);
    }

    /** 业务异常返回业务代码,描述和返回的参数 */
    public static <T> Result<T> failure(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return new Result<T>(ResultStatus.INTERNAL_SERVER_ERROR, null);
        }
        return new Result<T>(resultStatus, data);
    }

    public static <T> Result<T> failure(int code, String msg, T data) {
        if (msg == null) {
            return new Result<T>(ResultStatus.INTERNAL_SERVER_ERROR, null);
        }
        return new Result<T>(code, msg, data);
    }
}