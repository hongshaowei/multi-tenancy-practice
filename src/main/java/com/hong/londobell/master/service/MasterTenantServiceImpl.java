package com.hong.londobell.master.service;

import com.hong.londobell.master.entity.MasterTenant;
import com.hong.londobell.master.repository.MasterTenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link MasterTenantService} which accesses the
 * {@link MasterTenant} entity. This is the recommended way to access the
 * entities through an interface rather than using the corresponding repository.
 * This allows for separation into repository code and the service layer.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
@Service
public class MasterTenantServiceImpl implements MasterTenantService {

    @Autowired
    MasterTenantRepository masterTenantRepository;

    @Override
    public MasterTenant findByTenantId(String tenantId) {
        return masterTenantRepository.findByTenantId(tenantId);
    }
}
