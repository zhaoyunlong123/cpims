package com.javakc.springbootcpims.common.base.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @program: springboot-cpims
 * @Description: 统一封装接口返回数据
 * @Author: zhao yun long
 * @date: 2020/10/23 14:09
 * ResponseBodyAdvice:作用在数据返回之前将数据转为JSON，传到客户端
 * 这个类没有被ioc容器管理，需要添加注解，并添加需要扫描的包的路径
 * @RestControllerAdvice 封装了@ControllerAdvice和@ResponseBody
 * 单独针对RestController的通知进行拦截
 */
@RestControllerAdvice(basePackages = "com.javakc.springbootcpims.modules")
public class ResultAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 配置拦截规则
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        /*处理所有拦截的方法*/
        return true;
    }

    /**
     * 封装返回的对象（beforeBodyWrite：指表现层方法没有问题的时候会走这个方法）
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     * 相当于通知里边的后置通知after，要封装的环节
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return ResultMessage.success( o );
    }

    /**
     * 当运行出现异常的时候，进入此方法
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Object exception(Exception exception)
    {
        if (exception instanceof HttpMessageNotReadableException)
            return ResultMessage.failure(ResultCode.PARAM_TYPE_BIND_ERROR);
        else
            return ResultMessage.failure(ResultCode.INTERFACE_INNER_INVOKE_ERROR);
    }

    /**
     * @ResponseBody
     * 1、接口返回对象
     * -------拦截返回的对象，并将对象进行封装---------------
     * 2、把接口返回的对象（封装的）解析为Json（字符串）
     * 3、把Json通过Response传出到客户端
     * ResponseBodyAdvice的作用是将传出的内容拦截一下
     */



}
