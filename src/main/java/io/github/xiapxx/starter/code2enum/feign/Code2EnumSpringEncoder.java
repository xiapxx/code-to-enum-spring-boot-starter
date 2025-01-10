package io.github.xiapxx.starter.code2enum.feign;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import java.lang.reflect.Type;

/**
 * @Author xiapeng
 * @Date 2025-01-10 16:13
 */
public class Code2EnumSpringEncoder extends SpringEncoder {

    public Code2EnumSpringEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
        try {
            Code2EnumFeignHolder.remove();
            Code2EnumFeignHolder.setIsFeign();
            super.encode(requestBody, bodyType, request);
        } catch (EncodeException e) {
            throw e;
        } finally {
            Code2EnumFeignHolder.remove();
        }
    }
}
