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
public class VariableQuery {

  private String name;
  private String operator;
  private Object value;

  public static VariableQuery eq(final String name, final Long value) {
    return VariableQuery.builder().name(name).operator("eq").value(value).build();
  }
}
