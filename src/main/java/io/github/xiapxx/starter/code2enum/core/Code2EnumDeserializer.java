package io.github.xiapxx.starter.code2enum.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import java.io.IOException;

/**
 * @Author xiapeng
 * @Date 2024-03-19 13:33
 */
public class Code2EnumDeserializer<CODE, ENUM extends Code2Enum<CODE>> extends JsonDeserializer<ENUM> {
    private Code2EnumContainer<CODE, ENUM> enumContainer;

    public Code2EnumDeserializer(Code2EnumContainer<CODE, ENUM> enumContainer){
        this.enumContainer = enumContainer;
    }
    @Override
    public ENUM deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if(p.hasTokenId(JsonTokenId.ID_NULL) || enumContainer.isEmpty()){
            return null;
        }
        if(enumContainer.isLongCode() && p.isExpectedNumberIntToken()){
            return enumContainer.toEnum((CODE) Long.valueOf(p.getLongValue()));
        }
        if(enumContainer.isIntegerCode() && p.isExpectedNumberIntToken()){
            return enumContainer.toEnum((CODE) Integer.valueOf(p.getIntValue()));
        }
        if(enumContainer.isStringCode()){
            return enumContainer.toEnum((CODE) p.getValueAsString());
        }
        return null;
    }
}
