package belkin.dev;

import org.springframework.boot.SpringApplication;

public class TestEventManagmentApplication {

    public static void main(String[] args) {
        SpringApplication.from(EventManagmentApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
