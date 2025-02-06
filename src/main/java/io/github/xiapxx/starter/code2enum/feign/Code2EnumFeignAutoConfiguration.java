package io.github.xiapxx.starter.code2enum.feign;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;

/**
 * 支持Code2Enum的feign中作为参数传递
 *
 * @Author xiapeng
 * @Date 2025-01-10 13:22
 */
@ConditionalOnClass({SpringEncoder.class})
@AutoConfigureBefore(FeignClientsConfiguration.class)
public class Code2EnumFeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpringEncoder feignEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new Code2EnumSpringEncoder(messageConverters);
    }


}
