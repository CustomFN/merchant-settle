package com.z.merchantsettle.utils.aliyun;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Maps;
import com.z.merchantsettle.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class AliyunUtil {

    private static final String POST_METHOD = "POST";
    private static final String GET_METHOD = "GET";

    private static final String BUSINESS_LICENSE_HOST = "http://blicence.market.alicloudapi.com";
    private static final String BUSINESS_LICENSE_PATH = "/ai_business_license";
    private static final String BUSINESS_LICENSE_APPCODE = "d0e8700a1d6b40cdb873fae93e96e5bd";


    private static final String IDCARD_HOST = "http://vipidcardcheck.haoservice.com";
    private static final String IDCARD_PATH = "/idcard/VerifyIdcardv2";


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


    /**
     * 身份证实名验证
     *
     * @param idCardNo 身份证号
     * @param name 身份证姓名
     */
    public static void iDCardValid(String idCardNo, String name) {
        Map<String, String> headers = Maps.newHashMap();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + BUSINESS_LICENSE_APPCODE);
        Map<String, String> querys = Maps.newHashMap();
        querys.put("cardNo", idCardNo);
        querys.put("realName", name);


        try {
            HttpResponse response = HttpUtils.doGet(IDCARD_HOST, IDCARD_PATH, GET_METHOD, headers, querys);
            System.out.println(response.toString());
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String image = "C:\\Users\\zincpool\\Desktop\\MTQ3MzY3MDUyMjA1OS0xNjAyNzQ2Mjc0.jpg";
        String uuid = IdUtil.randomUUID();
//        String path = QiniuUtil.upload(image, uuid, false);
//        System.out.println(path);

        businessLicense("http://pniquhm38.bkt.clouddn.com/824490b6-483b-4475-a62c-599b6ab7935a", "1");

    }
}
