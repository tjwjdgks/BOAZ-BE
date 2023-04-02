package boaz.site.boazback.intro.application;


import boaz.site.boazback.intro.domain.Intro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IntroRepository extends JpaRepository<Intro, Long> {
    @Query(value = "select count(id) from intro where erase = false",nativeQuery = true)
    Long getCount();

    Page<Intro> findByOrderByCreatedDateDesc(Pageable pageable);



}
