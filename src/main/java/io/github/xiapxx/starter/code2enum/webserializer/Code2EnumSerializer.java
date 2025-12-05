package io.github.xiapxx.starter.code2enum.webserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.xiapxx.starter.code2enum.annotation.Code2EnumConfig;
import io.github.xiapxx.starter.code2enum.entity.Code2EnumWrapper;
import io.github.xiapxx.starter.code2enum.enums.EnumCodeWebType;
import io.github.xiapxx.starter.code2enum.enums.WebSerializerType;
import io.github.xiapxx.starter.code2enum.feign.Code2EnumFeignHolder;
import io.github.xiapxx.starter.code2enum.holder.Code2EnumHolder;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import java.io.IOException;

/**
 * @Author xiapeng
 * @Date 2024-03-19 13:18
 */
public class Code2EnumSerializer<T extends Code2Enum> extends JsonSerializer<T> {

    private EnumCodeWebType enumCodeWebType;

    public Code2EnumSerializer(Class<T> enum2CodeClass) {
        this.enumCodeWebType = EnumCodeWebType.NUMBER;
        Code2EnumConfig code2EnumConfig = enum2CodeClass.getAnnotation(Code2EnumConfig.class);
        if (code2EnumConfig != null) {
            this.enumCodeWebType = code2EnumConfig.codeWebType();
        }
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value == null){
            gen.writeNull();
            return;
        }

        WebSerializerType webSerializerType = Code2EnumHolder.getWebSerializerType();
        if (webSerializerType == WebSerializerType.CODE || Code2EnumFeignHolder.isFeign()) {
            onSerializerTypeIsCode(gen, value);
            return;
        }

        if (webSerializerType == WebSerializerType.JSON) {
            serializeToJson(gen, value);
            return;
        }

        if (webSerializerType == WebSerializerType.MSG) {
            gen.writeString(value.toActualMessage());
            return;
        }

        throw new IllegalArgumentException("Unsupported web serialization type:" + webSerializerType.name());
    }

    /**
     * 当序列号类型是code
     *
     * @param gen gen
     * @param value value
     */
    private void onSerializerTypeIsCode(JsonGenerator gen, T value) throws IOException {
        switch (enumCodeWebType) {
            case STRING:
                gen.writeString(value.getCode());
                break;
            case NUMBER:
                gen.writeNumber(value.getIntCode());
                break;
        }
    }

    /**
     * 序列化成json格式
     *
     * @param gen gen
     * @param value value
     */
    private void serializeToJson(JsonGenerator gen, T value) throws IOException {
        Code2EnumWrapper enumWrapper = new Code2EnumWrapper();
        enumWrapper.setCode(EnumCodeWebType.STRING == enumCodeWebType ? value.getCode() : value.getIntCode());
        enumWrapper.setMessage(value.toActualMessage());
        gen.writeObject(enumWrapper);
    }

}
