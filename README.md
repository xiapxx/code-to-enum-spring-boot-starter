使用先决条件:

     在启动类或@Configuration类上标注@Code2EnumScanner: 将扫描并注册实现了Code2Enum接口的枚举

代码内使用:

    # 枚举类需实现Code2Enum接口
    enum XxxEnum implements Code2Enum{ ... }
    
    # 编码转换为枚举对象
    XxxEnum xxxEnum = Code2EnumHolder.toEnum(enumClass, code);

各版本功能说明

1.0.0版本
    功能:
        
        1. 通过Code2EnumHolder类, 实现了Code2Enum接口的枚举可以通过编码转换成枚举对象

2.0.0版本
     功能:
        
        1. 通过Code2EnumHolder类, 实现了Code2Enum接口的枚举可以通过编码转换成枚举对象
        
        2. 如果项目依赖了mybatis或mybatis plus: 将支持从数据库中的编码直接转换为枚举
        
        3. 如果项目依赖了spring web:  将支持前端传入的code转换成枚举;
                                    将支持枚举转换成json字符串{code:xxx, message:xxx}, 以供前端使用;
                                    将支持通过实现LanguageEnvGetter接口决定输出枚举的code2Enum.getMessage()还是code2Enum.getMessageEn()
           另外, 如果需自定义com.fasterxml.jackson.databind.ObjectMapper对象, 请使用如下方式:
           
           @Bean
           public com.fasterxml.jackson.databind.ObjectMapper objectMapper(org.springframework.http.converter.json.Jackson2ObjectMapperBuilder builder){
                ObjectMapper objectMapper = builder.build();
                ...
                return objectMapper;
           }
