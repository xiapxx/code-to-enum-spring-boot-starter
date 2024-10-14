package io.github.xiapxx.starter.code2enum.webserializer;

import io.github.xiapxx.starter.code2enum.holder.Code2EnumHolder;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author xiapeng
 * @Date 2024-10-14 15:11
 */
public class Code2EnumConverter<T extends Code2Enum> implements GenericConverter {

    private Class<T> enum2CodeClass;

    private Set<ConvertiblePair> convertiblePairs;

    public Code2EnumConverter(Class<T> enum2CodeClass) {
        this.enum2CodeClass = enum2CodeClass;
        convertiblePairs = new HashSet<>();
        convertiblePairs.add(new ConvertiblePair(String.class, enum2CodeClass));
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return convertiblePairs;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if(source == null){
            return null;
        }
        return Code2EnumHolder.toEnum((String) source, enum2CodeClass);
    }
}
