package io.github.xiapxx.starter.code2enum.holder;

import io.github.xiapxx.starter.code2enum.enums.WebSerializerType;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import io.github.xiapxx.starter.code2enum.interfaces.Code2EnumLanguageGetter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Code2Enum的枚举持有者
 *
 * @Author xiapeng
 * @Date 2024-03-14 17:59
 */
public class Code2EnumHolder {

    static ApplicationContext applicationContext;

    static volatile Code2EnumLanguageGetter code2EnumLanguageGetter;

    static volatile boolean code2EnumLanguageGetterLoad = false;

    static Map<Class<? extends Code2Enum>, Map<String, ? extends Code2Enum>> enumClass2Code2DataMap = new HashMap<>();

    static Map<String, List<Code2Enum>> enumClass2DataListMap = new HashMap<>();

    static WebSerializerType webSerializerType;

    private Code2EnumHolder(){}

    /**
     * 获取全局的web序列化方式
     *
     * @return 全局的web序列化方式
     */
    public static WebSerializerType getWebSerializerType(){
        return webSerializerType;
    }

    /**
     * 获取枚举数据
     *
     * @param code code
     * @param code2EnumClass code2EnumClass
     * @return 枚举数据
     * @param <T>
     */
    public static <T extends Code2Enum> T toEnum(Long code, Class<T> code2EnumClass){
        if(code == null){
            return null;
        }
        return toEnum(code.toString(), code2EnumClass);
    }

    /**
     * 获取枚举数据
     *
     * @param code code
     * @param code2EnumClass code2EnumClass
     * @return 枚举数据
     * @param <T>
     */
    public static <T extends Code2Enum> T toEnum(Integer code, Class<T> code2EnumClass){
        if(code == null){
            return null;
        }
        return toEnum(code.toString(), code2EnumClass);
    }

    /**
     * 获取枚举数据
     *
     * @param code code
     * @param code2EnumClass code2EnumClass
     * @return 枚举数据
     * @param <T>
     */
    public static <T extends Code2Enum> T toEnum(String code, Class<T> code2EnumClass) {
        if(!StringUtils.hasLength(code) || code2EnumClass == null){
            return null;
        }
        Map<String, ? extends Code2Enum> code2DataMap = enumClass2Code2DataMap.get(code2EnumClass);
        if(code2DataMap == null){
            return null;
        }
        return (T) code2DataMap.get(code);
    }

    public static boolean isChinese() {
        loadCode2EnumLanguageGetter();
        return code2EnumLanguageGetter == null || code2EnumLanguageGetter.isChinese();
    }

    /**
     * 获取枚举类的所有值
     *
     * @param code2EnumClassName code2EnumClassName
     * @return 枚举类所有值
     */
    public static List<Code2Enum> toList(String code2EnumClassName){
        if(!StringUtils.hasLength(code2EnumClassName)){
            return new ArrayList<>();
        }
        return enumClass2DataListMap.get(code2EnumClassName);
    }

    /**
     * 获取枚举信息
     *
     * @param code2Enum code2Enum
     * @return 枚举信息
     */
    public static String getMessage(Code2Enum code2Enum) {
        return isChinese() ? code2Enum.getMessage() : code2Enum.getMessageEn();
    }

    /**
     * 加载code2EnumLanguageGetter
     */
    private static void loadCode2EnumLanguageGetter() {
        if(applicationContext == null || code2EnumLanguageGetterLoad) {
            return;
        }
        synchronized (Code2EnumHolder.class) {
            if(code2EnumLanguageGetterLoad) {
                return;
            }
            ObjectProvider<Code2EnumLanguageGetter> code2EnumLanguageGetterObjectProvider = applicationContext.getBeanProvider(Code2EnumLanguageGetter.class);
            code2EnumLanguageGetter = code2EnumLanguageGetterObjectProvider.getIfAvailable();
            code2EnumLanguageGetterLoad = true;
        }
    }

}
