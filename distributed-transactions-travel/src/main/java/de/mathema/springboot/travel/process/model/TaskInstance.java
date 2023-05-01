package de.mathema.springboot.travel.process.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskInstance {

  private String id;
  private String name;
  private String taskDefinitionKey;
  private String processInstanceId;
  private String processDefinitionId;
}
