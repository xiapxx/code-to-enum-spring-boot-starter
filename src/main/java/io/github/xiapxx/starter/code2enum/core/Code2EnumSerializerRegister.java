package io.github.xiapxx.starter.code2enum.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import io.github.xiapxx.starter.code2enum.interfaces.LanguageEnvGetter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import java.util.Map;

/**
 * @Author xiapeng
 * @Date 2024-03-19 13:16
 */
public class Code2EnumSerializerRegister implements BeanPostProcessor {

    @Autowired
    private ObjectProvider<LanguageEnvGetter> languageEnvGetterObjectProvider;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof ObjectMapper){
            ObjectMapper objectMapper = (ObjectMapper) bean;
            objectMapper.registerModule(register());
        }
        return bean;
    }

    private SimpleModule register(){
        LanguageEnvGetter languageEnvGetter = languageEnvGetterObjectProvider.getIfAvailable();
        SimpleModule simpleModule = new SimpleModule();
        for (Map.Entry<Class<? extends Code2Enum>, Code2EnumContainer> entry : Code2EnumHolder.enumClass2ContainerMap.entrySet()) {
            Code2EnumContainer code2EnumContainer = entry.getValue();
            if(code2EnumContainer.isEmpty()){
                continue;
            }
            simpleModule.addSerializer(entry.getKey(), new Code2EnumSerializer<>(languageEnvGetter));
            simpleModule.addDeserializer(entry.getKey(), new Code2EnumDeserializer<>(code2EnumContainer));
        }
        return simpleModule;
    }

}
