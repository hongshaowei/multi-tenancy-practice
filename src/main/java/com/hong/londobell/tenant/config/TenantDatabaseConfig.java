package com.hong.londobell.tenant.config;

import com.hong.londobell.tenant.entity.User;
import com.hong.londobell.tenant.repository.UserRepository;
import com.hong.londobell.tenant.service.UserService;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the tenant data sources configuration which sets up the multi-tenancy.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.hong.londobell.tenant.repository", "com.hong.londobell.tenant.entity" })
@EnableJpaRepositories(basePackages = { "com.hong.londobell.tenant.repository", "com.hong.londobell.tenant.service" },
        entityManagerFactoryRef = "tenantEntityManagerFactory", transactionManagerRef = "tenantTransactionManager")
public class TenantDatabaseConfig {

    private static final Logger LOG = LoggerFactory.getLogger(TenantDatabaseConfig.class);

    @Bean(name = "tenantJpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    /**
     * The multi tenant connection provider
     *
     * @return
     */
    @Bean(name = "datasourceBasedMultiTenantConnectionProvider")
    @ConditionalOnBean(name = "masterEntityManagerFactory")
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        //Autowire the Hibernate multi connection provider
        return new DataSourceBasedMultiTenantConnectionProviderImpl();
    }

    /**
     * The current tenant identifier resolver
     *
     * @return
     */
    @Bean(name = "currentTenantIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolverImpl();
    }

    /**
     * Creates the entity manager factory bean which is required to access the
     * JPA functionalities provided by the JPA persistence provider, i.e.
     * Hibernate in this case.
     *
     * @param connectionProvider
     * @param tenantResolver
     * @return
     */
    @Bean(name = "tenantEntityManagerFactory")
    @ConditionalOnBean(name = "datasourceBasedMultiTenantConnectionProvider")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("datasourceBasedMultiTenantConnectionProvider") MultiTenantConnectionProvider connectionProvider,
            @Qualifier("currentTenantIdentifierResolver") CurrentTenantIdentifierResolver tenantResolver) {

        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();

        // All the tenant related entities, repositories and service classes must be scanned
        emfBean.setPackagesToScan(new String[] { User.class.getPackage().getName(),
                                                UserRepository.class.getPackage().getName(),
                                                UserService.class.getPackage().getName() });
        emfBean.setJpaVendorAdapter(jpaVendorAdapter());
        emfBean.setPersistenceUnitName("tenant-persistence-unit");
        Map<String, Object> properties = new HashMap<>();
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        // ImprovedNamingStrategy is deprecated and unsupported in Hibernate 5
        // properties.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        properties.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
        properties.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "update");

        emfBean.setJpaPropertyMap(properties);
        LOG.info("tenantEntityManagerFactory set up successfully");
        return emfBean;
    }

    @Bean(name = "tenantTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("tenantEntityManagerFactory") EntityManagerFactory tenantEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tenantEntityManager);
        return transactionManager;
    }
}
