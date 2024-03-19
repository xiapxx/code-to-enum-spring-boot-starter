package io.github.xiapxx.starter.code2enum.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import io.github.xiapxx.starter.code2enum.interfaces.LanguageEnvGetter;
import java.io.IOException;

/**
 * @Author xiapeng
 * @Date 2024-03-19 13:18
 */
public class Code2EnumSerializer<T extends Code2Enum> extends JsonSerializer<T> {

    private LanguageEnvGetter languageEnvGetter;

    public Code2EnumSerializer(LanguageEnvGetter languageEnvGetter){
        this.languageEnvGetter = languageEnvGetter;
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value == null){
            gen.writeNull();
            return;
        }
        Code2EnumWrapper enumWrapper = new Code2EnumWrapper();
        enumWrapper.setCode(value.getCode());
        enumWrapper.setMessage(this.languageEnvGetter == null || this.languageEnvGetter.isChinese()
                ? value.getMessage() : value.getMessageEn()
        );
        gen.writeObject(enumWrapper);
    }

}
