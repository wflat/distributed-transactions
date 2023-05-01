package de.mathema.springboot.car.service;

import de.mathema.springboot.common.exception.BadRequestException;
import de.mathema.springboot.common.exception.ConflictException;
import de.mathema.springboot.common.exception.InternalServerErrorException;
import de.mathema.springboot.common.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.nio.charset.StandardCharsets;

@ControllerAdvice
@Slf4j
public class HttpStatusExceptionHandler {

  static final MediaType TEXT_PLAIN_UTF8 =
      new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8);
  static final String FORMAT_STR = "{}: {}";


  @ExceptionHandler({
      NotFoundException.class,
  })
  public final ResponseEntity<Object> handleNotFoundException(
      final NotFoundException ex, final WebRequest request) {
    log.info(FORMAT_STR, ex.getClass().getSimpleName(), ex.getMessage());

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .contentType(TEXT_PLAIN_UTF8)
        .body(ex.getMessage());
  }

  @ExceptionHandler({
      InternalServerErrorException.class,
  })
  public final ResponseEntity<Object> handleInternalServerErrorException(
      final InternalServerErrorException ex, final WebRequest request) {
    log.info(FORMAT_STR, ex.getClass().getSimpleName(), ex.getMessage());

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(TEXT_PLAIN_UTF8)
        .body(ex.getMessage());
  }

  @ExceptionHandler({
      BadRequestException.class,
  })
  public final ResponseEntity<Object> handleBadRequestException(
      final BadRequestException ex, final WebRequest request) {
    log.info(FORMAT_STR, ex.getClass().getSimpleName(), ex.getMessage());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .contentType(TEXT_PLAIN_UTF8)
        .body(ex.getMessage());
  }

  @ExceptionHandler({
      ConflictException.class,
      ObjectOptimisticLockingFailureException.class
  })
  public final ResponseEntity<Object> handleConflictException(
      final RuntimeException ex, final WebRequest request) {
    log.info(FORMAT_STR, ex.getClass().getSimpleName(), ex.getMessage());

    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .contentType(TEXT_PLAIN_UTF8)
        .body(ex.getMessage());
  }
}
