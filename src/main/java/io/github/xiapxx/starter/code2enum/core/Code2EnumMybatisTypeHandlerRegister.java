package io.github.xiapxx.starter.code2enum.core;

import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import java.util.Map;

/**
 * @Author xiapeng
 * @Date 2024-03-18 20:45
 */
public class Code2EnumMybatisTypeHandlerRegister implements ConfigurationCustomizer {

    @Override
    public void customize(Configuration configuration) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (Map.Entry<Class<? extends Code2Enum>, Code2EnumContainer> entry : Code2EnumHolder.enumClass2ContainerMap.entrySet()) {
            Code2EnumContainer code2EnumContainer = entry.getValue();
            if(code2EnumContainer.getEnumList().isEmpty()){
                continue;
            }
            typeHandlerRegistry.register(entry.getKey(), new Code2EnumTypeHandler<>(code2EnumContainer));
        }
    }
}
