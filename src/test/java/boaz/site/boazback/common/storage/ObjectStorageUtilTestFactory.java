package boaz.site.boazback.common.storage;

public class ObjectStorageUtilTestFactory {

    public static MinioObjectStorageUtil getMinoStorage(){
        String bucket = "<BUCKET NAME>";
        String accessKey = "<STORAGE BUCKET ACCESS KEY>";
        String secretKey = "<STORAGE BUCKET SECRET KEY>";
        String endpoint = "<ENDPOINT>";
        MinioObjectStorageUtil mino = new MinioObjectStorageUtil(accessKey,secretKey,endpoint,bucket);
        return mino;
    }
    public static S3ObjectStorageUtil getS3Storage(){
        String bucket = "<BUCKET NAME>";
        String accessKey = "<STORAGE BUCKET ACCESS KEY>";
        String secretKey = "<STORAGE BUCKET SECRET KEY>";
        String region = "";
        S3ObjectStorageUtil s3  = new S3ObjectStorageUtil(accessKey,secretKey,bucket,region);
        return s3;
    }
}
