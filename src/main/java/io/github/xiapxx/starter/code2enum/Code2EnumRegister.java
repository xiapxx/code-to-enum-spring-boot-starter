package io.github.xiapxx.starter.code2enum;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 * 注册【实现Code2Enum接口的枚举】
 *
 * @Author xiapeng
 * @Date 2024-01-05 09:53
 */
public class Code2EnumRegister implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(Code2EnumScanner.class.getName()));
        String[] basePackages = annoAttrs.getStringArray("basePackages");
        ClassPathCode2EnumScanner classPathCode2EnumScanner = new ClassPathCode2EnumScanner(registry);
        classPathCode2EnumScanner.addIncludeFilter(new AssignableTypeFilter(Code2Enum.class) {
            @Override
            protected boolean matchClassName(String className) {
                return false;
            }
        });
        classPathCode2EnumScanner.doScan(basePackages);
    }

}
