package org.testcontainers.junit.singlestore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.Properties;
import org.testcontainers.containers.SingleStoreContainer;
import org.testcontainers.db.AbstractContainerDatabaseTest;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.testcontainers.SingleStoreTestImages.MEMSQL_IMAGE;


public class SimpleSingleStoreTest extends AbstractContainerDatabaseTest {
    SingleStoreContainer<?> memsql = new SingleStoreContainer<>(MEMSQL_IMAGE);

    @Before
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        Properties properties = mapper.readValue(new File("src/test/resources/application-test.yaml"), Properties.class);
        memsql
            .withPassword(properties.getPassword())
            .withLicenseKey(properties.getLicenseKey());
    }

    @After
    public void destroy() {
        memsql.stop();
    }

    @Test
    public void testSimple() throws SQLException {
        memsql.start();

        ResultSet resultSet = performQuery(memsql, "SELECT 1");
        int resultSetInt = resultSet.getInt(1);

        assertEquals("A basic SELECT query succeeds", 1, resultSetInt);
    }

    @Test
    public void testWithAdditionalUrlParamInJdbcUrl() {
        memsql
            .withUrlParam("connectTimeout", "40000")
            .withUrlParam("rewriteBatchedStatements", "true");

        memsql.start();
        String jdbcUrl = memsql.getJdbcUrl();
        assertThat(jdbcUrl, containsString("?"));
        assertThat(jdbcUrl, containsString("&"));
        assertThat(jdbcUrl, containsString("rewriteBatchedStatements=true"));
        assertThat(jdbcUrl, containsString("connectTimeout=40000"));
    }
}
