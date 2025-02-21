package com.software.modsen.rideservice.config;

import com.software.modsen.rideservice.model.CanceledRide;
import com.software.modsen.rideservice.model.MonthlyRide;
import com.software.modsen.rideservice.repository.jpa.CanceledRideRepository;
import com.software.modsen.rideservice.repository.jpa.MonthlyRideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.software.modsen.rideservice.util.BatchConstants.DEFAULT_EFFECTIVE_ROWS;
import static com.software.modsen.rideservice.util.BatchConstants.DEFAULT_TRIP_COUNT_1;
import static com.software.modsen.rideservice.util.BatchConstants.DELETE_CANCELED_RIDES_TASKLET_STEP;
import static com.software.modsen.rideservice.util.BatchConstants.MONTHLY_RIDE_AGGREGATION_JOB;
import static com.software.modsen.rideservice.util.BatchConstants.MONTHLY_RIDE_AGGREGATION_STEP;
import static com.software.modsen.rideservice.util.BatchConstants.NUM_OF_EFFECTIVE_ROWS;
import static com.software.modsen.rideservice.util.BatchConstants.UNIQUENESS_KEY;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@RequiredArgsConstructor
public class MonthlyRidesJobConfig {
    private final CanceledRideRepository canceledRideRepository;
    private final MonthlyRideRepository monthlyRideRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final JobLauncher jobLauncher;
    private final DeleteCanceledRides deleteCanceledRides;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
    private static final int CHUNK_SIZE = 100;
    private static final Long SELECT_LIMIT = 1000L;

    @Bean
    public Job rideAggregationJob() {
        return new JobBuilder(MONTHLY_RIDE_AGGREGATION_JOB, jobRepository)
                .start(rideAggregationStep())
                .next(cleanCanceledRidesStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step cleanCanceledRidesStep() {
        return new StepBuilder(DELETE_CANCELED_RIDES_TASKLET_STEP, jobRepository)
                .tasklet(deleteCanceledRides, transactionManager)
                .build();
    }

    @Bean
    public Step rideAggregationStep() {
        return new StepBuilder(MONTHLY_RIDE_AGGREGATION_STEP, jobRepository)
                .<CanceledRide, MonthlyRide>chunk(CHUNK_SIZE, transactionManager)
                .reader(rideItemReader())
                .processor(rideItemProcessor())
                .writer(rideItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<CanceledRide> rideItemReader() {
        return new ListItemReader<>(fetchListOfRecentRides());
    }

    @Bean
    @StepScope
    public ItemProcessor<CanceledRide, MonthlyRide> rideItemProcessor() {
        return ride -> new MonthlyRide(
                ride.getDriverId(),
                ride.getFinishedAt().format(formatter),
                DEFAULT_TRIP_COUNT_1
        );
    }

    @Bean
    @StepScope
    public ItemWriter<MonthlyRide> rideItemWriter() {
        return items -> {
            StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
            if (stepExecution != null) {
                ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
                long currentCount = executionContext.getLong(NUM_OF_EFFECTIVE_ROWS, DEFAULT_EFFECTIVE_ROWS);
                executionContext.putLong(NUM_OF_EFFECTIVE_ROWS, currentCount + items.size());
            }
            for (MonthlyRide item : items) {
                updateOrInsertMonthlyRide(item);
            }
        };
    }

    private void updateOrInsertMonthlyRide(MonthlyRide newMonthlyRide) {
        monthlyRideRepository.upsertMonthRide(newMonthlyRide.getDriverId(), newMonthlyRide.getMonthYear());
    }

    private List<CanceledRide> fetchListOfRecentRides() {
        return canceledRideRepository.selectWithLimit(SELECT_LIMIT);
    }

    @Scheduled(fixedRate = 10000)
    public void runJob() throws Exception {
        jobLauncher.run(
                rideAggregationJob(),
                new JobParametersBuilder().addLocalDateTime(UNIQUENESS_KEY, LocalDateTime.now()).toJobParameters()
        );
    }
}
