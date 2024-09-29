package io.github.xiapxx.starter.code2enum.mybatistypehandler;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import org.apache.ibatis.type.TypeHandlerRegistry;
import java.util.Set;

/**
 * @Author xiapeng
 * @Date 2024-03-18 20:51
 */
public class EnumMybatisPlusTypeHandlerRegister<T extends Code2Enum> implements ConfigurationCustomizer {

    private Set<Class<? extends Code2Enum>> enumClassSet;

    public EnumMybatisPlusTypeHandlerRegister(Set<Class<? extends Code2Enum>> enumClassSet) {
        this.enumClassSet = enumClassSet;
    }

    @Override
    public void customize(MybatisConfiguration configuration) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (Class<? extends Code2Enum> enumClass : enumClassSet) {
            Class<T> itemEnumClass = (Class<T>) enumClass;
            typeHandlerRegistry.register(itemEnumClass, new EnumMybatisTypeHandler<>(itemEnumClass));
        }
    }
}
