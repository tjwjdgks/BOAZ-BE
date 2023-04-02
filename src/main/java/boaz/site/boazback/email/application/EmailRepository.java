package boaz.site.boazback.email.application;

import boaz.site.boazback.email.domain.Email;
import boaz.site.boazback.email.domain.EmailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email,Long> {

    Optional<Email> findByUserEmailAndEmailType(String userEmail, EmailType emailType);

    Optional<Email> findByAuthCodeAndEmailType(String authCode, EmailType emailType);



}
