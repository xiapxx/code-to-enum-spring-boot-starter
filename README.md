使用方式:
        1. 类上标注@Code2EnumScanner;
        2. 枚举类实现Code2Enum接口
    ....
    XxxEnum xxxEnum = Code2EnumHolder.toEnum(enumClass, code);

各版本功能说明
1.0.0版本
    功能:
        1. 通过Code2EnumHolder类, 实现了Code2Enum接口的枚举可以通过编码转换成枚举对象

2.0.0版本
     功能:
        1. 通过Code2EnumHolder类, 实现了Code2Enum接口的枚举可以通过编码转换成枚举对象
        2. 如果项目依赖了mybatis: 将支持从数据库中的编码直接转换为枚举(需实现Code2Enum接口, 且在@Code2EnumScanner扫描范围内)
        3. 如果项目依赖了spring web:  将支持前端传入的code转换成枚举;
                                    将支持枚举转换成json字符串{code:xxx, message:xxx}, 以供前端使用;

           另外, 如果需自定义com.fasterxml.jackson.databind.ObjectMapper对象, 请使用如下方式:
           @Bean
           public com.fasterxml.jackson.databind.ObjectMapper objectMapper(org.springframework.http.converter.json.Jackson2ObjectMapperBuilder builder){
                ObjectMapper objectMapper = builder.build();
                ...
                return objectMapper;
           }