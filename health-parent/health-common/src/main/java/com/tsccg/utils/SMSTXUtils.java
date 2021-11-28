package com.tsccg.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;

/**
 * @Author: TSCCG
 * @Date: 2021/11/26 00:03
 * 腾讯云短信服务工具类
 */
public class SMSTXUtils {
    //鉴权
    private static final String secretId = "FSAKIDxkGXZLq52lDOIwJPEn7ByVmmVx94i5D3";
    private static final String secretKey = "bvylHWTBcnhLhma7NM4VfY9UZOwDCvObFS";
    //短信应用id
    /* 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId，示例如1400006666 */
    private static final String APP_ID = "1400602573";
    //短信签名
    /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，签名信息可登录 [短信控制台] 查看 */
    private static final String SING_NAME = "范帅强的公众号";
    //发送预约验证码：1.验证码
    public static final String ORDER_VALIDATE_CODE = "1215716";
    //发送体检预约成功通知短信：1.用户姓名 2.预约日期 3.预约地点
    public static final String ORDER_SUCCESS_NOTICE = "1215759";
    //发送登录验证码：1.验证码 2.有效时间
    public static final String LOGIN_VALIDATE_CODE = "1215708";
    //通用验证码模板：1.验证码 2.有效时间
    public static final String COMMON_VALIDATE_CODE = "1217279";
    //电话号码
//    private static final String telephone = "15737258296";
//    private static final String telephone = "17633926134";

    /**
     * 发送短信
     * @param templateCode 发送的短信模板种类
     * @param telephone 要发送的电话号码
     * @param params 模板参数，填入模板的参数，传入的是字符串数组
     *      若只有一个参数：1.验证码
     *      有两个参数：1.验证码、2.生效时间
     *      有三个参数：1.用户姓名、2.预约日期、3.医院地址
     */
    public static void sendShortMessage(String templateCode,String telephone,String[] params) {
        try {
            /* 必要步骤：
             * 实例化一个认证对象，入参需要传入腾讯云账户密钥对 secretId 和 secretKey
             */
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个 http 选项，可选，无特殊需求时可以跳过
            HttpProfile httpProfile = new HttpProfile();
            // 设置代理
//            httpProfile.setProxyHost("host");
//            httpProfile.setProxyPort(8080);
            /* SDK 默认使用 POST 方法。
             * 如需使用 GET 方法，可以在此处设置，但 GET 方法无法处理较大的请求 */
            httpProfile.setReqMethod("POST");
            /* SDK 有默认的超时时间，非必要请不要进行调整
             * 如有需要请在代码中查阅以获取最新的默认值 */
            httpProfile.setConnTimeout(60);
            /* SDK 会自动指定域名，通常无需指定域名，但访问金融区的服务时必须手动指定域名
             * 例如 SMS 的上海金融区域名为 sms.ap-shanghai-fsi.tencentcloudapi.com */
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            /* 非必要步骤:
             * 实例化一个客户端配置对象，可以指定超时时间等配置 */
            ClientProfile clientProfile = new ClientProfile();
            /* SDK 默认用 TC3-HMAC-SHA256 进行签名
             * 非必要请不要修改该字段 */
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            /* 实例化 SMS 的 client 对象
             * 第二个参数是地域信息，可以直接填写字符串 ap-guangzhou，或者引用预设的常量 */
            SmsClient client = new SmsClient(cred, "ap-guangzhou",clientProfile);
            /* 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
             * 您可以直接查询 SDK 源码确定接口有哪些属性可以设置
             * 属性可能是基本类型，也可能引用了另一个数据结构
             * 推荐使用 IDE 进行开发，可以方便地跳转查阅各个接口和数据结构的文档说明 */
            SendSmsRequest req = new SendSmsRequest();
            /* 填充请求参数，这里 request 对象的成员变量即对应接口的入参
            /* 短信应用 ID: 在 [短信控制台] 添加应用后生成的实际 SDKAppID，例如1400006666 */
            req.setSmsSdkAppid(APP_ID);
            /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，可登录 [短信控制台] 查看签名信息 */
            req.setSign(SING_NAME);
            /* 模板 ID: 必须填写已审核通过的模板 ID，可登录 [短信控制台] 查看模板 ID */
            req.setTemplateID(templateCode);
            //发送的手机号码，采用 e.164 标准，+[国家或地区码][手机号]
            //例如+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号*/
            String[] phoneNumbers = {"+86" + telephone};
            req.setPhoneNumberSet(phoneNumbers);
            /*
             * 模板参数:填入模板的参数，字符串数组
             * 若只有一个参数：1.验证码
             * 有两个参数：1.验证码、2.生效时间
             * 有三个参数：1.用户姓名、2.预约日期、3.医院地址
             */
            String[] templateParams = params;
            req.setTemplateParamSet(templateParams);
            /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应 */
            SendSmsResponse res = client.SendSms(req);
            // 输出 JSON 格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(res));
            // 可以取出单个值，您可以通过官网接口文档或跳转到 response 对象的定义处查看返回字段的定义
            System.out.println(res.getRequestId());
            System.out.println("发送短信成功");
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            System.out.println("发送短信失败");
        }
    }
}
