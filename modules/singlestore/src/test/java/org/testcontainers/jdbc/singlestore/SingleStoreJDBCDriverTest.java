package org.testcontainers.jdbc.singlestore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.testcontainers.containers.Properties;
import org.testcontainers.jdbc.AbstractJDBCDriverTest;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

import static java.util.Arrays.asList;

@RunWith(Parameterized.class)
public class SingleStoreJDBCDriverTest extends AbstractJDBCDriverTest {

    private static Properties properties;

    static {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        properties = mapper.readValue(new File("src/test/resources/application-test.yaml"), Properties.class);
    }

    @Parameterized.Parameters(name = "{index} - {0}")
    public static Iterable<Object[]> data() {
        return asList(
            new Object[][]{
                {"jdbc:tc:memsql://hostname/cluster?user=root&password=" + properties.getPassword() + "&licenseKey=" + properties.getLicenseKey(), EnumSet.of(Options.JDBCParams)}
            });
    }
}
