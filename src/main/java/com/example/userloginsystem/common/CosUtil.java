package com.example.userloginsystem.common;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.util.UUID;

public class CosUtil {

    private static final String secretId = "你的SecretId";
    private static final String secretKey = "你的SecretKey";

    private static final String bucketName = "user-avatar-125xxxxxxx";

    private static final String regionName = "ap-shanghai";

    private static final String urlPrefix =
            "https://user-avatar-125xxxxxxx.cos.ap-shanghai.myqcloud.com/";

    public static String upload(File file) {

        BasicCOSCredentials cred =
                new BasicCOSCredentials(secretId, secretKey);

        ClientConfig clientConfig =
                new ClientConfig(new Region(regionName));

        COSClient cosClient =
                new COSClient(cred, clientConfig);

        String key =
                "avatar/" +
                        UUID.randomUUID() +
                        ".jpg";

        PutObjectRequest request =
                new PutObjectRequest(bucketName, key, file);

        cosClient.putObject(request);

        cosClient.shutdown();

        return urlPrefix + key;
    }

}