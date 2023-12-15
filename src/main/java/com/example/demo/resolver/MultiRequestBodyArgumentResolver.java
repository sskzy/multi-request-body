package com.example.demo.resolver;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.annotation.MultiRequestBody;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author : songtc
 * @since : 2023/12/14 11:44
 */
public class MultiRequestBodyArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // whether included @MultiRequestBody
        return parameter.hasParameterAnnotation(MultiRequestBody.class);
    }

    /**
     * 获取名称
     *
     * @param parameter
     * @param jsonObject
     * @return
     */
    private String getAnnotationKey(MethodParameter parameter, JSONObject jsonObject) {
        MultiRequestBody parameterAnnotation = parameter.getParameterAnnotation(MultiRequestBody.class);
        // 注解的value是JSON的key
        String key = parameterAnnotation.value();
        Object value;
        // 如果@MultiRequestBody注解没有设置value，则取参数名FrameworkServlet作为json解析的key
        if (StringUtils.hasLength(key)) {
            value = jsonObject.get(key);
            // 如果设置了value但是解析不到，报错
            if (value == null && parameterAnnotation.required()) {
                throw new IllegalArgumentException(String.format("required param %s is not present", key));
            }
        } else {
            // 注解为设置value则用参数名当做json的key
            key = parameter.getParameterName();
            value = jsonObject.get(key);
        }
        return value.toString();
    }

    /**
     * 获取请求体
     */
    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        String body = (String) webRequest.getAttribute(JSON_REQUEST_BODY, NativeWebRequest.SCOPE_REQUEST);
        if (body == null) {
            try {
                // TODO 这个Collector.joining很精辟
                // TODO 这个Objects.requireNonNull(servletRequest)要去看看实现符不符合预期
                body = servletRequest.getReader().lines().collect(Collectors.joining());
                // TODO 为什么要在set回去?
                // 不set进去第二个参数会为空
                webRequest.setAttribute(JSON_REQUEST_BODY, body, NativeWebRequest.SCOPE_REQUEST);
                ;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return body;
    }

    /**
     * 类型转化
     *
     * @param value         需要转换的值
     * @param parameterType 目标对象类型
     * @return 目标对象的值
     */
    private Object convertType(Object value, Class<?> parameterType) {
        // 通过注解的value或者参数名解析，能拿到value进行解析
        if (value != null) {
            // 基本类型
            // 基本类型包装类
            // 字符串类型
            if (parameterType.isPrimitive()
                    || ClassUtils.isPrimitiveWrapper(parameterType)
                    || parameterType == String.class) {
                ConversionService conversion = DefaultConversionService.getSharedInstance();
                Object convert = conversion.convert(value, parameterType);
                return convert;
            }
            // 其他复杂对象
            return JSONObject.parseObject(value.toString(), parameterType);
        }
        return null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jsonBody = getRequestBody(webRequest);
        JSONObject jsonObject = JSONObject.parseObject(jsonBody);

        String value = getAnnotationKey(parameter, jsonObject);

        // 获取的注解后的类型 Long
        if (value != null) {
            Class<?> parameterType = parameter.getParameterType();
            Object o = convertType(value, parameterType);
            return o;
        }
        return null;
    }
}
