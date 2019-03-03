package com.z.merchantsettle.utils.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class QiniuUtil {

    /**
     * 上传本地文件
     * @param localFilePath 本地文件完整路径
     * @param key 文件云端存储的名称
     * @param override 是否覆盖同名同位置文件
     * @return
     */
    public static String upload(String localFilePath, String key, boolean override) throws QiniuException {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuConfig.getInstance().getZone());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(QiNiuConfig.getInstance().getAccessKey(), QiNiuConfig.getInstance().getSecretKey());
        String upToken;
        if(override){
            upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket(), key);//覆盖上传凭证
        }else{
            upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket());
        }

        Response response = uploadManager.put(localFilePath, key, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return QiNiuConfig.getInstance().getDomainOfBucket() + "/" + putRet.key;

    }


    /**
     * 获取文件访问地址
     * @param fileName 文件云端存储的名称
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getFileUrl(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String publicUrl = String.format("%s/%s", QiNiuConfig.getInstance().getDomainOfBucket(), encodedFileName);
        Auth auth = getAuth();
        long expireInSeconds = QiNiuConfig.getInstance().getExpireInSeconds();
        if(-1 == expireInSeconds){
            return auth.privateDownloadUrl(publicUrl);
        }
        return auth.privateDownloadUrl(publicUrl, expireInSeconds);
    }

    /**
     * 上传MultipartFile
     * @param file
     * @param key
     * @param override
     * @return
     * @throws IOException
     */
    public static boolean uploadMultipartFile(MultipartFile file, String key, boolean override) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuConfig.getInstance().getZone());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //把文件转化为字节数组
        InputStream is = null;
        ByteArrayOutputStream bos = null;

        try {
            is = file.getInputStream();
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = is.read(b)) != -1){
                bos.write(b, 0, len);
            }
            byte[] uploadBytes= bos.toByteArray();

            Auth auth = getAuth();
            String upToken;
            if(override){
                upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket(), key);//覆盖上传凭证
            }else{
                upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket());
            }
            //默认上传接口回复对象
            DefaultPutRet putRet;
            //进行上传操作，传入文件的字节数组，文件名，上传空间，得到回复对象
            Response response = uploadManager.put(uploadBytes, key, upToken);
            putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);//key 文件名
            System.out.println(putRet.hash);//hash 七牛返回的文件存储的地址，可以使用这个地址加七牛给你提供的前缀访问到这个视频。
            return true;
        }catch (QiniuException e){
            e.printStackTrace();
            return false;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Auth getAuth(){
        Auth auth = Auth.create(QiNiuConfig.getInstance().getAccessKey(), QiNiuConfig.getInstance().getSecretKey());
        return auth;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        upload("C:\\Users\\zincpool\\Desktop\\1.jpg","1", false);
        System.out.println(getFileUrl("1"));
    }
}
