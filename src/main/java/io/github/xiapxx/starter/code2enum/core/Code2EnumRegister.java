package io.github.xiapxx.starter.code2enum.core;

import io.github.xiapxx.starter.code2enum.annotation.Code2EnumScanner;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 注册【实现Code2Enum接口的枚举】
 *
 * @Author xiapeng
 * @Date 2024-01-05 09:53
 */
public class Code2EnumRegister implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importMetadata) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(Code2EnumScanner.class.getName()));
        String[] basePackages = annoAttrs.getStringArray("basePackages");
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(basePackages));
        Set<Class<? extends Code2Enum>> code2EnumClassSet = reflections.getSubTypesOf(Code2Enum.class);
        for (Class<? extends Code2Enum> enumClass : code2EnumClassSet) {
            if(!Code2Enum.class.isAssignableFrom(enumClass) || !enumClass.isEnum()){
                continue;
            }
            Code2EnumHolder.register(enumClass, new Code2EnumContainer<>(enumClass));
        }

        if(Code2EnumHolder.enumClass2ContainerMap.isEmpty()){
            return null;
        }

        List<String> importsList = getImportList();
        return importsList.isEmpty() ? null : importsList.toArray(new String[importsList.size()]);
    }

    private List<String> getImportList(){
        List<String> importsList = new ArrayList<>();
        String typeHandlerRegister = getTypeHandlerRegister();
        if(typeHandlerRegister != null){
            importsList.add(typeHandlerRegister);
        }
        try {
            Class.forName("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter");
            importsList.add(Code2EnumSerializerRegister.class.getName());
        } catch (ClassNotFoundException e) {
        }
        return importsList;
    }


    public String getTypeHandlerRegister() {
        String typeHandlerRegister = null;
        try {
            Class.forName("com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer");
            typeHandlerRegister = Code2EnumMybatisPlusTypeHandlerRegister.class.getName();
        } catch (ClassNotFoundException e) {
        }

        if(typeHandlerRegister == null){
            try {
                Class.forName("org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer");
                typeHandlerRegister = Code2EnumMybatisTypeHandlerRegister.class.getName();
            } catch (ClassNotFoundException e) {
            }
        }
        return typeHandlerRegister;
    }

}
