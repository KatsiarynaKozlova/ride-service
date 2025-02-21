package com.software.modsen.rideservice.config;

import com.software.modsen.rideservice.repository.jpa.CanceledRideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import static com.software.modsen.rideservice.util.BatchConstants.DEFAULT_EFFECTIVE_ROWS;
import static com.software.modsen.rideservice.util.BatchConstants.NUM_OF_EFFECTIVE_ROWS;

@Component
@RequiredArgsConstructor
public class DeleteCanceledRides implements Tasklet {
    private final CanceledRideRepository canceledRideRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        long numOfEffectiveRows = chunkContext.getStepContext().getStepExecution().getJobExecution()
                .getExecutionContext().getLong(NUM_OF_EFFECTIVE_ROWS, DEFAULT_EFFECTIVE_ROWS);
        if (numOfEffectiveRows != 0) canceledRideRepository.deleteTop(numOfEffectiveRows);
        return RepeatStatus.FINISHED;
    }
}
