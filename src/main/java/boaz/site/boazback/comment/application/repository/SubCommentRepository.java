package boaz.site.boazback.comment.application.repository;


import boaz.site.boazback.comment.domain.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubCommentRepository extends JpaRepository<SubComment,Long> {

    List<SubComment> findByComment_Id(Long id);
}
