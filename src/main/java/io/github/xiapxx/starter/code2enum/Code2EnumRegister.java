package io.github.xiapxx.starter.code2enum;

import io.github.xiapxx.starter.code2enum.annotation.Code2EnumScanner;
import io.github.xiapxx.starter.code2enum.enums.WebSerializerType;
import io.github.xiapxx.starter.code2enum.holder.Code2EnumHolderConfigurator;
import io.github.xiapxx.starter.code2enum.webserializer.Code2EnumSerializerRegister;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import io.github.xiapxx.starter.code2enum.mybatistypehandler.EnumMybatisPlusTypeHandlerRegister;
import io.github.xiapxx.starter.code2enum.mybatistypehandler.EnumMybatisTypeHandlerRegister;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<Class<? extends Code2Enum>> code2EnumClassSet = scanEnumClass(annoAttrs.getStringArray("basePackages"));
        if(code2EnumClassSet == null || code2EnumClassSet.isEmpty()){
            return;
        }

        String beanNamePrefix = importMetadata.getClassName() + "#";

        registerCode2EnumHolderConfigurator(registry, beanNamePrefix, code2EnumClassSet, annoAttrs.getEnum("webSerializerType"));

        registerMybatisTypeHandlerRegister(registry, beanNamePrefix, code2EnumClassSet);

        registerWebDeserializerRegister(registry, beanNamePrefix, code2EnumClassSet);
    }


    /**
     * 注册Code2EnumHolder的配置类
     *
     * @param registry registry
     * @param beanNamePrefix beanNamePrefix
     * @param code2EnumClassSet code2EnumClassSet
     * @param webSerializerType webSerializerType
     */
    private void registerCode2EnumHolderConfigurator(BeanDefinitionRegistry registry,
                                                     String beanNamePrefix,
                                                     Set<Class<? extends Code2Enum>> code2EnumClassSet,
                                                     WebSerializerType webSerializerType) {
        Code2EnumHolderConfigurator code2EnumHolderConfigurator = new Code2EnumHolderConfigurator(code2EnumClassSet, webSerializerType);
        code2EnumHolderConfigurator.initEnumData();

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(Code2EnumHolderConfigurator.class, () -> code2EnumHolderConfigurator);
        registry.registerBeanDefinition(beanNamePrefix + Code2EnumHolderConfigurator.class.getName(), beanDefinitionBuilder.getBeanDefinition());
    }


    /**
     * 如果项目依赖了mybatis或mybatis plus, 那么将支持数据库中查询出的code转换为枚举对象
     *
     * @param registry registry
     * @param beanNamePrefix beanNamePrefix
     * @param code2EnumClassSet 枚举类
     */
    private void registerMybatisTypeHandlerRegister(BeanDefinitionRegistry registry,
                                                    String beanNamePrefix,
                                                    Set<Class<? extends Code2Enum>> code2EnumClassSet){
        Class typeHandlerRegisterClass = getTypeHandlerRegister();
        if(typeHandlerRegisterClass == null){
            return;
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(typeHandlerRegisterClass);
        beanDefinitionBuilder.addConstructorArgValue(code2EnumClassSet);
        registry.registerBeanDefinition(beanNamePrefix + typeHandlerRegisterClass.getName(), beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * 如果项目依赖了spring-boot-starter-web, 那么将支持前端传入的code转换为枚举对象
     *
     * @param registry registry
     * @param beanNamePrefix beanNamePrefix
     * @param code2EnumClassSet 枚举类
     */
    private void registerWebDeserializerRegister(BeanDefinitionRegistry registry,
                                                 String beanNamePrefix,
                                                 Set<Class<? extends Code2Enum>> code2EnumClassSet) {
        try {
            Class.forName("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter");
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Code2EnumSerializerRegister.class);
            beanDefinitionBuilder.addConstructorArgValue(code2EnumClassSet);
            registry.registerBeanDefinition(beanNamePrefix + Code2EnumSerializerRegister.class.getName(), beanDefinitionBuilder.getBeanDefinition());
        } catch (ClassNotFoundException e) {
        }
    }

    /**
     * 扫描枚举类
     *
     * @param basePackages basePackages
     * @return 字典类
     */
    private Set<Class<? extends Code2Enum>> scanEnumClass(String[] basePackages){
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(basePackages));
        Set<Class<? extends Code2Enum>> code2EnumClassSet = reflections.getSubTypesOf(Code2Enum.class);

        return code2EnumClassSet.stream().filter(item -> item.isEnum()).collect(Collectors.toSet());
    }

    public Class getTypeHandlerRegister() {
        Class typeHandlerRegisterClass = null;
        try {
            Class.forName("com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer");
            typeHandlerRegisterClass = EnumMybatisPlusTypeHandlerRegister.class;
        } catch (ClassNotFoundException e) {
        }

        if(typeHandlerRegisterClass == null){
            try {
                Class.forName("org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer");
                typeHandlerRegisterClass = EnumMybatisTypeHandlerRegister.class;
            } catch (ClassNotFoundException e) {
            }
        }
        return typeHandlerRegisterClass;
    }

}
