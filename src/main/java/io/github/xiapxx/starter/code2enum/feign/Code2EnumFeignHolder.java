package io.github.xiapxx.starter.code2enum.feign;

import java.util.Optional;

/**
 * @Author xiapeng
 * @Date 2025-01-10 13:24
 */
public class Code2EnumFeignHolder {

    private static final ThreadLocal<Boolean> THREAD_LOCAL = new ThreadLocal<>();

    public static void setIsFeign() {
        THREAD_LOCAL.set(Boolean.TRUE);
    }

    public static boolean isFeign() {
        return Optional.ofNullable(THREAD_LOCAL.get()).orElse(Boolean.FALSE).booleanValue();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
