package com.hong.londobell.master.entity;

import javax.persistence.*;

/**
 * This JPA entity represents the master_tenant table in the masterdb.
 * This table holds the details of the tenant database.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
@Entity
@Table(name = "master_tenant")
public class MasterTenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "username")
    private String username;

    /**
     * For simplicity, not storing an encrypted password. In production this should be a encrypted password.
     */
    @Column(name = "password")
    private String password;

    @Column(name = "url")
    private String url;

    /**
     * Specifies the version field or property of an entity class that serves as
     * its optimistic lock value. The version is used to ensure integrity when
     * performing the merge operation and for optimistic concurrency control.
     */
    @Version
    private int version = 0;

    /** Getters and Setters */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
