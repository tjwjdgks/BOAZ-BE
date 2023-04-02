package boaz.site.boazback.common.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public interface ObjectStorageUtil {
    void setup();
    String upload(MultipartFile file, String filePath);
    List<String> uploadFiles(List<MultipartFile> files,String filePath);
    void removeFilesByFilePath(String filePath);
    void removeFiles(List<String> fileUrl);
    void removeFile(String fileUrl);
    default List<String> getFilesName(List<MultipartFile> files){
        return files.stream().map(f->f.getOriginalFilename())
                .map(f->f.toLowerCase())
                .collect(Collectors.toList());
    }
    String getFilePathFormUrlByBucket(String fileUrl);

    default String urlPrefix(){
        return "https://";
    }
}
