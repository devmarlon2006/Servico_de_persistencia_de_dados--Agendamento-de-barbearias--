package br.com.devmarlon2006.registrationbarberservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    /*
    Data soucer calues
     */

    @Value( "${spring.datasource.url}")
    String url;
    @Value( "${spring.datasource.username}" )
    String username;
    @Value( "${spring.datasource.password}" )
    String password;
    @Value( "${spring.datasource.driver-class-name}" )
    String driverClassName;


    @Bean
    public DataSource hikaryDataSourceConfig() {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl( url );
        hikariConfig.setUsername( username );
        hikariConfig.setPassword( password );
        hikariConfig.setDriverClassName( driverClassName );

        hikariConfig.setMaximumPoolSize( 10 );
        hikariConfig.setMinimumIdle( 5 );

        return new HikariDataSource(hikariConfig);
    }
}
