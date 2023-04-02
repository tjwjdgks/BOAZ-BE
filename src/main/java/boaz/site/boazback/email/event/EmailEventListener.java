package boaz.site.boazback.email.event;

import boaz.site.boazback.common.domain.TracingFunction;
import boaz.site.boazback.common.util.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Async
@RequiredArgsConstructor
public class EmailEventListener {

    private final MailUtil mailUtil;

    @EventListener
    @TracingFunction
    public void handleEmailJoinEvent(EmailJoinEvent emailJoinEvent){
        log.info("join event "+emailJoinEvent.getEmail() + " " + emailJoinEvent.getCertificationCode()+ " send");
        mailUtil.sendMailForJoin(emailJoinEvent.getEmail(), emailJoinEvent.getCertificationCode());
    }

    @EventListener
    @TracingFunction
    public void handleEmailPasswordReissueEvent(EmailPasswordReissueEvent reissueEvent){
        log.info("reissue event "+reissueEvent.getEmail() + " " + reissueEvent.getCertificationCode()+ " send");
        mailUtil.sendEmailForPasswordReissue(reissueEvent.getEmail(),reissueEvent.getCertificationCode());
    }
}
