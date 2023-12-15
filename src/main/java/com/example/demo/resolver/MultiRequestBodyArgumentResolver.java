package com.example.demo.resolver;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.annotation.MultiRequestBody;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : songtc
 * @since : 2023/12/14 11:44
 */
public class MultiRequestBodyArgumentResolver implements HandlerMethodArgumentResolver {

    /** 存储属性表示符 */
    private static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // whether included @MultiRequestBody
        return parameter.hasParameterAnnotation(MultiRequestBody.class);
    }


    /** 获取请求中的请求体json数据 */
    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        String body = (String) webRequest.getAttribute(JSON_REQUEST_BODY, NativeWebRequest.SCOPE_REQUEST);
        if (body == null) {
            try {
                // splicing json string
                body = Objects.requireNonNull(servletRequest, "")
                        .getReader()
                        .lines()
                        .collect(Collectors.joining());

                // Temporary storage request
                webRequest.setAttribute(JSON_REQUEST_BODY, body, NativeWebRequest.SCOPE_REQUEST);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return body;
    }

    /** 从json对象中获取注解对应的数据 */
    private Object getAnnoData(MethodParameter parameter, JSONObject jsonObject) {
        MultiRequestBody annotation = parameter.getParameterAnnotation(MultiRequestBody.class);
        String key = annotation.value();
        Object value;
        // 如果@MultiRequestBody注解没有设置value 则取json解析的key
        if (StringUtils.hasLength(key)) {
            value = jsonObject.get(key);
            // 不存在value且数据为必填项
            if (value == null && annotation.required()) {
                throw new IllegalArgumentException(String.format("required param %s not present", key));
            }
        }
        // 从json中取值并返回
        return jsonObject.get(parameter.getParameterName());
    }

    /**
     * 数据类型转化
     *
     * @param value         需要转换的值
     * @param parameterType 目标对象类型
     * @return 目标对象的值
     */
    private Object convertType(Object value, Class<?> parameterType) {
        if (ObjectUtils.isEmpty(value)) {
            throw new IllegalArgumentException("");
        }
        // 基本类型 基本类型包装 String类型
        if (parameterType.isPrimitive() || ClassUtils.isPrimitiveWrapper(parameterType)
                || parameterType == String.class) {
            return DefaultConversionService.getSharedInstance().convert(value, parameterType);
        }
        // 其他复杂对象
        return JSONObject.parseObject(value.toString(), parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        JSONObject object = JSONObject.parseObject(getRequestBody(webRequest));
        return convertType(getAnnoData(parameter, object), parameter.getParameterType());
    }
}
