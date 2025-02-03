package com.example.notificationbatch.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job notificationJob;

    @Scheduled(fixedRate = 10000)
    public void runBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", Instant.now().toEpochMilli())
                    .toJobParameters();
            JobExecution execution = jobLauncher.run(notificationJob, jobParameters);

//            log.info("Batch job 실행됨. 상태 : {}", execution.getStatus());
//            for(StepExecution stepExecution : execution.getStepExecutions()) {
//                log.info("Step: {} | 처리된 건수: {}", stepExecution.getStepName(), stepExecution.getReadCount());
//            }
        } catch(JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
