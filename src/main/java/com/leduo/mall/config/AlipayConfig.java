package com.leduo.mall.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2021000117626282";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCR7f5Q5ngj+Dx+o1QIJi9VDkhppysTyL88UjMjXVMBpiKzttSiKRQ+weAJBkANsJ/xPVMzQP1wxHmJ23vpDwPRMUZ0KzsfGMD3BwfkHPSmRU3QmZxdK3GnkIeloHEhn3D7JXVSU3YfXEhSPhaQXa98hzT1mC6MGkc7C2qT5+MuBpmhlklK4QNvQR4Sxlnize2BkrRrphFZhdJaS8y+05zsOx50i9LmiThMtzySur3bjI9pbrnqFtJ/kiI4utQSe+YeeSuSX9Ho5ZUDjYPlXAdI83/dDLji+R78s6vjeHr5MqAb1RarusyY42NQ2k1XbUw7ELf0n+QAALooJr4hzHSVAgMBAAECggEAXI73n5iO20G4HRq7Ue/mVayZgWnmiMXrhfcdOHEZFRKrMBHnDHPBw4x07URpnEVLLVgYTYoLNltoO2hbT5eWdDks1sIU3cR1oDl1Z9p10oBiusyzwOStUfr6DrOVDh5zKti3cWA+q+V8ea2n4imKBRI9JQlBfNuuEmVLVETgc3ACLPX2xhRdTTp8GIospEd1XdUHFWl7Wbdy5+3s6R5CQRhaowOuyroB5JvbpNNzgn/QZInuzpxIOEkgcUqAOhkb5OXl0OXZTHP/uZW2xsI4pb6L2BEixDWEm/Ox60J8WhirfobQtkExaZu3H4VbpAzD+vvN2R/T8YGOxuhWlYswgQKBgQDr8ZNopUm0VN6A6xk2vluTx3koUQ0nQEwXUlMo2rEi9nXvZk5nj9Iqvpd51d2GX0NMF3JR55/QdpNC7c+usjDKJTAakIH0//eGSpkEVdC0lnz2YiWF1jwmzcN1WqgKNmsK6QYmBpfPiAe3Sqn05tcwBEHkkVnKzBeDqLVDw4b97wKBgQCeVZo1N1uTFYJJFYG/ukv1xwru+Jk0krRuLQQjppQeUwUP8kQCmnRdLo6D3eE0l10NGRD64E17TnTEhl4hNZG5QGsYKJ5Xkx7bZj+uF5GiEbRPZp9+/Td+3/Vy2ID49eO65Nnh4ewnkUUaN9+THC/HbJxR+liLAVPUGv1qnYV5uwKBgQCS7u161LxSpiuuozofgmprXGTSO69ySzNXrrgFEDQR7uRBAzg+fC1I20pf4FTFcpvpTgyufw1ieAV7P7f/Ng72RDRxxp534dPW+KvULFD9tV363eI6N9Fb5PYdJijRu1PsBnQCyEZQQolzlvbCgeNujqguXGpLxc+6+RxxJ9nwVwKBgHs37wxfClsKsBukKxdEKf0Za37R5JivWs8rJT+BUV/QB+6DAT5taQVjR/GDaNF+wiGBUl+K04GUavbOAs8hW0ipwy5H3gIVMjxX9FLNcL6JvKLWIGW5Ncj9DYzUxHn7EcDOfGl3YkrgmMBwnwNjOiOeeICrrOBwpgY5AuJHuColAoGAVjxKgLIhPXzjN38gQVelwiPIz3VA9DLwpkeT802DLsU32TbbAdvHvFmJfEsipv0h7OzJR/7MtAqscBV6qtpfsvqECYPiDUWtpxIxZfvXGzBn1A/ty2wVUa98ZAZ4jhAg8xK0FadHd84WZGCqaT1HbmD2mou5ElBOBASq/9jm1GU=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzZr5fnm1gTmkb9MyvpajMkxqLY8Q+Kl2ry+9yjtm+vMkxfUH5IPDmNxlb+x0Owg/a/vcIme71ToPqD5AIV4vyu75gRsRl0PqeyjEp3oShIOD6xlC+dQeJTxU0qtuTNtTXmluGfgpRNvY2AKILbfQbrb82xjKm+Xs2iKlExKAZsVI/A1eYgPiZRQU1+qam8nvleV5ezVEJHkeDAOBj5C5NUAx50vxZ6yRCQzLjW11K1gNxLGoHFkjacgFQcT2PuYJUTnCKWRiuXrfutMAaMrIt5s+fT3YOj3XLo0YHa7ZGsYsH5Ua1f4SucrEm7u06rKhYFFZCq47wdx/Hyz60FuCqwIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://127.0.0.1:28089/alipay";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://leduomall.free.idcfengye.com/alipay";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

