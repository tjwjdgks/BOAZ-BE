package boaz.site.boazback.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
/**
 * @EnableAsync의 기본 executor SimpleAsyncTaskExecutor 사용
 * SimpleAsyncTaskExecutor는 task 작업마다 스레드 생성하므로 좋지 않음
 * 따라서 새로운 executor 사용( 스레드풀 기본 : cpu 코어 수, max : 코어 * 2, 기다리는 queue : 10개)
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors*2);
        executor.setQueueCapacity(10);
        // 이상적인 thread 수(setCorePoolSize 수)를 넘은 thread들을 언제 까지 살리다가 정리할 것인지
        executor.setKeepAliveSeconds(60);
        executor.initialize();
        return executor;
    }
}
