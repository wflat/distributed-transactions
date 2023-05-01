package de.mathema.springboot.infrastructure.camunda;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.camunda.spin.impl.json.jackson.format.JacksonJsonDataFormat;
import org.camunda.spin.spi.DataFormatConfigurator;
import org.springframework.stereotype.Component;

@Component
public class JacksonDataFormatConfigurator implements
    DataFormatConfigurator<JacksonJsonDataFormat> {

  public void configure(final JacksonJsonDataFormat dataFormat) {
    dataFormat.getObjectMapper().registerModule(new JavaTimeModule());
  }

  public Class<JacksonJsonDataFormat> getDataFormatClass() {
    return JacksonJsonDataFormat.class;
  }
}
