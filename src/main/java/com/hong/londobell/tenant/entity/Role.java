package com.hong.londobell.tenant.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Role entity to represent a ROLE of the {@link User} in the system.
 *
 * The JPA definitions of {@link User} and {@link Role} will cause the following
 * 3 tables to be created:
 * user
 * role
 * user_role
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "role")
    private String role;

    /**
     * Defining the Many-to-Many relation of users and roles. A Role can belong
     * to many Users and many Users can belong to a Role.
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;

    /** Getters and Setters */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
