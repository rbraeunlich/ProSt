package de.blogspot.wrongtracks.prost.blueprint;

import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.osgi.blueprint.ConfigurationFactory;

/**
 * Apache Aries Blueprint cannot inject setters which have a non void return
 * value. Because I do not want to change and recompile every version of Aries
 * used in every server this class exists.
 * 
 * @author Ronny Bräunlich
 * 
 */
public class ProStConfigurationFactory extends ConfigurationFactory {
	private String dataSourceJndiName;
	private String databaseType;
	private String jdbcUsername;
	private String jdbcPassword;
	private String mailServerHost;
	private String mailServerDefaultFrom;
	private String history;

	@Override
	public StandaloneProcessEngineConfiguration getConfiguration() {
		StandaloneProcessEngineConfiguration configuration = super
				.getConfiguration();
		configuration.setDataSourceJndiName(dataSourceJndiName);
		configuration.setDatabaseType(databaseType);
		configuration.setJdbcUsername(jdbcUsername);
		configuration.setJdbcPassword(jdbcPassword);
		configuration.setMailServerHost(mailServerHost);
		configuration.setMailServerDefaultFrom(mailServerDefaultFrom);
		configuration.setHistory(history);
		return configuration;
	}

	public void setDataSourceJndiName(String dataSourceJndiName) {
		this.dataSourceJndiName = dataSourceJndiName;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public void setMailServerDefaultFrom(String mailServerDefaultFrom) {
		this.mailServerDefaultFrom = mailServerDefaultFrom;
	}

	public void setHistory(String history) {
		this.history = history;
	}
}
