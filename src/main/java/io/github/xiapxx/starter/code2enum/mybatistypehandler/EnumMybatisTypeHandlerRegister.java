package io.github.xiapxx.starter.code2enum.mybatistypehandler;

import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import java.util.Set;

/**
 * @Author xiapeng
 * @Date 2024-03-18 20:45
 */
public class EnumMybatisTypeHandlerRegister<T extends Code2Enum> implements ConfigurationCustomizer {

    private Set<Class<? extends Code2Enum>> enumClassSet;

    public EnumMybatisTypeHandlerRegister(Set<Class<? extends Code2Enum>> enumClassSet) {
        this.enumClassSet = enumClassSet;
    }

    @Override
    public void customize(Configuration configuration) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (Class<? extends Code2Enum> enumClass : enumClassSet) {
            Class<T> itemEnumClass = (Class<T>) enumClass;
            typeHandlerRegistry.register(itemEnumClass, new EnumMybatisTypeHandler<>(itemEnumClass));
        }
    }
}
