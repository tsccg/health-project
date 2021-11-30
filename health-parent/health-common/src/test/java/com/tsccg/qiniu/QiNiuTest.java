//package com.tsccg.qiniu;
//
//import com.google.gson.Gson;
//import com.qiniu.common.QiniuException;
//import com.qiniu.common.Zone;
//import com.qiniu.http.Response;
//import com.qiniu.storage.BucketManager;
//import com.qiniu.storage.Configuration;
//import com.qiniu.storage.UploadManager;
//import com.qiniu.storage.model.DefaultPutRet;
//import com.qiniu.util.Auth;
//import org.junit.Test;
//
///**
// * @Author: TSCCG
// * @Date: 2021/11/09 23:13
// * 测试七牛云
// */
//public class QiNiuTest {
//    /**
//     * 上传图片至七牛云
//     */
//    @Test
//    public void uploadTest() {
//        //构造一个带指定Zone对象的配置类
//        Configuration cfg = new Configuration(Zone.zone2());
//        //...其他参数参考类注释
//        UploadManager uploadManager = new UploadManager(cfg);
//        //...生成上传凭证，然后准备上传
//        String accessKey = "zbw3iAvaNYylRh0KjFseAxRwFoY4THhqt_Jxz68l";
//        String secretKey = "vT_aOZZDRYT35fb46Z2l3lVvvw6ek6hH0HkQer_X";
//        String bucket = "tsccg-health-space";
//        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//        String localFilePath = "D:\\code\\source\\images\\卖萌狗狗.jpg";
//        //默认不指定key的情况下，以文件内容的hash值作为文件名
////        String key = null;
//        String key = "hot-dog.jpg";
//        Auth auth = Auth.create(accessKey, secretKey);
//        String upToken = auth.uploadToken(bucket);
//        try {
//            Response response = uploadManager.put(localFilePath, key, upToken);
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
//        } catch (QiniuException ex) {
//            Response r = ex.response;
//            System.err.println(r.toString());
//            try {
//                System.err.println(r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//            }
//        }
//    }
//
//    /**
//     * 删除七牛云中的图片
//     */
//    @Test
//    public void testDelete() {
//        //构造一个带指定Zone对象的配置类
//        Configuration cfg = new Configuration(Zone.zone2());
//        //...其他参数参考类注释
//        //...生成上传凭证，然后准备上传
//        String accessKey = "zbw3iAvaNYylRh0KjFseAxRwFoY4THhqt_Jxz68l";
//        String secretKey = "vT_aOZZDRYT35fb46Z2l3lVvvw6ek6hH0HkQer_X";
//        String bucket = "tsccg-health-space";
//        String key = "FkLieZHhSvNQVEWYgj0L5FOEclDo";
//
//        Auth auth = Auth.create(accessKey, secretKey);
//        BucketManager bucketManager = new BucketManager(auth, cfg);
//        try {
//            bucketManager.delete(bucket, key);
//        } catch (QiniuException ex) {
//            //如果遇到异常，说明删除失败
//            System.err.println(ex.code());
//            System.err.println(ex.response.toString());
//        }
//    }
//}
