package com.xxx.starter.code2enum;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author xiapeng
 * @Date 2024-01-05 09:14
 */
public class ClassPathCode2EnumScanner extends ClassPathBeanDefinitionScanner {

    public ClassPathCode2EnumScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        setBeanNameGenerator((definition, registry) -> definition.getBeanClassName());
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if (beanDefinitionHolders.isEmpty()) {
            return beanDefinitionHolders;
        }

        AbstractBeanDefinition definition;
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            definition = (AbstractBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            Class<? extends Code2Enum> oldBeanClass = toEnumClass(definition.getBeanClassName());
            if (!Code2Enum.class.isAssignableFrom(oldBeanClass) || !oldBeanClass.isEnum()) {
                continue;
            }
            Code2EnumHolder.register(oldBeanClass, new Code2EnumContainer(oldBeanClass));
        }
        return new HashSet<>();
    }


    private Class<? extends Code2Enum> toEnumClass(String beanClassName){
        try {
            return (Class<? extends Code2Enum>) Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
