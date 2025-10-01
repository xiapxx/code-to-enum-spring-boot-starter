package io.github.xiapxx.starter.code2enum.holder;

import io.github.xiapxx.starter.code2enum.enums.WebSerializerType;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import io.github.xiapxx.starter.code2enum.interfaces.Code2EnumLanguageGetter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    static Map<String, List<Code2Enum>> alias2DataListMap = new HashMap<>();

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
    public static <T extends Code2Enum> T toEnum(Long code, Class<T> code2EnumClass) {
        return toEnum(code, code2EnumClass, null);
    }

    /**
     * 获取枚举数据
     *
     * @param code code
     * @param code2EnumClass code2EnumClass
     * @param defaultEnum 默认值
     * @return 枚举数据
     * @param <T>
     */
    public static <T extends Code2Enum> T toEnum(Long code, Class<T> code2EnumClass, T defaultEnum) {
        if(code == null){
            return defaultEnum;
        }
        return toEnum(code.toString(), code2EnumClass, defaultEnum);
    }

    /**
     * 获取枚举数据
     *
     * @param code code
     * @param code2EnumClass code2EnumClass
     * @return 枚举数据
     * @param <T>
     */
    public static <T extends Code2Enum> T toEnum(Integer code, Class<T> code2EnumClass) {
        return toEnum(code, code2EnumClass, null);
    }

    /**
     * 获取枚举数据
     *
     * @param code code
     * @param code2EnumClass code2EnumClass
     * @param defaultEnum 默认值
     * @return 枚举数据
     * @param <T>
     */
    public static <T extends Code2Enum> T toEnum(Integer code, Class<T> code2EnumClass, T defaultEnum) {
        if(code == null){
            return defaultEnum;
        }
        return toEnum(code.toString(), code2EnumClass, defaultEnum);
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
        return toEnum(code, code2EnumClass, null);
    }

    /**
     * 获取枚举数据
     *
     * @param code code
     * @param code2EnumClass code2EnumClass
     * @param defaultEnum 默认值
     * @return 枚举数据
     * @param <T>
     */
    public static <T extends Code2Enum> T toEnum(String code, Class<T> code2EnumClass, T defaultEnum) {
        if(!StringUtils.hasLength(code) || code2EnumClass == null){
            return defaultEnum;
        }
        Map<String, ? extends Code2Enum> code2DataMap = enumClass2Code2DataMap.get(code2EnumClass);
        if(code2DataMap == null){
            return defaultEnum;
        }
        Code2Enum code2Enum = code2DataMap.get(code);
        if(code2Enum == null){
            return defaultEnum;
        }
        return (T) code2Enum;
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
        return toList(code2EnumClassName, null);
    }

    /**
     * 获取枚举的所有值
     *
     * @param code2EnumName 枚举类名或别名
     * @param execludCodes 排除掉的code
     * @return 枚举类所有值
     */
    public static List<Code2Enum> toList(String code2EnumName, List<String> execludCodes) {
        if(!StringUtils.hasLength(code2EnumName)){
            return new ArrayList<>();
        }
        boolean isClassName = code2EnumName.contains(".");
        return isClassName
                ? toList(enumClass2DataListMap, code2EnumName, execludCodes)
                : toList(alias2DataListMap, code2EnumName, execludCodes);
    }

    /**
     * 获取枚举的所有值
     *
     * @param code2EnumName2DataList code2EnumName2DataList
     * @param code2EnumName 枚举类名或别名
     * @param excludeCodes 排除掉的code
     * @return 枚举类所有值
     */
    private static List<Code2Enum> toList(Map<String, List<Code2Enum>> code2EnumName2DataList,
                                          String code2EnumName,
                                          Collection<String> excludeCodes) {
        if(code2EnumName2DataList == null || code2EnumName2DataList.isEmpty()){
            return new ArrayList<>();
        }

        List<Code2Enum> dataList = code2EnumName2DataList.get(code2EnumName);
        if(dataList == null || dataList.isEmpty()){
            return new ArrayList<>();
        }

        if(excludeCodes == null || excludeCodes.isEmpty()){
            return dataList;
        }

        return dataList.stream()
                .filter(item -> !excludeCodes.contains(item.getCode()))
                .collect(Collectors.toList());
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
