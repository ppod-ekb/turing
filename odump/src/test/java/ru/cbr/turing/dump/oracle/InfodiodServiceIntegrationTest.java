package ru.cbr.turing.dump.oracle;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import ru.cbr.turing.dump.oracle.Application;
import ru.cbr.turing.dump.oracle.InfodiodService;

@TestPropertySource(locations="/application.yml")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
public class InfodiodServiceIntegrationTest {

    @Autowired private InfodiodService infodiodService;
    @Autowired private Application.Config config;

    @Ignore
    @Test
    public void expCompletedTest() {
        System.out.println("version: " + config.getVersion());
        InfodiodService.InfodiodReport report = infodiodService.expCompleted("42");
        System.out.println("report: " + report);
    }

}
