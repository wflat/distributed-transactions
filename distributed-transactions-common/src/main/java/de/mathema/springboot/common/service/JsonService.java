package de.mathema.springboot.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.mathema.springboot.common.exception.JsonInvalidException;

@Component
public class JsonService {

  private final ObjectMapper objectMapper;
  private final ObjectMapper objectMapperOhneNullFelder;

  public JsonService() {
    objectMapper = createObjectMapper(false);
    objectMapperOhneNullFelder = createObjectMapper(true);
  }

  public <T> List<T> liste(final String json, final Class<T> clazz) {
    try {
      return objectMapper.readValue(json,
          objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    } catch (final Exception e) {
      throw new JsonInvalidException(String.format("Can not create List<%s> form JSON String", clazz.getSimpleName()), e);
    }
  }

  public <K, V> Map<K, V> map(final String json, final Class<K> clazzKey, final Class<V> clazzValue) {
    try {
      return objectMapper.readValue(json,
          objectMapper.getTypeFactory().constructMapType(Map.class, clazzKey, clazzValue));
    } catch (final Exception e) {
      throw new JsonInvalidException(String.format("Can not create Map<%s, %s> from JSON String",
          clazzKey.getSimpleName(), clazzValue.getSimpleName()), e);
    }
  }

  public <T> T objekt(final String json, final Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (final Exception e) {
      throw new JsonInvalidException(
          String.format("Aus JSON String kann kein %s Objekt erstellt werden",
              clazz.getSimpleName()), e);
    }
  }

  public String json(final Object obj) {
    return json(obj, false);
  }

  public String json(final Object obj, final boolean ignoriereNullFelder) {
    return json(ignoriereNullFelder ? objectMapperOhneNullFelder : objectMapper, obj);
  }

  String json(final ObjectMapper objectMapper, final Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (final Exception e) {
      throw new JsonInvalidException(
          String.format("Aus %s Objekt kann kein JSON Strign erstellt werden",
              obj == null ? "<null>" : obj.getClass().getSimpleName()), e);
    }
  }

  public static ObjectMapper createObjectMapper(final boolean ignoreNullValues) {
    ObjectMapper result = new ObjectMapper();
    result.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    result.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    result.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    result.registerModule(new JavaTimeModule());
    if (ignoreNullValues) {
      result.setSerializationInclusion(Include.NON_NULL);
    }
    return result;
  }
}
