各版本功能说明
1.0.0版本
    使用:
        1. 通过@Code2EnumScanner开启编码转换为注解功能
        2. 枚举类实现Code2Enum
        3. 通过Code2EnumHolder类, 实现了Code2Enum接口的枚举可以通过编码转换成枚举对象

2.0.0版本
     功能:
        1. 支持1.0.0的所有功能
        2. 如果项目依赖了mybatis, 将支持从数据库中的编码直接转换为枚举(需Code2Enum接口, 且在@Code2EnumScanner扫描范围内)
