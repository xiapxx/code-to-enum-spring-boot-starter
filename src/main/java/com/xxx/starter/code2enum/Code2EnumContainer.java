package com.xxx.starter.code2enum;

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

    Code2EnumContainer(Class<ENUM> enumClazz){
        this.code2EnumMap = Stream.of(enumClazz.getEnumConstants()).collect(Collectors.toMap(item -> item.getCode(), item -> item, (o, n) -> n));
        this.enumList = Stream.of(enumClazz.getEnumConstants()).collect(Collectors.toList());
    }

    ENUM toEnum(CODE code){
        return code2EnumMap.get(code);
    }

    List<ENUM> getEnumList(){
        return this.enumList;
    }

}
