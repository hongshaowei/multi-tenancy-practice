package com.hong.londobell.tenant.config;

import com.hong.londobell.master.entity.MasterTenant;
import com.hong.londobell.master.repository.MasterTenantRepository;
import com.hong.londobell.tenant.entity.CustomUserDetails;
import com.hong.londobell.util.DataSourceUtil;
import com.hong.londobell.util.TenantContextHolder;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class does the job of selecting the correct database based on the tenant id found by the
 * {@link CurrentTenantIdentifierResolverImpl}
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 *
 */
@Configuration
public class DataSourceBasedMultiTenantConnectionProviderImpl
        extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceBasedMultiTenantConnectionProviderImpl.class);

    private static final long serialVersionUID = 1L;

    /**
     * Injected MasterTenantRepository to access the tenant information from the master_tenant table
     */
    @Autowired
    private MasterTenantRepository masterTenantRepo;

    /**
     * Map to store the tenant ids as key and the data source as the value
     */
    private Map<String, DataSource> dataSourceMtApp = new TreeMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        // This method is called more than once. So check if the data source map is empty.
        // If it is then rescan master_tenant table for all tenant entries.
        if (dataSourceMtApp.isEmpty()) {
            List<MasterTenant> masterTenants = masterTenantRepo.findAll();
            LOG.info(">>>>> selectAnyDataSource() -- Total tenants:" + masterTenants.size());
            for (MasterTenant masterTenant : masterTenants) {
                dataSourceMtApp.put(masterTenant.getTenantId(), DataSourceUtil.createAndConfigureDataSource(masterTenant));
            }
        }
        return this.dataSourceMtApp.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        // If the requested tenant id is not present,
        // check for it in the master database 'master_tenant' table.
        tenantIdentifier = initializeTenantIfLost(tenantIdentifier);

        if (!this.dataSourceMtApp.containsKey(tenantIdentifier)) {
            List<MasterTenant> masterTenants = masterTenantRepo.findAll();
            LOG.info(">>>>> selectDataSource() -- tenant: " + tenantIdentifier + " Total tenants: " + masterTenants.size());
            for (MasterTenant masterTenant : masterTenants) {
                dataSourceMtApp.put(masterTenant.getTenantId(), DataSourceUtil.createAndConfigureDataSource(masterTenant));
            }
        }
        return this.dataSourceMtApp.get(tenantIdentifier);
    }

    /**
     * Initialize tenantId based on logged in user if the tenant Id got lost in after form submission in a user session.
     *
     * @param tenantIdentifier
     * @return
     */
    private String initializeTenantIfLost(String tenantIdentifier) {

        if (TenantContextHolder.getTenant() == null) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            CustomUserDetails customUserDetails = null;
            if (authentication != null) {
                Object principle = authentication.getPrincipal();
                customUserDetails = principle instanceof CustomUserDetails ? (CustomUserDetails) principle : null;
            }
            TenantContextHolder.setTenantId(customUserDetails.getTenant());
        }

        if (tenantIdentifier != TenantContextHolder.getTenant()) {
            tenantIdentifier = TenantContextHolder.getTenant();
        }
        return tenantIdentifier;
    }
}
