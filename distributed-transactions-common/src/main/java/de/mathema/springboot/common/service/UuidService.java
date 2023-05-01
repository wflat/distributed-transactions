package de.mathema.springboot.common.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UuidService {

  public UUID uuid() {
    return UUID.randomUUID();
  }
}
