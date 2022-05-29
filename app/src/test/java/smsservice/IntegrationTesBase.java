package smsservice;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class IntegrationTesBase {

    public static int rabbitPort;
    public static String rabbitHost;
    public static String rabbitUser;
    public static String rabbitPass;
    public static String rabbitQueueName;
    public static String adminPhone;

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static final DockerImageName RABBIT_IMAGE = DockerImageName.parse("rabbitmq:3-alpine");
        static final GenericContainer rabbit = new GenericContainer(RABBIT_IMAGE)
                .withExposedPorts(5672);

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext)  {
            rabbit.start();
            rabbitHost = rabbit.getHost();
            rabbitPort = rabbit.getMappedPort(5672);
            rabbitUser = rabbitPass = "guest";
            rabbitQueueName = "sms";
            adminPhone = "1111";
            TestPropertyValues
                    .of("THRESHOLD_BALANCE=5",
                            String.format("ADMIN_PHONE=%s", adminPhone),
                            String.format("QUEUE_NAME=%s", rabbitQueueName),
                            String.format("RABBIT_HOST=%s", rabbitHost),
                            String.format("RABBIT_PORT=%d", rabbitPort),
                            String.format("RABBIT_USER=%s", rabbitUser),
                            String.format("RABBIT_PASS=%s", rabbitPass),
                            "SMSRU_TOKEN=123")
                    .applyTo(applicationContext.getEnvironment());
        }
    }
}
