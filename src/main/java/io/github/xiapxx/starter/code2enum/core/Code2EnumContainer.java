package io.github.xiapxx.starter.code2enum.core;

import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author xiapeng
 * @Date 2024-03-14 17:27
 */
class Code2EnumContainer<CODE, ENUM extends Code2Enum<CODE>> {

    private Map<CODE, ENUM> code2EnumMap;

    private List<ENUM> enumList;

    Class<CODE> codeClass;

    Code2EnumContainer(Class<ENUM> enumClazz){
        this.code2EnumMap = Stream.of(enumClazz.getEnumConstants()).collect(Collectors.toMap(item -> item.getCode(), item -> item, (o, n) -> n));
        this.enumList = Stream.of(enumClazz.getEnumConstants()).collect(Collectors.toList());
        this.codeClass = this.enumList.isEmpty() ? null
                : (Class<CODE>) this.enumList.stream().filter(item -> item.getCode() != null).map(item -> item.getCode()).findAny().get().getClass();
    }

    boolean isEmpty(){
        return this.codeClass == null;
    }

    boolean isStringCode(){
        return this.codeClass == String.class;
    }

    boolean isIntegerCode(){
        return this.codeClass == Integer.class;
    }

    boolean isLongCode(){
        return this.codeClass == Long.class;
    }

    ENUM toEnum(CODE code){
        return code2EnumMap.get(code);
    }

    List<ENUM> getEnumList(){
        return this.enumList;
    }

}
