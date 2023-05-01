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
public class Variable {

  private Object value;
  private String type;

  public static Variable bool(final Boolean value) {
    return Variable.builder().value(value).type("Boolean").build();
  }

  public static Variable number(final Long value) {
    return Variable.builder().value(value).type("Long").build();
  }

  public static Variable string(final String value) {
    return Variable.builder().value(value).type("String").build();
  }
}
