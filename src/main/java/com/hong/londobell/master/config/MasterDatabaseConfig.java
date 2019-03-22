package com.hong.londobell.master.config;

import com.hong.londobell.master.entity.MasterTenant;
import com.hong.londobell.master.repository.MasterTenantRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Configuration of the master database which holds information about tenants in the application.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(MasterDatabaseConfigProperties.class)
@EnableJpaRepositories(basePackages = {"com.hong.londobell.master.entity", "com.hong.londobell.master.repository"},
        entityManagerFactoryRef = "masterEntityManagerFactory", transactionManagerRef = "masterTransactionManager")
public class MasterDatabaseConfig {

    public static final Logger LOG = LoggerFactory.getLogger(MasterDatabaseConfig.class);

    /**
     * Master database configuration properties like username, password, etc.
     */
    @Autowired
    private MasterDatabaseConfigProperties masterDbProperties;

    /**
     * Create the master datasource bean which is required for creating the entity manager factory bean.
     *
     * Note that using names for beans is not mandatory but it is a good practice to ensure that
     * the intended beans are being used where required.
     *
     * @return
     */
    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {

        LOG.info("Setting up masterDataSource with: " + masterDbProperties.toString());

        HikariDataSource ds = new HikariDataSource();

        ds.setUsername(masterDbProperties.getUsername());
        ds.setPassword(masterDbProperties.getPassword());
        ds.setJdbcUrl(masterDbProperties.getUrl());
        ds.setDriverClassName(masterDbProperties.getDriverClassName());
        ds.setPoolName(masterDbProperties.getPoolName());

        // HikariCP settings
        // Maximum number of actual connection in the pool
        ds.setMaximumPoolSize(masterDbProperties.getMaxPoolSize());

        // Minimum number of idle connections in the pool
        ds.setMinimumIdle(masterDbProperties.getMinIdle());

        // Maximum waiting time for a connection from the pool
        ds.setConnectionTimeout(masterDbProperties.getConnectionTimeout());

        // Maximum time that a connection is allowed to sit idle in the pool
        ds.setIdleTimeout(masterDbProperties.getIdleTimeout());

        LOG.info("Setup of masterDataSource succeeded.");

        return ds;
    }

    /**
     * The properties for configuring the JPA provider Hibernate.
     *
     * @return
     */
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
        properties.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "update");
        return properties;
    }

    /**
     * Create the entity manager factory bean which is required to access the JPA functionalities
     * provided by the JPA persistence provider, i.e. Hibernate in this case.
     *
     * Note that @Primary annotation which tells Spring boot to create this entity manager
     * as the first thing when starting the application.
     *
     * @return
     */
    @Primary
    @Bean(name = "masterEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        // Set the master data source
        em.setDataSource(masterDataSource());

        // The master tenant entity and repository need to be scanned
        em.setPackagesToScan(new String[] { MasterTenant.class.getPackage().getName(),
                MasterTenantRepository.class.getPackage().getName() });

        // Setting the name for the persistence unit as Spring sets it as 'default' if not defined
        em.setPersistenceUnitName("masterdb-persistence-unit");

        // Setting Hibernate as the JPA provider
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        // Set the Hibernate properties
        em.setJpaProperties(hibernateProperties());

        return em;
    }

    /**
     * This transaction manager is appropriate for applications that use a
     * single JPA EntityManagerFactory for transactional data access.
     *
     * Note the @Qualifier annotation to ensure that the masterEntityManagerFactory
     * is used for setting up the transaction manager.
     *
     * @param emf
     * @return
     */
    @Bean(name = "masterTransactionManager")
    public JpaTransactionManager masterTransactionManager(@Qualifier("masterEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    /**
     * Bean post-processor that automatically applies persistence exception
     * translation to any bean marked with Spring's @Repository annotation,
     * adding a corresponding PersistenceExceptionTranslationAdvisor to the
     * exposed proxy (either an existing AOP proxy or a newly generated proxy
     * that implements all of the target's interfaces).
     *
     * @return
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
