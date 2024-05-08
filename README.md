使用先决条件:

     启动类或@Configuration类上标注@Code2EnumScanner

代码内使用:

    # 枚举类需实现Code2Enum接口
    enum XxxEnum implements Code2Enum{ ... }
    
    # 编码转换为枚举对象
    XxxEnum xxxEnum = Code2EnumHolder.toEnum(enumClass, code);

功能说明  

        1. 使用Code2EnumHolder类, 可将编码转换成Code2Enum枚举对象
        
        2. 如果项目依赖了mybatis或mybatis plus: 将支持从数据库中的编码直接转换为Code2Enum枚举对象
        
        3. 如果项目依赖了spring web:  支持前端传入的编码转换成Code2Enum枚举对象;
                                     支持Code2Enum枚举对象转换成json字符串{code:xxx, message:xxx}, 以供前端使用;
                                     支持通过实现LanguageEnvGetter接口决定输出枚举的code2Enum.getMessage()还是code2Enum.getMessageEn()     
