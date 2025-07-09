package eastgate.tenantservice.config;


import eastgate.tenantservice.entity.Event;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    @Bean
    public FieldSetMapper<Event> eventFieldSetMapper() {
        return fieldSet -> {

            String timestampStr = fieldSet.readString("timestamp");
            String eventNumberStr = fieldSet.readString("eventNumber");
            log.info("timestampStr: {}, eventNumberStr: {}", timestampStr, eventNumberStr);
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(timestampStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            Integer eventNumber = Integer.parseInt(eventNumberStr);

            return new Event(timestamp, eventNumber);
        };
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Event> eventItemReader(
            @Value("#{jobParameters['inputFilePath']}") String inputFilePath,
            FieldSetMapper<Event> eventFieldSetMapper) {
        Resource inputFileResource = new FileSystemResource(inputFilePath);
        return new FlatFileItemReaderBuilder<Event>()
                .name("eventItemReader")
                .resource(inputFileResource)
                .delimited()
                .names("timestamp", "eventNumber")
                .fieldSetMapper(eventFieldSetMapper)
                .linesToSkip(0)
                .build();
    }

    @Bean
    public ItemProcessor<Event, Event> eventItemProcessor() {
        return item -> item;
    }

    @Bean
    public JpaItemWriter<Event> eventItemWriter(EntityManagerFactory emf) {
        JpaItemWriter<Event> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public Step importEventsStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<Event> eventItemReader,
            ItemProcessor<Event, Event> eventItemProcessor,
            JpaItemWriter<Event> eventItemWriter) {

        return new StepBuilder("importEventsStep", jobRepository)
                .<Event, Event>chunk(5, transactionManager)
                .reader(eventItemReader)
                .processor(eventItemProcessor)
                .writer(eventItemWriter)
                .listener(new ChunkListener() {
                    @Override
                    public void beforeChunk(ChunkContext context) {
                        log.info("starting chunk");
                    }

                    @Override
                    public void afterChunk(ChunkContext context) {
                        log.info("chunk complete");
                    }

                    @Override
                    public void afterChunkError(ChunkContext context) {
                        log.info("chunk error");
                    }
                })
                .build();
    }

    @Bean
    public Job importEventsJob(JobRepository jobRepository, Step importEventsStep) {
        return new JobBuilder("importEventsJob", jobRepository)
                .start(importEventsStep)
                .build();
    }

}
