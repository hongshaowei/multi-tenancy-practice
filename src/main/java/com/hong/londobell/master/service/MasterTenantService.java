package com.hong.londobell.master.service;

import com.hong.londobell.master.entity.MasterTenant;
import org.springframework.data.repository.query.Param;

/**
 * Service definition which accesses the {@link MasterTenant} entity.
 * This is the recommended way to access the entities through an interface
 * rather than using the corresponding repository.
 * This allows for separation into repository code and the service layer.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
public interface MasterTenantService {

    MasterTenant findByTenantId(@Param("tenantId") String tenantId);
}
