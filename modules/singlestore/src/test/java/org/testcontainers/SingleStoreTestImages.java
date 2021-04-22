package org.testcontainers;

import org.testcontainers.utility.DockerImageName;

public interface SingleStoreTestImages {
    DockerImageName MEMSQL_IMAGE = DockerImageName.parse("memsql/cluster-in-a-box:latest");
}
