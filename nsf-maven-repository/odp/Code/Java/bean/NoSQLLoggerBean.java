package bean;

import org.openntf.xsp.jakarta.nosql.driver.ExplainEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class NoSQLLoggerBean {
	public void logDqlExplain(@Observes ExplainEvent event) {
		System.out.println(event.explain());
	}
}
