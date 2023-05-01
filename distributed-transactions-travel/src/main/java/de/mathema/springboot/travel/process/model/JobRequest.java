package de.mathema.springboot.travel.process.model;

import java.util.List;
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
public class JobRequest {

  private String processInstanceId;
  private String activityId;
  private int firstResult;
  private int maxResults;
  private List<Sort> sorting;

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static final class Sort {

    private String sortBy;
    private String sortOrder;
  }
}
