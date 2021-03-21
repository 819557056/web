package cn.byzk.xframe.web.common.exception.assertion;

import cn.byzk.xframe.web.common.constant.IResponseEnum;
import cn.byzk.xframe.web.common.exception.BaseException;
import cn.byzk.xframe.web.common.exception.RaException;

import java.text.MessageFormat;

/**
 * <p>业务异常断言</p>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new RaException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new RaException(this, args, msg, t);
    }

}
