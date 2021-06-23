package org.testcontainers.containers;

import io.r2dbc.spi.ConnectionFactoryOptions;
import org.testcontainers.r2dbc.AbstractR2DBCDatabaseContainerTest;
import org.testcontainers.utility.DockerImageName;

public class SingleStoreR2DBCDatabaseContainerTest extends AbstractR2DBCDatabaseContainerTest<SingleStoreContainer<?>> {

    @Override
    protected ConnectionFactoryOptions getOptions(SingleStoreContainer<?> container) {
        return SingleStoreR2DBCDatabaseContainer.getOptions(container);
    }

    @Override
    protected String createR2DBCUrl() {
        return "r2dbc:tc:mariadb:///cluster";
    }

    @Override
    protected SingleStoreContainer<?> createContainer() {
        return new SingleStoreContainer<>(DockerImageName.parse("memsql/cluster-in-a-box"));
    }

}
