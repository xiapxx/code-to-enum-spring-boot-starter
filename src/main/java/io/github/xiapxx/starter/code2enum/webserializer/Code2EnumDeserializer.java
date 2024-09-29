package io.github.xiapxx.starter.code2enum.webserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.xiapxx.starter.code2enum.holder.Code2EnumHolder;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import java.io.IOException;

/**
 * @Author xiapeng
 * @Date 2024-03-19 13:33
 */
public class Code2EnumDeserializer<T extends Code2Enum> extends JsonDeserializer<T> {
    private Class<T> enum2CodeClass;

    public Code2EnumDeserializer(Class<T> enum2CodeClass){
        this.enum2CodeClass = enum2CodeClass;
    }
    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if(p.hasTokenId(JsonTokenId.ID_NULL)){
            return null;
        }
        String code = p.getValueAsString();
        return Code2EnumHolder.toEnum(code, enum2CodeClass);
    }

}
