package ptit.htpt.bookstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Primary
    @Qualifier("share")
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:sqlserver://DESKTOP-JL38HGV\\TOANTM;databaseName=BookStore");
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("123456");
        return dataSourceBuilder.build();
    }

    @Qualifier("ds1")
    @Bean
    public DataSource dataSourceHaNoi() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:sqlserver://DESKTOP-JL38HGV\\TOANTM;databaseName=BookStore");
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("123456");
        return dataSourceBuilder.build();
    }
    @Qualifier("ds2")
    @Bean
    public DataSource dataSourceHCM() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:sqlserver://DESKTOP-JL38HGV\\TOANTM1;databaseName=BookStore");
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("123456");
        return dataSourceBuilder.build();
    }

    @Qualifier("template1")
    @Bean
    public JdbcTemplate jdbcTemplate1(@Qualifier("ds1") @Autowired DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }

    @Qualifier("template2")
    @Bean
    public JdbcTemplate jdbcTemplate2(@Qualifier("ds2") @Autowired DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }

}
