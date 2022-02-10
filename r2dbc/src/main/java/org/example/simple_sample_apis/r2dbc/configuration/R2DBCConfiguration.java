package org.example.simple_sample_apis.r2dbc.configuration;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
@EnableR2dbcRepositories(basePackages = "org.example.simple_sample_apis.r2dbc")
class R2DBCConfiguration extends AbstractR2dbcConfiguration {
  @Bean
  @Override
  public H2ConnectionFactory connectionFactory() {
    // see https://github.com/r2dbc/r2dbc-h2
    return new H2ConnectionFactory(
      H2ConnectionConfiguration
        .builder()
        .url("mem:tstdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;") // r2dbc:h2:mem:///testdb; mem:testdb
        .username("sa")
        .build()
    );
  }

  @Bean
  public ConnectionFactoryInitializer initializer() {
    var initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory());

    final var databasePopulator = new CompositeDatabasePopulator();
    databasePopulator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("customers-schema.sql")));
    initializer.setDatabasePopulator(databasePopulator);
    return initializer;
  }
}
