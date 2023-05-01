package de.mathema.springboot.infrastructure.camunda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.migration.MigrationPlan;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessInstancesMigration {

  public static final String PROCESS_DEFINITION_KEY = "Process_ID";

  @Async
  public void migrate(final ProcessEngine processEngine) {
    if (processEngine != null) {
      final RepositoryService repositoryService = processEngine.getRepositoryService();
      final RuntimeService runtimeService = processEngine.getRuntimeService();
      final List<ProcessDefinition> latestProcessDefinitions =
          repositoryService.createProcessDefinitionQuery().latestVersion().list()
              .stream().filter(pd -> pd.getKey().equals(PROCESS_DEFINITION_KEY))
              .toList();
      final List<ProcessDefinition> oldProcessDefinitions = repositoryService.createProcessDefinitionQuery()
          .list().stream()
          .filter(pdOld -> pdOld.getKey().equals(PROCESS_DEFINITION_KEY) &&
              latestProcessDefinitions.stream()
                  .noneMatch(pdLatest -> pdLatest.getId().equals(pdOld.getId())))
          .toList();

      latestProcessDefinitions.forEach(pd -> log.info(String
          .format("Latest ProcessDefinition: name=%s, id=%s, version=%d, versionTag=%s",
              pd.getName(), pd.getKey(), pd.getVersion(), pd.getVersionTag())));
      oldProcessDefinitions.forEach(pd -> log.info(String
          .format("Old ProcessDefinition: name=%s, id=%s, version=%d, versionTag=%s",
              pd.getName(), pd.getKey(), pd.getVersion(), pd.getVersionTag())));

      log.info("Start Migration of {}. ProcessDefinitions", oldProcessDefinitions.size());
      oldProcessDefinitions.forEach(oldPd -> {
        final ProcessDefinition latestPd = latestProcessDefinitions.stream()
            .filter(pd -> pd.getKey().equals(oldPd.getKey())).findFirst().orElse(null);
        if (latestPd != null) {
          log.info("Migrate from {}/{} to {}/{}", oldPd.getId(), oldPd.getVersionTag(),
              latestPd.getId(), latestPd.getVersionTag());

          try {
            final MigrationPlan migrationPlan = runtimeService
                .createMigrationPlan(oldPd.getId(), latestPd.getId())
                .mapEqualActivities()
                .build();
            final List<String> processIds = runtimeService.createProcessInstanceQuery()
                .processDefinitionId(migrationPlan.getSourceProcessDefinitionId())
                .list().stream().map(ProcessInstance::getId).toList();

            if (!processIds.isEmpty()) {
              final Batch batch = runtimeService.newMigration(migrationPlan)
                  .processInstanceIds(processIds)
                  .executeAsync();
              log.info(
                  "Migration : #{} ProcessInstances will be migrate async from {}/{} to {}/{} with #{} Jobs",
                  processIds.size(), oldPd.getId(), oldPd.getVersionTag(),
                  latestPd.getId(), latestPd.getVersionTag(), batch.getTotalJobs());
            } else {
              log.info("Migration : No ProcessInstances from {}/{} to {}/{} available",
                  oldPd.getId(), oldPd.getVersionTag(), latestPd.getId(), latestPd.getVersionTag());
            }
          } catch (final Exception e) {
            log.info("Error in Migration from {}/{} to {}/{} : {}",
                oldPd.getId(), oldPd.getVersionTag(), latestPd.getId(), latestPd.getVersionTag(),
                e.getMessage());
          }

          log.info("Migration finished: from {}/{} to {}/{}", oldPd.getId(), oldPd.getVersionTag(),
              latestPd.getId(), latestPd.getVersionTag());
        }
      });
      log.info("End of Migration of {}. ProcessDefinitions", oldProcessDefinitions.size());
    }
  }
}
