package com.hong.londobell.util;

import com.hong.londobell.master.entity.MasterTenant;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Utility class for DataSource
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 *
 */
public class DataSourceUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceUtil.class);

    /**
     * Utility method to create and configure a data source.
     *
     * @param masterTenant
     * @return
     */
    public static DataSource createAndConfigureDataSource(MasterTenant masterTenant) {
        HikariDataSource ds = new HikariDataSource();
        ds.setUsername(masterTenant.getUsername());
        ds.setPassword(masterTenant.getPassword());
        ds.setJdbcUrl(masterTenant.getUrl());
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // HikariCP settings - could come from the master_tenant table but
        // hardcoded here for brevity
        // Maximum waiting time for a connection from the pool
        ds.setConnectionTimeout(20000);

        // Minimum number of idle connections in the pool
        ds.setMinimumIdle(10);

        // Maximum number of actual connection in the pool
        ds.setMaximumPoolSize(20);

        // Maximum time that a connection is allowed to sit idle in the pool
        ds.setIdleTimeout(300000);
        ds.setConnectionTimeout(20000);

        // Setting up a pool name for each tenant datasource
        String tenantId = masterTenant.getTenantId();
        String tenantConnectionPoolName = tenantId + "-connection-pool";
        ds.setPoolName(tenantConnectionPoolName);
        LOG.info("Configured datasource:" + masterTenant.getTenantId() + ". Connection poolname:" + tenantConnectionPoolName);
        return ds;
    }
}
