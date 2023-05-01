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
public class ProcessInstance {

  private String id;
  private String definitionId;
  private String businessKey;
}
