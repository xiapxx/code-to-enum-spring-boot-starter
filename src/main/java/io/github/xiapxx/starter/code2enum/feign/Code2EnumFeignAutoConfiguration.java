package io.github.xiapxx.starter.code2enum.feign;

import feign.codec.Encoder;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import java.lang.reflect.Method;

/**
 * 支持Code2Enum的feign中作为参数传递
 *
 * @Author xiapeng
 * @Date 2025-01-10 13:22
 */
@ConditionalOnClass(Encoder.class)
public class Code2EnumFeignAutoConfiguration {

    @Bean
    public DefaultPointcutAdvisor feignEncoderInterceptor() {
        DefaultPointcutAdvisor feignEncoderAdvisor = new DefaultPointcutAdvisor();
        feignEncoderAdvisor.setPointcut(new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                if(method.getDeclaringClass() == Object.class || targetClass == null){
                    return false;
                }
                return targetClass.isAssignableFrom(Encoder.class) && method.getName().equals("encode");
            }
        });
        feignEncoderAdvisor.setAdvice(new FeignEncoderInterceptor());
        return feignEncoderAdvisor;
    }
}
