package ru.cbr.turing.dump.oracle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cbr.turing.dump.infodiod.client.InfodiodServiceClient;

@Service
public class InfodiodService {

    private final Application.Config.Infodiod config;
    private final InfodiodServiceClient client;

    public InfodiodService(@Autowired Application.Config config) {
        this.config = config.getInfodiod();
        this.client = new InfodiodServiceClient(this.config.getServiceHost());
    }

    public InfodiodReport expCompleted(String orderNum) {
        InfodiodServiceClient.SendDumpByInfodiodResponse response = client.dumpCreated().post(orderNum);
        //ExpCompletedResponse response = restTemplate.postForObject(config.getDumpCreatedUrl(), new DumpCreatedRequestBody(orderNum), ExpCompletedResponse.class);
        return new InfodiodReport(response);
    }

    public static class InfodiodReport {
        private final InfodiodServiceClient.SendDumpByInfodiodResponse response;

        public InfodiodReport(InfodiodServiceClient.SendDumpByInfodiodResponse response) {
            this.response = response;
        }

        public InfodiodServiceClient.SendDumpByInfodiodResponse getResponse() {
            return response;
        }
    }
}
