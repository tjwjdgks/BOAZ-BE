package boaz.site.boazback.common.storage;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

//@Service
public class MinioObjectStorageUtil implements ObjectStorageUtil{

    private MinioClient minioClient;

    @Value("${minio.accesskey}")
    private String accessKey;

    @Value("${minio.secretkey}")
    private String secretKey;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.bucket}")
    private String bucket;

    @Autowired
    public MinioObjectStorageUtil(){
    }

    public MinioObjectStorageUtil(String accessKey, String secretKey, String endpoint, String bucket) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.endpoint = endpoint;
        this.bucket = bucket;
    }

    @Override
    @PostConstruct
    public void setup() {
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey,secretKey)
                .build();
    }
    @Override
    public String upload(MultipartFile file, String filePath) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalStateException("file name null");
        }
        String extension = fileName.split("\\.")[1].toLowerCase();
        if(extension.contains(".jpg")) {
            extension = extension.replace(".jpg", ".jpeg");
        }
        fileName =  UUID.randomUUID().toString()+"."+extension;
        try{
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if(found){
                String path = !Objects.equals(filePath, "") ? filePath+"/"+ fileName : fileName;
                byte[] bytes = IOUtils.toByteArray(file.getInputStream());
                ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);
                minioClient.putObject(PutObjectArgs.builder()
                        .object(path)
                        .bucket(bucket)
                        .contentType(file.getContentType())
                        .stream(byteArrayIs,byteArrayIs.available(),-1)
                        .build());
                byteArrayIs.close();
                return endpoint+"/"+bucket+"/"+path;
            }
            return "";
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalStateException("image send error");
        }
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String filePath) {
        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            String path = upload(file,filePath);
            imagePaths.add(urlPrefix()+path);
        }
        return imagePaths;
    }

    @Override
    public void removeFilesByFilePath(String filePath) {
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(filePath+"/")
                .build();
        Iterable<Result<Item>> results = minioClient.listObjects(listObjectsArgs);
        for(Result<Item> item : results){
            try {
                String objectName = item.get().objectName();
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
            } catch (Exception e){
                throw new IllegalStateException("files search error");
            }
        }
    }

    @Override
    public void removeFiles(List<String> fileUrl) {
        List<DeleteObject> objects = fileUrl.stream()
                .map(f -> getFilePathFormUrlByBucket(f))
                .map(p -> new DeleteObject(p))
                .collect(Collectors.toList());
        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket(bucket).objects(objects).build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);

        if(results.iterator().hasNext()) throw new IllegalStateException("files remove error");

    }

    @Override
    public void removeFile(String fileUrl) {
        String filePath = getFilePathFormUrlByBucket(fileUrl);
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucket).object(filePath).build();
        try {
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e){
            throw new IllegalStateException("file remove error");
        }
    }
    /**
     * https://[storage url]/[bucket name]/[file path] 로 구성되어 있습니다.
     * 그중 file path 만 bucket name 을 기반으로 split 하여 뒤에 file path를 얻어오는 함수 입니다
     * @param fileUrl
     * @return filePath
     */
    @Override
    public String getFilePathFormUrlByBucket(String fileUrl) {
        String bucketUrl = bucket+"/";
        return (fileUrl).split(bucketUrl)[1];
    }
}
