package de.mathema.springboot.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimestampService {

  public LocalDateTime jetzt() {
    return LocalDateTime.now();
  }

  public long epochSecond() {
    return jetzt().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
  }
}
