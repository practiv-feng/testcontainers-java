package org.testcontainers.containers;

import org.testcontainers.jdbc.ConnectionUrl;
import org.testcontainers.utility.DockerImageName;

/**
 * Factory for SingleStore org.testcontainers.containers.
 */
public class SingleStoreContainerProvider extends JdbcDatabaseContainerProvider {

    private static final String USER_PARAM = "user";

    private static final String PASSWORD_PARAM = "password";

    private static final String LICENSE_KEY = "licenseKey";

    @Override
    public boolean supports(String databaseType) {
        return databaseType.equals(SingleStoreContainer.NAME);
    }

    @Override
    public JdbcDatabaseContainer newInstance(String tag) {
        return new SingleStoreContainer(DockerImageName.parse(SingleStoreContainer.IMAGE).withTag(tag));
    }

    @Override
    public JdbcDatabaseContainer newInstance(ConnectionUrl connectionUrl) {
       return newInstanceFromConnectionUrl(connectionUrl, USER_PARAM, PASSWORD_PARAM, LICENSE_KEY);
    }

}
