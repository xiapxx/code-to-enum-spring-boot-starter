package io.github.xiapxx.starter.code2enum.annotation;

import io.github.xiapxx.starter.code2enum.Code2EnumRegister;
import io.github.xiapxx.starter.code2enum.feign.Code2EnumFeignAutoConfiguration;
import org.springframework.context.annotation.Import;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扫描【实现Code2Enum接口的枚举】
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({Code2EnumRegister.class, Code2EnumFeignAutoConfiguration.class})
public @interface Code2EnumScanner {

    String[] basePackages();

}
