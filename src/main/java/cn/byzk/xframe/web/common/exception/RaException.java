package cn.byzk.xframe.web.common.exception;


import cn.byzk.xframe.web.common.constant.IResponseEnum;

/**
 * <p>业务异常</p>
 * <p>业务处理时，出现异常，可以抛出该异常</p>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public class RaException extends  BaseException {

    private static final long serialVersionUID = 1L;

    public RaException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public RaException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }

    public RaException(int code , String msg) {
        super(code, msg);
    }

    private static String appendStr(String ...msg) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < msg.length; i++) {
            sb.append(msg[i]);
        }
        return sb.toString();
    }
}