package io.github.xiapxx.starter.code2enum;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author xiapeng
 * @Date 2024-01-05 09:14
 */
public class ClassPathCode2EnumScanner extends ClassPathBeanDefinitionScanner {

    private boolean registerMybatisTypeHandler;

    public ClassPathCode2EnumScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        try {
            Class.forName("org.apache.ibatis.type.BaseTypeHandler");
            registerMybatisTypeHandler = true;
        } catch (ClassNotFoundException e) {
            registerMybatisTypeHandler = false;
        }
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        setBeanNameGenerator((definition, registry) -> definition.getBeanClassName());
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if(beanDefinitionHolders.isEmpty()){
            return beanDefinitionHolders;
        }

        return filterBeanDefinitionHolder(beanDefinitionHolders);
    }

    private Set<BeanDefinitionHolder> filterBeanDefinitionHolder(Set<BeanDefinitionHolder> beanDefinitionHolders){
        Set<BeanDefinitionHolder> resultBeanDefinitionHolderSet = new HashSet<>();
        AbstractBeanDefinition definition;
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            definition = (AbstractBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            Class<? extends Code2Enum> enumClass = toEnumClass(definition.getBeanClassName());
            if(!Code2Enum.class.isAssignableFrom(enumClass) || !enumClass.isEnum()){
                continue;
            }
            Code2EnumContainer<?, Code2Enum<?>> code2EnumContainer = new Code2EnumContainer<>(enumClass);
            Code2EnumHolder.register(enumClass, code2EnumContainer);

            if(registerMybatisTypeHandler && !code2EnumContainer.getEnumList().isEmpty()){
                definition.getConstructorArgumentValues().addGenericArgumentValue(code2EnumContainer);
                definition.setBeanClass(Code2EnumTypeHandler.class);
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                resultBeanDefinitionHolderSet.add(beanDefinitionHolder);
            }
        }
        return resultBeanDefinitionHolderSet;
    }

    private Class<? extends Code2Enum> toEnumClass(String beanClassName){
        try {
            return (Class<? extends Code2Enum>) Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
