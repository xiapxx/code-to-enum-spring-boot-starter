package io.github.xiapxx.starter.code2enum.feign;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Author xiapeng
 * @Date 2025-01-10 13:25
 */
public class FeignEncoderInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Code2EnumFeignHolder.remove();
        Object result;
        try {
            Code2EnumFeignHolder.setIsFeign();
            result = invocation.proceed();
            return result;
        } catch (Throwable e) {
            throw e;
        } finally {
            Code2EnumFeignHolder.remove();
        }
    }

}
