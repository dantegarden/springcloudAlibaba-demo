package com.example.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSON;
import com.example.bean.Result;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 自定义降级异常处理
 * @author: lij
 * @create: 2020-02-22 01:07
 */
@Component
public class ExceptionHandlerPage implements UrlBlockHandler {
    @Override
    public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException e) throws IOException {
        Result result = Result.fail();
        if(e instanceof FlowException){
            result.setMessage("接口已被限流");
        }else if(e instanceof DegradeException){
            result.setMessage("接口已被降级");
        }else if(e instanceof AuthorityException){
            result.setMessage("来源无权访问该接口");
        }else if(e instanceof ParamFlowException){
            result.setMessage("访问热点过于频繁");
        }else if(e instanceof SystemBlockException){
            result.setMessage("因触发系统保护，接口禁止访问");
        }else{
            result.setMessage("接口因不明原因被禁止访问");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
