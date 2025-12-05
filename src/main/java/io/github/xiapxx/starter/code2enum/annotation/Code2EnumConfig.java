package io.github.xiapxx.starter.code2enum.annotation;

import io.github.xiapxx.starter.code2enum.enums.EnumCodeJdbcType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static io.github.xiapxx.starter.code2enum.constants.Code2EnumConstants.EMPTY;

/**
 * @Author xiapeng
 * @Date 2025-08-13 14:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Code2EnumConfig {

    /**
     * 如果编码在数据库中是个索引字段, 如果不指定正确的jdbcType, 会导致索引失效
     *
     * @return code字段的jdbc类型
     */
    EnumCodeJdbcType codeJdbcType() default EnumCodeJdbcType.INT;

    /**
     * 有时候希望入库时, 并且枚举为null时, 可以写入一个默认的code, 实现该方法即可
     *
     * @return 为空代表null
     */
    String jdbcDefaultCode() default EMPTY;


    /**
     * 别名
     *
     * @return 枚举别名
     */
    String alias() default EMPTY;

}
