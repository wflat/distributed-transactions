package de.mathema.springboot.travel.events;

import de.mathema.springboot.common.jms.JmsMessageSender;
import de.mathema.springboot.common.model.EntityEvent;
import de.mathema.springboot.common.service.JsonService;
import de.mathema.springboot.travel.repository.TravelOutputRepository;
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

  private final TravelOutputRepository travelOutputRepository;
  private final JmsMessageSender hotelJmsMessageSender;
  private final JmsMessageSender carJmsMessageSender;
  private final JmsMessageSender flightJmsMessageSender;
  private final JsonService jsonService;

  @Scheduled(initialDelayString = "PT0S", fixedDelay = 100)
  @SchedulerLock(name = "ProcessOutputEventsJob.process")
  public void process() {
    log.debug("Start processing ...");

    travelOutputRepository.getFirstEvent().ifPresent(outputEvent -> {
      log.info("Process OutputEvent: {}/{}/{}",
          outputEvent.getId(), outputEvent.getEntityType(), outputEvent.getCreated().toString());

      final EntityEvent event = jsonService.objekt(outputEvent.getEventJson(), EntityEvent.class);
      switch (outputEvent.getEntityType()) {
        case HOTEL -> hotelJmsMessageSender.convertAndSend(event);
        case CAR -> carJmsMessageSender.convertAndSend(event);
        case FLIGHT -> flightJmsMessageSender.convertAndSend(event);
      }
      outputEvent.setProcessed(true);
    });

    log.debug("End processing");
  }
}
