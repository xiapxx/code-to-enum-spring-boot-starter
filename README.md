# code-to-enum-spring-boot-starter

## 使用场景

### 假设表中有字段sex int comment '性别; 0-男 1-女', 那么表实体有字段 private Integer sex
    表实体中的sex字段定义成了Integer类型, 虽然可以实现需求, 但如果没有写任何注释, 后期维护时, 很难知道0代表什么含义, 1代表什么含义。 
    如果能直接定义成枚举类型, 那么可以大幅减少后期维护成本。
    该组件允许你将sex定义成枚举。

### 当前端收到sex=0时, 前端需要将sex转换成'男'
    由前端转换虽然是常见的做法, 但稍显麻烦, 后续所有涉及该字段的地方都要转换一次。 
    该组件可以直接返回给前端{code:0, message:'男'}

## 如何使用？
### 引入依赖
~~~~xml
<dependency>
    <groupId>io.github.xiapxx</groupId>
    <artifactId>code-to-enum-spring-boot-starter</artifactId>
    <version>2.1.6</version>
 </dependency>
~~~~ 

### 启用枚举
    @Configuration
    @Code2EnumScanner(basePackages = "com.xxx.enums") // 将扫描com.xxx.enums包下实现了Code2Enum接口的枚举
    public class XXXConfiguration {
    }

### 定义枚举
    package com.xxx.enums
    ...

    public enum SexEnum implements Code2Enum {
        MALE("0", "男"),
        FEMALE("1", "女");
        
        @Getter
        private String code;
        @Getter
        private String message;

        SexEnum(String code, String message){
            this.code = code;
            this.message = message;    
        }
    }

### 使用枚举

    假设有如下实体:
    @Getter
    @Setter
    public class XXXRequest {

        private SexEnum sex;

    }

    @Getter
    @Setter
    public class XXXResponse {
        
        private SexEnum sex;

    }
#### mybatis里使用枚举

    public interface XXXMapper {
        
        XXXResponse select(XXXRequest request);
    }

    XXXMapper.xml(入参和出参都可以直接使用枚举)
        <select id="select" resultType="xxx.xxx.XXXResponse">
            select 0 sex from table_name where sex = #{sex} limit 1 
        </select>

#### 控制层使用枚举

    @PostMapping("/test")
    public XXXResponse test(@RequestBody XXXRequest request) {
        ... 
    }

    对于上述接口, 入参格式:
    {
        "sex":"0"
    }

    对于上述接口, 出参格式:
    {
        "sex":{"code":0, "message":"男"}
    }
    
## 支持国际化
    package com.xxx.enums
    ...

    public enum SexEnum implements Code2Enum {
        MALE("0", "男", "male"),
        FEMALE("1", "女", "female");
        
        @Getter
        private String code;
        @Getter
        private String message;
        @Getter
        privaet String messageEn;  // 需多定义一个英文属性

        SexEnum(String code, String message, String messageEn){
            this.code = code;
            this.message = message;    
            this.messageEn = messageEn;
        }
    }
    
    @Component
    public class XXXClass implements Code2EnumLanguageGetter {
        
        public boolean isChinese() {
            // 此处判断是什么语言环境; true:中文环境 false:英文环境
            // 控制层的响应实体在反序列化时被调用
        }
    }

## 枚举在mybatis的xml中作为条件参数

    package com.xxx.enums
    ...

    public enum SexEnum implements Code2Enum {
        MALE("0", "男"),
        FEMALE("1", "女");
        
        @Getter
        private String code;
        @Getter
        private String message;

        SexEnum(String code, String message){
            this.code = code;
            this.message = message;    
        }

        // 枚举作为XXXMapper.xml中的条件参数时, 需实现toString接口, 并返回code
        public String toString(){
            return code;
        }
    }

    XXXMapper.xml
        <select id="select" resultType="xxx.xxx.XXXResponse">
            select 0 sex from table_name 
            <if test="sex == 0">  // sex作为条件参数
                ...
            </if>
        </select>

## 防止索引失效
    查询场景时, 枚举的code会作为占位符传递到jdbc, 如果传递时的类型与表字段(code字段有索引)类型不一致, 会导致索引失效。
    该组件允许定义code的jdbc类型

    package com.xxx.enums
    ...
    public enum SexEnum implements Code2Enum {
        MALE("0", "男"),
        FEMALE("1", "女");
        
        @Getter
        private String code;
        @Getter
        private String message;

        SexEnum(String code, String message){
            this.code = code;
            this.message = message;    
        }

        // 指定code的jdbc类型
        EnumCodeJdbcType enumCodeJdbcType(){
            return EnumCodeJdbcType.INT;  // 支持int(默认), string, long
        }
    }

## 默认值
    入库场景, 当表实体的枚举字段为null时, 有时候希望表里该字段能写一个默认值。
    该组件允许定义一个默认值。

    package com.xxx.enums
    ...
    public enum SexEnum implements Code2Enum {
        MALE("0", "男"),
        FEMALE("1", "女");

        @Getter
        private String code;
        @Getter
        private String message;

        SexEnum(String code, String message){
            this.code = code;
            this.message = message;    
        }

        pulbic String jdbcDefaultCode() {
            return "2"; // 定义默认值, 2代表空
        }
    }