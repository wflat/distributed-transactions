package de.mathema.springboot.travel.config.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import de.mathema.springboot.common.service.JsonService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ServicesConfig {

  @Bean
  @ConfigurationProperties(prefix = "travel.service")
  public ServicesProperties servicesProperties() {
    return new ServicesProperties();
  }

  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
    return new MappingJackson2HttpMessageConverter(JsonService.createObjectMapper(false));
  }
}
