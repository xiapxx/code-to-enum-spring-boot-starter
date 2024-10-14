package io.github.xiapxx.starter.code2enum.webserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import java.util.Set;

/**
 * @Author xiapeng
 * @Date 2024-03-19 13:16
 */
public class Code2EnumSerializerRegister<T extends Code2Enum> implements BeanPostProcessor {

    private Set<Class<? extends Code2Enum>> enumClassSet;

    public Code2EnumSerializerRegister(Set<Class<? extends Code2Enum>> enumClassSet) {
        this.enumClassSet = enumClassSet;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof RequestMappingHandlerAdapter){
            RequestMappingHandlerAdapter requestMappingHandlerAdapter = (RequestMappingHandlerAdapter) bean;
            for (HttpMessageConverter<?> messageConverter : requestMappingHandlerAdapter.getMessageConverters()) {
                if(!(messageConverter instanceof MappingJackson2HttpMessageConverter)){
                    continue;
                }
                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) messageConverter;
                mappingJackson2HttpMessageConverter.getObjectMapper().registerModule(newSimpleModule());
            }
        }
        if(bean instanceof ObjectMapper){
            ObjectMapper objectMapper = (ObjectMapper) bean;
            objectMapper.registerModule(newSimpleModule());
        }
        if(bean instanceof ConfigurableConversionService){
            ConfigurableConversionService conversionService = (ConfigurableConversionService) bean;
            addConverter(conversionService);
        }
        return bean;
    }

    private void addConverter(ConfigurableConversionService conversionService){
        for (Class<? extends Code2Enum> enumClass : enumClassSet) {
            conversionService.addConverter(new Code2EnumConverter(enumClass));
        }
    }

    private SimpleModule newSimpleModule(){
        SimpleModule simpleModule = new SimpleModule();
        for (Class<? extends Code2Enum> enumClass : enumClassSet) {
            Class<T> itemEnumClass = (Class<T>) enumClass;
            simpleModule.addSerializer(itemEnumClass, new Code2EnumSerializer<>());
            simpleModule.addDeserializer(itemEnumClass, new Code2EnumDeserializer<>(itemEnumClass));
        }
        return simpleModule;
    }

}
