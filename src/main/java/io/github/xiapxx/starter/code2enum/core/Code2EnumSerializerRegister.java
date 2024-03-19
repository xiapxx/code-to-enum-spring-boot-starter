package io.github.xiapxx.starter.code2enum.core;

import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import io.github.xiapxx.starter.code2enum.interfaces.LanguageEnvGetter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import java.util.Map;

/**
 * @Author xiapeng
 * @Date 2024-03-19 13:16
 */
public class Code2EnumSerializerRegister implements Jackson2ObjectMapperBuilderCustomizer {

    @Autowired
    private ObjectProvider<LanguageEnvGetter> languageEnvGetterObjectProvider;

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        LanguageEnvGetter languageEnvGetter = languageEnvGetterObjectProvider.getIfAvailable();
        for (Map.Entry<Class<? extends Code2Enum>, Code2EnumContainer> entry : Code2EnumHolder.enumClass2ContainerMap.entrySet()) {
            Code2EnumContainer code2EnumContainer = entry.getValue();
            if(code2EnumContainer.isEmpty()){
                continue;
            }
            builder.serializerByType(entry.getKey(), new Code2EnumSerializer<>(languageEnvGetter));
            builder.deserializerByType(entry.getKey(), new Code2EnumDeserializer<>(code2EnumContainer));
        }
    }

}
