package com.example.userloginsystem.service;

import com.example.userloginsystem.config.CosConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CosService {

    private final CosConfig cos;

    public String uploadAvatar(File file, String contentType) throws Exception {
        BasicCOSCredentials cred = new BasicCOSCredentials(cos.getSecretId(), cos.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(cos.getRegion()));
        COSClient client = new COSClient(cred, clientConfig);

        try {
            String ext = guessExt(contentType);
            String key = "avatar/" + UUID.randomUUID() + ext;

            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.length());
            if (contentType != null && !contentType.isBlank()) {
                meta.setContentType(contentType);
            }

            PutObjectRequest req = new PutObjectRequest(
                    cos.getBucket(),
                    key,
                    new FileInputStream(file),
                    meta
            );

            client.putObject(req);

            String prefix = cos.getUrlPrefix();
            if (prefix.endsWith("/")) prefix = prefix.substring(0, prefix.length() - 1);
            return prefix + "/" + key;
        } finally {
            client.shutdown();
        }
    }

    private String guessExt(String contentType) {
        if (contentType == null) return ".jpg";
        if (contentType.contains("png")) return ".png";
        if (contentType.contains("webp")) return ".webp";
        return ".jpg";
    }
}