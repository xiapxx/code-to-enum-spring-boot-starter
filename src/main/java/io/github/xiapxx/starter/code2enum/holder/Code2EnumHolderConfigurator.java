package io.github.xiapxx.starter.code2enum.holder;

import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author xiapeng
 * @Date 2024-09-29 13:59
 */
public class Code2EnumHolderConfigurator implements ApplicationContextAware, Ordered {

    private Set<Class<? extends Code2Enum>> enumClassSet;


    public Code2EnumHolderConfigurator(Set<Class<? extends Code2Enum>> enumClassSet) {
        this.enumClassSet = enumClassSet;
    }

    /**
     * 初始化枚举数据
     */
    public void initEnumData() {
        for (Class<? extends Code2Enum> enumClass : enumClassSet) {
            Code2Enum[] code2Enums = enumClass.getEnumConstants();

            Map<String, Code2Enum> code2EnumMap = Stream.of(code2Enums).collect(Collectors.toMap(item -> item.getCode(), item -> item, (o, n) -> n));
            Code2EnumHolder.enumClass2Code2DataMap.put(enumClass, code2EnumMap);

            List<Code2Enum> code2EnumList = Stream.of(code2Enums).collect(Collectors.toList());
            Code2EnumHolder.enumClass2DataListMap.put(enumClass.getName(), code2EnumList);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Code2EnumHolder.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
