package com.z.merchantsettle.utils.aliyun;


import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.qiniu.common.QiniuException;
import com.z.merchantsettle.utils.HttpUtils;
import com.z.merchantsettle.utils.qiniu.QiniuUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AliyunUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliyunUtil.class);

    private static final String SUCCESS_STATUS = "01";
    private static final String ENTERPRISE_CHECK_SUCCESS_STATUS = "0";

    /**
     * 身份证实名验证
     *
     * @param idCardNo 身份证号
     * @param name 身份证姓名
     */
    public static boolean iDCardValid(String idCardNo, String name) {
        String host = "https://idcardcert.market.alicloudapi.com";
        String path = "/idCardCert";
        String method = "GET";
        String appcode = "d0e8700a1d6b40cdb873fae93e96e5bd";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139xxx
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("idCard", idCardNo);
        querys.put("name", name);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());如不输出json, 请打开这行代码，打印调试头部状态码。
            //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            //获取response的body
            LOGGER.info("idCard response = {}", EntityUtils.toString(response.getEntity()));
            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
            return SUCCESS_STATUS.equals(jsonObject.getString("status"));
        } catch (Exception e) {
            LOGGER.error("身份证实名认证异常", e);
            return false;
        }
    }

    /**
     * 银行卡三要素认证
     *
     * @param bankCardId    银行卡号
     * @param idCard        身份证号
     * @param realName      姓名
     */
    public static boolean bankCardValid(String bankCardId, String idCard, String realName) {
        String host = "https://tbank.market.alicloudapi.com";
        String path = "/bankCheck";
        String method = "GET";
        String appcode = "d0e8700a1d6b40cdb873fae93e96e5bd";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("accountNo", bankCardId);
        querys.put("idCard", idCard);
        querys.put("name", realName);
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());如不输出json, 请打开这行代码，打印调试头部状态码。
            //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            //获取response的body
            LOGGER.info("bankCardValid response = {}", EntityUtils.toString(response.getEntity()));
            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
            return SUCCESS_STATUS.equals(jsonObject.getString("status"));
        } catch (Exception e) {
            LOGGER.error("银行卡三要素认证异常", e);
            return false;
        }
    }

    /**
     * 企业三要素认证
     *
     * @param enterpriceId      企业统一社会信用代码
     * @param enterpriseName    企业名称
     * @param enterpriseOwner   企业法人名称
     */
    public static boolean enterpriseValid(String enterpriceId, String enterpriseName, String enterpriseOwner) {
        String host = "http://three.market.alicloudapi.com";
        String path = "/ai_market/ai_enterprise_knowledge/enterprise_three_keys/v1";
        String method = "GET";
        String appcode = "d0e8700a1d6b40cdb873fae93e96e5bd";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("ENTERPRISE_ID", enterpriceId);
        querys.put("ENTERPRISE_NAME_CH", enterpriseName);
        querys.put("ENTERPRISE_OWNER", enterpriseOwner);


        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            //获取response的body
            LOGGER.info("enterpriseValid response = {}", EntityUtils.toString(response.getEntity()));
            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
            return ENTERPRISE_CHECK_SUCCESS_STATUS.equals(jsonObject.getString("CHECK_STATUS"));
        } catch (Exception e) {
            LOGGER.error("企业三要素认证异常", e);
            return false;
        }
    }


    private static final String POST_METHOD = "POST";
    private static final String BUSINESS_LICENSE_HOST = "http://blicence.market.alicloudapi.com";
    private static final String BUSINESS_LICENSE_PATH = "/ai_business_license";
    private static final String BUSINESS_LICENSE_APPCODE = "d0e8700a1d6b40cdb873fae93e96e5bd";

    /**
     * 营业执照识别
     *
     * @param businessLicenseImage 图像数据，BASE64编码后进行URLENCODE，要求base64编码和URLENCODE后大小不超过4M，最短边至少15PX，最长边最大4096PX,支持JPG/PNG/BMP格式
     *                             0：营业执照图像内容为BASE64编码；1：营业执照图像内容为图像文件URL链接
     * @param businessLicenseImageType 营业执照图像类型
     */
    public static void businessLicense(String businessLicenseImage, String businessLicenseImageType) {
        Map<String, String> headers = Maps.newHashMap();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + BUSINESS_LICENSE_APPCODE);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = Maps.newHashMap();
        Map<String, String> bodys = Maps.newHashMap();
        bodys.put("AI_BUSINESS_LICENSE_IMAGE", businessLicenseImage);
        bodys.put("AI_BUSINESS_LICENSE_IMAGE_TYPE", businessLicenseImageType);


        try {
            HttpResponse response = HttpUtils.doPost(BUSINESS_LICENSE_HOST, BUSINESS_LICENSE_PATH, POST_METHOD, headers, querys, bodys);
            System.out.println("++++++++++++++++  " + response.toString());
            System.out.println("----------------  " + EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException, QiniuException {
        String image = "C:\\Users\\zincpool\\Desktop\\timg.jpg";
        String uuid = IdUtil.randomUUID();
        String path = QiniuUtil.upload(image, uuid, false);
        System.out.println(path);

        businessLicense(path, "1");

    }
}
