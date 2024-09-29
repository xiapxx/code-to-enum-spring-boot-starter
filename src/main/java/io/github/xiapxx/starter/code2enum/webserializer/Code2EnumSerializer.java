package io.github.xiapxx.starter.code2enum.webserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.xiapxx.starter.code2enum.entity.Code2EnumWrapper;
import io.github.xiapxx.starter.code2enum.holder.Code2EnumHolder;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import java.io.IOException;

/**
 * @Author xiapeng
 * @Date 2024-03-19 13:18
 */
public class Code2EnumSerializer<T extends Code2Enum> extends JsonSerializer<T> {

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value == null){
            gen.writeNull();
            return;
        }
        Code2EnumWrapper enumWrapper = new Code2EnumWrapper();
        enumWrapper.setCode(value.getCode());
        enumWrapper.setMessage(Code2EnumHolder.getMessage(value.getMessage(), value.getMessageEn()));
        gen.writeObject(enumWrapper);
    }

}
