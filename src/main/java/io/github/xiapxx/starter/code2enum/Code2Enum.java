package io.github.xiapxx.starter.code2enum;

/**
 * 枚举类需实现该接口
 *
 * @Author xiapeng
 * @Date 2024-03-14 17:12
 */
public interface Code2Enum<T> {

    /**
     * 需保证一个枚举类中唯一
     *
     * @return 一个枚举类中的唯一编码; 编码类型支持String, Integer, Long
     */
    T getCode();

    default String getMessage(){
        return null;
    };

    default String getMessageEn(){
        return null;
    }

}
