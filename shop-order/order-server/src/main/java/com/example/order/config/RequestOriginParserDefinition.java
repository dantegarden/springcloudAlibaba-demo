package com.example.order.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 自定义请求来源解析器
 * @author: lij
 * @create: 2020-02-22 00:42
 */
@Component
@Slf4j
public class RequestOriginParserDefinition implements RequestOriginParser {
    /**定义区分来源：本质作用是通过HttpServletRequest对象来获取来源标识
     * 例如请求有两个来源app、pc
     * 然后交给sentinel应用，根据配置的规则进行匹配
     * **/
    @Override
    public String parseOrigin(HttpServletRequest request) {
        String serviceName = request.getParameter("serviceName");
        if(StrUtil.isBlank(serviceName)){
            log.warn("serviceName is empty!");
        }
        return serviceName;
    }
}
