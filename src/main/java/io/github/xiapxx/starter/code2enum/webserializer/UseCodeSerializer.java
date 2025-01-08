package io.github.xiapxx.starter.code2enum.webserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import java.io.IOException;

/**
 * 仅仅需要code
 *
 * @Author xiapeng
 * @Date 2025-01-08 17:36
 */
public class UseCodeSerializer<T extends Code2Enum> extends JsonSerializer<T> {

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value == null){
            gen.writeNull();
            return;
        }
        gen.writeString(value.getCode());
    }


}
