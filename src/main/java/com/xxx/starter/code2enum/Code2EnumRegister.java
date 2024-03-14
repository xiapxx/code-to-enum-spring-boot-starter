package com.xxx.starter.code2enum;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import java.util.Set;

/**
 * 注册【实现Code2Enum接口的枚举】
 *
 * @Author xiapeng
 * @Date 2024-01-05 09:53
 */
public class Code2EnumRegister implements ImportAware {

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(Code2EnumScanner.class.getName()));
        String[] basePackages = annoAttrs.getStringArray("basePackages");
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(basePackages));
        Set<Class<? extends Code2Enum>> code2EnumClassSet = reflections.getSubTypesOf(Code2Enum.class);
        for (Class<? extends Code2Enum> enumClass : code2EnumClassSet) {
            if (!Code2Enum.class.isAssignableFrom(enumClass) || !enumClass.isEnum()) {
                continue;
            }
            Code2EnumHolder.register(enumClass, new Code2EnumContainer(enumClass));
        }
    }

}
