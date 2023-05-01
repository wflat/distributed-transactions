package de.mathema.springboot.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.management.JobDefinition;
import org.camunda.bpm.engine.runtime.Job;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.awaitility.Awaitility.await;
import static org.camunda.bpm.engine.impl.util.ClockUtil.getCurrentTime;

@Slf4j
public final class ProcessUtils {

  public static void removeProcesses(final ProcessEngine processEngine, final String processId) {
    processEngine.getRuntimeService().createProcessInstanceQuery()
        .processDefinitionKey(processId).list()
        .forEach(pi -> processEngine.getRuntimeService().deleteProcessInstance(pi.getProcessInstanceId(), "Outdated"));
  }

  public static List<String> executeAvailableJobs(final ProcessEngine processEngine,
      final int expectedExecutions, final long timeoutInSeconds) {
    final List<String> executedJobs = new ArrayList<>();
    final Duration pollIntervalDuration =
        timeoutInSeconds <= 0 ? Duration.ofMillis(1) : Duration.ofMillis(100);
    final Duration atMostDuration =
        timeoutInSeconds <= 1 ? Duration.ofSeconds(1) : Duration.ofSeconds(timeoutInSeconds);

    await().atMost(atMostDuration).pollInterval(pollIntervalDuration).until(() -> {
      for (final Job job : getAvailableJobs(processEngine)) {
        final JobDefinition jobDefinition = jobDefinition(processEngine, job);
        log.info(String.format("Execute job: %s, %s", job.getId(), jobDefinition.getActivityId()));

        execute(processEngine, job);
        executedJobs.add(jobDefinition.getActivityId());
      }
      return executedJobs.size() >= expectedExecutions;
    });

    return executedJobs;
  }

  private static void execute(final ProcessEngine engine, final Job job) {
    Job current = engine.getManagementService().createJobQuery().jobId(job.getId()).singleResult();
    if (current == null) {
      throw new IllegalStateException(
          format("Illegal state when calling execute(job = '%s') - job does not exist anymore!",
              job));
    }
    engine.getManagementService().executeJob(job.getId());
  }

  private static JobDefinition jobDefinition(final ProcessEngine engine, final Job job) {
    return engine.getManagementService().createJobDefinitionQuery()
        .jobDefinitionId(job.getJobDefinitionId()).singleResult();
  }

  private static List<Job> getAvailableJobs(ProcessEngine engine) {
    return engine.getManagementService().createJobQuery().withRetriesLeft().list().stream()
        .filter(job -> !job.isSuspended() && (job.getDuedate() == null || getCurrentTime().after(
            job.getDuedate())))
        .collect(Collectors.toList());
  }
}
