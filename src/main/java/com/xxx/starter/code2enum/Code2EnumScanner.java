package com.xxx.starter.code2enum;

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
@Import(Code2EnumRegister.class)
public @interface Code2EnumScanner {

    String[] basePackages();

}
