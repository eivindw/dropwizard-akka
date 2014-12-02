package eivindw.api;

import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;
import java.util.UUID;

public class CorIdFilter implements ContainerRequestFilter {

   @Override
   public void filter(ContainerRequestContext ctx) throws IOException {
      MDC.put("corId", Integer.toHexString(UUID.randomUUID().hashCode()));
   }
}
