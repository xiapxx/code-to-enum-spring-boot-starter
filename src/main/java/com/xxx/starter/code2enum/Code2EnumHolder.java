package com.xxx.starter.code2enum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Code2Enum的枚举持有者
 *
 * @Author xiapeng
 * @Date 2024-03-14 17:59
 */
public class Code2EnumHolder {

    private static final Map<Class<? extends Code2Enum>, Code2EnumContainer> enumClass2ContainerMap = new HashMap<>();

    private static final Map<String, Code2EnumContainer> enumClassName2ContainerMap = new HashMap<>();

    /**
     * 注册Code2Enum的枚举
     *
     * @param enumClazz 枚举class对象
     * @param code2EnumContainer code2EnumContainer
     */
    static void register(Class enumClazz, Code2EnumContainer code2EnumContainer){
        enumClass2ContainerMap.putIfAbsent(enumClazz, code2EnumContainer);
        enumClassName2ContainerMap.putIfAbsent(enumClazz.getName(), code2EnumContainer);
    }

    /**
     * code转换成枚举
     *
     * @param enumClazz 枚举class对象
     * @param code 枚举内的唯一编码
     * @return 枚举对象
     */
    public static <CODE, ENUM extends Code2Enum<CODE>> ENUM toEnum(Class<ENUM> enumClazz, CODE code){
        if(!enumClass2ContainerMap.containsKey(enumClazz)){
            return null;
        }
        return (ENUM) enumClass2ContainerMap.get(enumClazz).toEnum(code);
    }

    /**
     * code转换成枚举
     *
     * @param enumClassName 枚举className
     * @param code 枚举内的唯一编码
     * @return 枚举对象
     */
    public static <CODE> Code2Enum<CODE> toEnum(String enumClassName, CODE code){
        if(!enumClassName2ContainerMap.containsKey(enumClassName)){
            return null;
        }
        return (Code2Enum<CODE>) enumClassName2ContainerMap.get(enumClassName).toEnum(code);
    }

    /**
     * 获取枚举名称对应的所有枚举
     *
     * @param enumClassName enumClassName2ContainerMap
     * @return 枚举名称对应的所有枚举
     */
    public static List<Code2Enum> getAll(String enumClassName){
        if(!enumClassName2ContainerMap.containsKey(enumClassName)){
            return new ArrayList<>();
        }
        return enumClassName2ContainerMap.get(enumClassName).getEnumList();
    }
}
