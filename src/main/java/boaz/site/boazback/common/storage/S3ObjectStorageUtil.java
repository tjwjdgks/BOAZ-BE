package boaz.site.boazback.common.storage;

import boaz.site.boazback.common.exception.ImageException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class S3ObjectStorageUtil implements ObjectStorageUtil{

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public S3ObjectStorageUtil() {
    }

    public S3ObjectStorageUtil(String accessKey, String secretKey, String bucket, String region) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        this.region = region;
    }

    @Override
    @PostConstruct
    public void setup() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }
    @Override
    public String upload(MultipartFile file, String filePath) {

        if(file == null){
            throw ImageException.FILE_NULL;
        }

        String fileName = "/" + UUID.randomUUID()+ "-" + file.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        try{
            s3Client.putObject(new PutObjectRequest(bucket, filePath+fileName, file.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return s3Client.getUrl(bucket, filePath+fileName).toString();
        }catch(Exception e){
            e.printStackTrace();
            throw ImageException.IMAGE_UPLOAD_ERROR;
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
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucket)
                .withPrefix(filePath+"/");

        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);
        List<String> keysList = new ArrayList<>();
        while (true) {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                keysList.add(objectSummary.getKey());
            }
            if (objectListing.isTruncated()) {
                objectListing = s3Client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket).withKeys(keysList.toArray(new String[0]));
            this.s3Client.deleteObjects(deleteObjectsRequest);
        }
    }

    @Override
    public void removeFiles(List<String> fileUrl) {
        String[] filePaths = fileUrl.stream().map(f -> getFilePathFormUrlByBucket(f)).toArray(String[]::new);
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket).withKeys(filePaths);
        this.s3Client.deleteObjects(deleteObjectsRequest);
    }

    @Override
    public void removeFile(String fileUrl) {
        String filePath = getFilePathFormUrlByBucket(fileUrl);
        this.s3Client.deleteObject(bucket,filePath);
    }
    /**
     * https://[bucket-name].s3.[region-code].amazonaws.com/[file path] 로 구성되어 있습니다.
     * 그중 file path 만 bucket name 을 기반으로 split 하여 뒤에 file path를 얻어오는 함수 입니다
     * @param fileUrl
     * @return filePath
     */
    @Override
    public String getFilePathFormUrlByBucket(String fileUrl) {
        return fileUrl.split("com/")[1];
    }

}
