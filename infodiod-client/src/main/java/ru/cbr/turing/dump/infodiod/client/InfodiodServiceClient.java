package ru.cbr.turing.dump.infodiod.client;

import org.springframework.web.client.RestTemplate;

import java.util.List;

public class InfodiodServiceClient {

    private final String url;
    private final RestTemplate restTemplate;

    public InfodiodServiceClient(String url) {
        this.url = url;
        this.restTemplate = new RestTemplate();
    }

    public SendDumpByInfodiodReport dumpCreated(String orderNum) {
        DumpCreatedResponse response = restTemplate.postForObject(url, new Order(orderNum), DumpCreatedResponse.class);
        return response;
    }

    public SendDumpByInfodiodReport dumpCreated(Order order) {
        DumpCreatedResponse response = restTemplate.postForObject(url,order,DumpCreatedResponse.class);
        return response;
    }

    public interface SendDumpByInfodiodReport {
        String getMessage();
        List<String> getCreatedFileNames();
    }

    public static class Order {
        private String orderNum;

        public Order() {

        }

        public Order(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }
    }

    public static class DumpCreatedResponse implements SendDumpByInfodiodReport {
        private String message;
        private List<String> createdFileNames;

        public DumpCreatedResponse() {

        }

        public DumpCreatedResponse(String message, List<String> createdFileNames) {
            this.message = message;
            this.createdFileNames = createdFileNames;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<String> getCreatedFileNames() {
            return createdFileNames;
        }

        public void setCreatedFileNames(List<String> createdFileNames) {
            this.createdFileNames = createdFileNames;
        }
    }

}
