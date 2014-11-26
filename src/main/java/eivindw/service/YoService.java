package eivindw.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class YoService {

   private final Logger log = LoggerFactory.getLogger(YoService.class);

   public String sayYo(String name) {
      MDC.put("sourceThread", Thread.currentThread().getName());
      log.info("Say yo to {}", name);
      return "yo! " + name;
   }
}
