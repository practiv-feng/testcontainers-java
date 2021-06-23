package org.testcontainers.containers;

import org.apache.commons.lang.StringUtils;
import org.testcontainers.utility.DockerImageName;

/**
 * Container implementation for the SingleStore project.
 *
 * @author Zhantao Feng
 */
public class SingleStoreContainer<SELF extends SingleStoreContainer<SELF>> extends JdbcDatabaseContainer<SELF> {
    public static final String NAME = "memsql";

    static final String DEFAULT_USER = "root";
    static final String DEFAULT_ROOT_PASSWORD = "";
    static final Integer MEMSQL_PORT = 3306;

    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("memsql/cluster-in-a-box");

    public static final String IMAGE = DEFAULT_IMAGE_NAME.getUnversionedPart();

    private String databaseName = "memsql";
    private String username = DEFAULT_USER;
    private String password = DEFAULT_ROOT_PASSWORD;
    private String licenseKey = "";

    public SingleStoreContainer(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName));
    }

    public SingleStoreContainer(final DockerImageName dockerImageName) {
        super(dockerImageName);

        dockerImageName.assertCompatibleWith(DEFAULT_IMAGE_NAME);

        addExposedPort(MEMSQL_PORT);
    }

    @Override
    protected Integer getLivenessCheckPort() {
        return getMappedPort(MEMSQL_PORT);
    }

    @Override
    protected void configure() {
        addEnv("START_AFTER_INIT", "Y"); // 'Y' means keep running after cluster init
        addEnv("MEMSQL_DATABASE", databaseName);
        addEnv("MEMSQL_USER", username);
        if (StringUtils.isNotBlank(password) || StringUtils.isNotBlank(licenseKey)) {
            addEnv("ROOT_PASSWORD", password);
            addEnv("LICENSE_KEY", licenseKey);
        } else {
            throw new ContainerLaunchException("The password and license key must not be blank");
        }
        setStartupAttempts(3);
    }

    @Override
    public String getDriverClassName() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    public String getJdbcUrl() {
        String additionalUrlParams = constructUrlParameters("?", "&");
        return "jdbc:mariadb://" + getHost() + ":" + getMappedPort(MEMSQL_PORT) +
            "/" + databaseName + additionalUrlParams;
    }

    @Override
    public String getDatabaseName() {
    	return databaseName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    @Override
    public String getTestQueryString() {
        return "SELECT 1";
    }

    @Override
    public SELF withDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
        return self();
    }

    @Override
    public SELF withUsername(final String username) {
        this.username = username;
        return self();
    }

    @Override
    public SELF withPassword(final String password) {
        this.password = password;
        return self();
    }

    @Override
    public SELF withLicenseKey(final String licenseKey) {
        this.licenseKey = licenseKey;
        return self();
    }
}
