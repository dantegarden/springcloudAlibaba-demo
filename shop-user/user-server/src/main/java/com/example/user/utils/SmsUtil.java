package com.example.user.utils;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 短信服务的工具类
 * @author: lij
 * @create: 2020-02-24 14:34
 */
@Slf4j
public class SmsUtil {
    private static final String ACCESS_KEY_ID = "yourAccessKeyId";
    private static final String ACCESS_KEY_SECRET = "yourAccessKeySecret";

    static final String product = "Dysmsapi";
    static final String domain = "dysmsapi.aliyuncs.com";

    /**
     * 发送短信
     * @param phoneNumber 要发送到哪个手机号
     * @param signName 短信签名（必须）
     * @param templateCode 短信模板（必须）
     * @param param 模板中的参数，map
     * **/
    public static void sendSms(String phoneNumber, String signName, String templateCode, Object param){
        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(phoneNumber);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(JSON.toJSONString(param));
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            //request.setOutId("yourOutId");
            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if(!"OK".equals(sendSmsResponse.getCode())){
                log.error("短信发送失败：{}", sendSmsResponse.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("短信发送失败");
        }
    }
}
