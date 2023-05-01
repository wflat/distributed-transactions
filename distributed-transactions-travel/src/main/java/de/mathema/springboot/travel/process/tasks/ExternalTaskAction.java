package de.mathema.springboot.travel.process.tasks;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.engine.variable.VariableMap;

public interface ExternalTaskAction {

  VariableMap verarbeite(ExternalTask externalTask);
}
