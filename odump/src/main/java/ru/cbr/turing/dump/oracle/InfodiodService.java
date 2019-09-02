package ru.cbr.turing.dump.oracle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.cbr.turing.dump.infodiod.client.InfodiodServiceClient;

import java.util.List;

@Service
public class InfodiodService {

    private final Application.Config.Infodiod config;
    private final InfodiodServiceClient client;
    //private final RestTemplate restTemplate;

    public InfodiodService(@Autowired Application.Config config) {
        this.config = config.getInfodiod();
        this.client = new InfodiodServiceClient(this.config.getDumpCreatedUrl());
    }

    public InfodiodReport expCompleted(String orderNum) {
        InfodiodServiceClient.SendDumpByInfodiodReport response = client.dumpCreated(orderNum);
        //ExpCompletedResponse response = restTemplate.postForObject(config.getDumpCreatedUrl(), new DumpCreatedRequestBody(orderNum), ExpCompletedResponse.class);
        return new InfodiodReport(response);
    }

    public static class InfodiodReport {
        private final InfodiodServiceClient.SendDumpByInfodiodReport response;

        public InfodiodReport(InfodiodServiceClient.SendDumpByInfodiodReport response) {
            this.response = response;
        }

        public InfodiodServiceClient.SendDumpByInfodiodReport getResponse() {
            return response;
        }
    }

   /* public interface SendToInfodiodResponse {
        String getMessage();
        List<String> getCreatedFileNames();
    }

    public static class ExpCompletedResponse implements SendToInfodiodResponse {
        private String message;
        private List<String> createdFileNames;

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public List<String> getCreatedFileNames() {
            return createdFileNames;
        }

        public void setCreatedFileNames(List<String> createdFileNames) {
            this.createdFileNames = createdFileNames;
        }
    }

    public interface  InfodiodDumpCreatedRequest {
        String getOrderNum();
    }

    private class DumpCreatedRequestBody {

        public DumpCreatedRequestBody(String orderNum) {
            this.orderNum = orderNum;
        }

        private final String orderNum;

        public String getOrderNum() {
            return orderNum;
        }
    }*/
}
