package io.github.xiapxx.starter.code2enum.holder;

import io.github.xiapxx.starter.code2enum.annotation.Code2EnumConfig;
import io.github.xiapxx.starter.code2enum.constants.Code2EnumConstants;
import io.github.xiapxx.starter.code2enum.enums.WebSerializerType;
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


    public Code2EnumHolderConfigurator(Set<Class<? extends Code2Enum>> enumClassSet, WebSerializerType webSerializerType) {
        this.enumClassSet = enumClassSet;
        Code2EnumHolder.webSerializerType = webSerializerType == null ? WebSerializerType.JSON : webSerializerType;
    }

    /**
     * 初始化枚举数据
     */
    public void initEnumData() {
        for (Class<? extends Code2Enum> enumClass : enumClassSet) {
            Code2Enum[] code2Enums = enumClass.getEnumConstants();

            Map<String, Code2Enum> code2EnumMap = Stream.of(code2Enums).collect(Collectors.toMap(item -> item.getCode(), item -> item, (o, n) -> n));
            Code2EnumHolder.enumClass2Code2DataMap.putIfAbsent(enumClass, code2EnumMap);

            List<Code2Enum> code2EnumList = Stream.of(code2Enums).collect(Collectors.toList());
            Code2EnumHolder.enumClass2DataListMap.putIfAbsent(enumClass.getName(), code2EnumList);

            loadAlias2DataListMap(enumClass, code2EnumList);
        }
    }

    /**
     * 加载Code2EnumHolder.alias2DataListMap
     *
     * @param enumClass enumClass
     * @param code2EnumList code2EnumList
     */
    private void loadAlias2DataListMap(Class<? extends Code2Enum> enumClass, List<Code2Enum> code2EnumList) {
        Code2EnumConfig code2EnumConfig = enumClass.getAnnotation(Code2EnumConfig.class);
        if (code2EnumConfig == null
                || Code2EnumConstants.EMPTY.equals(code2EnumConfig.alias())) {
            return;
        }
        List<Code2Enum> oldCode2EnumList = Code2EnumHolder.alias2DataListMap.get(code2EnumConfig.alias());
        if (oldCode2EnumList != null && oldCode2EnumList.size() > 0) {
            Class oldEnumClass = oldCode2EnumList.get(0).getClass();
            if (enumClass != oldEnumClass) {
                throw new IllegalArgumentException(oldEnumClass.getName() + "与" + enumClass.getName() + "的别名冲突 : " + code2EnumConfig.alias());
            }
        }
        Code2EnumHolder.alias2DataListMap.put(code2EnumConfig.alias(), code2EnumList);
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
