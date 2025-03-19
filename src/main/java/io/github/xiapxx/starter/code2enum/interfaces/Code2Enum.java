package io.github.xiapxx.starter.code2enum.interfaces;

import io.github.xiapxx.starter.code2enum.enums.EnumCodeJdbcType;
import io.github.xiapxx.starter.code2enum.holder.Code2EnumHolder;
import org.springframework.util.StringUtils;

/**
 * 枚举类需实现该接口
 *
 * @Author xiapeng
 * @Date 2024-03-14 17:12
 */
public interface Code2Enum {

    /**
     * 需保证一个枚举类中唯一
     *
     * @return 一个枚举类中的唯一编码
     */
    String getCode();

    default Integer getIntCode() {
        String code = getCode();
        if(!StringUtils.hasLength(code)){
            return null;
        }
        return Integer.valueOf(code);
    }

    default Long getLongCode(){
        String code = getCode();
        if(!StringUtils.hasLength(code)){
            return null;
        }
        return Long.valueOf(code);
    }

    default EnumCodeJdbcType enumCodeJdbcType(){
        return EnumCodeJdbcType.INT;
    }

    /**
     * 有时候希望入库时, 并且枚举为null时, 可以写入一个默认的code, 实现该方法即可
     *
     * @return 枚举为null时, 默认code
     */
    default String jdbcDefaultCode() {
        return null;
    }

    default String getMessage(){
        return null;
    };

    default String getMessageEn(){
        return null;
    }

    default String toActualMessage() {
        return Code2EnumHolder.getMessage(this);
    }

}
