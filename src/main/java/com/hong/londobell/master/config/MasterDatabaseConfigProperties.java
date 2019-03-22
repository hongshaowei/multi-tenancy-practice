package com.hong.londobell.master.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Master database configuration properties which are read from the application.yml file.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
//@Configuration
@ConfigurationProperties(prefix = "multitenancy.mtapp.master.datasource")
public class MasterDatabaseConfigProperties {

    /** database url */
    private String url;

    /** database username */
    private String username;

    /** database password */
    private String password;

    /** database driver */
    private String driverClassName;

    // Following are for setting the Hikari Connection Pool properties. Spring
    // Boot uses Hikari CP by default.

    /**
     * Maximum number of milliseconds that a client will wait for a connection
     * from the pool. If this time is exceeded without a connection becoming
     * available, a SQLException will be thrown from
     * javax.sql.DataSource.getConnection().
     */
    private long connectionTimeout;

    /**
     * The property controls the maximum size that the pool is allowed to reach,
     * including both idle and in-use connections. Basically this value will
     * determine the maximum number of actual connections to the database
     * backend.
     *
     * When the pool reaches this size, and no idle connections are available,
     * calls to getConnection() will block for up to connectionTimeout
     * milliseconds before timing out.
     */
    private int maxPoolSize;

    /**
     * This property controls the maximum amount of time (in milliseconds) that
     * a connection is allowed to sit idle in the pool. Whether a connection is
     * retired as idle or not is subject to a maximum variation of +30 seconds,
     * and average variation of +15 seconds. A connection will never be retired
     * as idle before this timeout. A value of 0 means that idle connections are
     * never removed from the pool.
     */
    private long idleTimeout;

    /**
     * The property controls the minimum number of idle connections that
     * HikariCP tries to maintain in the pool, including both idle and in-use
     * connections. If the idle connections dip below this value, HikariCP will
     * make a best effort to restore them quickly and efficiently.
     */
    private int minIdle;

    /**
     * The name for the master database connection pool
     */
    private String poolName;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MasterDatabaseConfigProperties [url=");
        builder.append(url);
        builder.append(", username=");
        builder.append(username);
        builder.append(", password=");
        builder.append(password);
        builder.append(", driverClassName=");
        builder.append(driverClassName);
        builder.append(", connectionTimeout=");
        builder.append(connectionTimeout);
        builder.append(", maxPoolSize=");
        builder.append(maxPoolSize);
        builder.append(", idleTimeout=");
        builder.append(idleTimeout);
        builder.append(", minIdle=");
        builder.append(minIdle);
        builder.append(", poolName=");
        builder.append(poolName);
        builder.append("]");
        return builder.toString();
    }

    /** Getters and Setters */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }
}
