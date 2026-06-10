package jaxrs;

import java.io.IOException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;

@Provider
@PreMatching
public class LoggingFilter implements ContainerRequestFilter {
	public static boolean DEBUG = false;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(DEBUG) {
			System.out.println("======");
			System.out.println(requestContext.getMethod() + " " + requestContext.getUriInfo().getRequestUri());
			requestContext.getHeaders().forEach((key, value) -> System.out.println(key + " => " + value));
			System.out.println("======");
		}
	}

}
