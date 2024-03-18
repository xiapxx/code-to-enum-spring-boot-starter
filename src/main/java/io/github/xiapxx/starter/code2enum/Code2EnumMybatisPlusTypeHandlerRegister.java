package io.github.xiapxx.starter.code2enum;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import java.util.Map;

/**
 * @Author xiapeng
 * @Date 2024-03-18 20:51
 */
public class Code2EnumMybatisPlusTypeHandlerRegister implements ConfigurationCustomizer {

    @Override
    public void customize(MybatisConfiguration configuration) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (Map.Entry<Class<? extends Code2Enum>, Code2EnumContainer> entry : Code2EnumHolder.enumClass2ContainerMap.entrySet()) {
            Code2EnumContainer code2EnumContainer = entry.getValue();
            if(code2EnumContainer.getEnumList().isEmpty()){
                continue;
            }
            typeHandlerRegistry.register(entry.getKey(), new Code2EnumTypeHandler<>(code2EnumContainer, entry.getKey()));
        }
    }
}
