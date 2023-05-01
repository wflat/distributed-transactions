package de.mathema.springboot.car.events;

import de.mathema.springboot.car.repository.CarOutputRepository;
import de.mathema.springboot.common.jms.JmsMessageSender;
import de.mathema.springboot.common.service.JsonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@RequiredArgsConstructor
@Transactional(TxType.REQUIRED)
@Slf4j
@Component
@Profile("!test")
public class ProcessOutputEventsJob {

  private final CarOutputRepository carOutputRepository;
  private final JmsMessageSender carJmsMessageSender;
  private final JsonService jsonService;

  @Scheduled(initialDelayString = "PT0S", fixedDelay = 100)
  @SchedulerLock(name = "ProcessOutputEventsJob.process")
  public void process() {
    log.debug("Start processing ...");

    carOutputRepository.getFirstEvent().ifPresent(outputEvent -> {
      log.info("Process OutputEvent: {}/{}", outputEvent.getId(), outputEvent.getCreated().toString());

      // TODO: Inhalt von OutputEvent mit carJmsMessageSender versenden

      outputEvent.setProcessed(true);
    });

    log.debug("End processing");
  }
}
