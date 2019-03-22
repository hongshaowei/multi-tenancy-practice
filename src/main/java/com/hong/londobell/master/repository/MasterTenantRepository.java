package com.hong.londobell.master.repository;

import com.hong.londobell.master.entity.MasterTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for the {@link MasterTenant} JPA entity. Any custom methods, not
 * already defined in {@link JpaRepository}, are to be defined here
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
@Repository
public interface MasterTenantRepository extends JpaRepository<MasterTenant, Long> {

    /** Using a custom named query
     * @param tenantId
     * @return
     */
    @Query("select p from MasterTenant p where p.tenantId = :tenantId")
    MasterTenant findByTenantId(@Param("tenantId") String tenantId);
}
