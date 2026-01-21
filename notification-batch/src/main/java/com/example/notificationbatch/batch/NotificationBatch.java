package com.example.notificationbatch.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.ZonedDateTime;

@Slf4j
@Configuration
public class NotificationBatch {
    @Value("${spring.mail.username}")
    private String email;

    @Bean
    public Job notificationJob(JobRepository jobRepository, Step notificationStep) {
        return new JobBuilder("mail-send-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(notificationStep)
                .build();
    }

    @Bean
    public Step notificationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                 ItemReader<NotificationInfo> itemReader, ItemWriter<NotificationInfo> itemWriter) {
        return new StepBuilder("mail-send-step", jobRepository)
                .<NotificationInfo, NotificationInfo>chunk(10, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<NotificationInfo> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<NotificationInfo>()
                .name("followerReader")
                .dataSource(dataSource)
                .sql("""
                        SELECT f.follow_id, u.email, u.username, f.follower_id,
                            u2.username as follower_name, f.follow_datetime
                          FROM follow f JOIN user u  ON(f.user_id = u.user_id)
                                        JOIN user u2 ON(f.follower_id = u2.user_id)
                         WHERE f.mail_sent_datetime is null;
                """)
                .beanRowMapper(NotificationInfo.class)
                .build();
    }

    @Bean
    public ItemWriter<NotificationInfo> sendMail(JavaMailSender mailSender, JdbcTemplate jdbcTemplate) {
        return items -> {
            if(items.isEmpty()) {
                log.info("No data to process.");
                return;
            }

            for(NotificationInfo item: items) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(email);
                message.setTo(item.getEmail());
                message.setSubject("New Follower!");
                message.setText("Hello, " + item.getUsername() + "!\n" + item.getFollowerName() + " is now follow you!");
                mailSender.send(message);

                jdbcTemplate.update("UPDATE follow SET mail_sent_datetime = ? WHERE follow_id = ?", ZonedDateTime.now(), item.getFollowId());
            }
        };
    }
}
