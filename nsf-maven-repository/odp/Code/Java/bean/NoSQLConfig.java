package bean;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openntf.xsp.jakarta.nosql.driver.NoSQLConfigurationBean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NoSQLConfig implements NoSQLConfigurationBean {
	@Inject @ConfigProperty(name="logDqlExplains", defaultValue = "false")
	private boolean logDqlExplains;

	@Override
	public boolean emitExplainEvents() {
		return logDqlExplains;
	}
	
	@Override
	public boolean shouldDqlRefreshViews(Class<?> entityType, String dqlQuery) {
		return true;
	}

}
