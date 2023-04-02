package boaz.site.boazback.common.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DeleteFileUrlWrapper {

    List<String> fileUrls;
    String fileUrl;



    @Builder
    public DeleteFileUrlWrapper(List<String> fileUrls, String fileUrl) {
        this.fileUrls = fileUrls;
        this.fileUrl = fileUrl;
    }
}
