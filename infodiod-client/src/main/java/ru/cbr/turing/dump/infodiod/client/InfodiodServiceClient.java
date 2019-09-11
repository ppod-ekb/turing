package ru.cbr.turing.dump.infodiod.client;

import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class InfodiodServiceClient {

    private final String serviceHost;
    private final RestTemplate restTemplate;
    private final DumpCreated dumpCreated;

    public InfodiodServiceClient(String serviceHost) {
        this.serviceHost = serviceHost;
        this.restTemplate = new RestTemplate();
        this.dumpCreated = new DumpCreated();
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

    public DumpCreated dumpCreated() {
        return dumpCreated;
    }

    public interface SendDumpByInfodiodResponse {
        String getMessage();
        List<String> getCreatedFileNames();

        class Empty implements SendDumpByInfodiodResponse {

            private String message = "";

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public List<String> getCreatedFileNames() {
                return Collections.emptyList();
            }
        }

        class ErrorSendDumpResponse  implements SendDumpByInfodiodResponse {
            private final String message;
            public ErrorSendDumpResponse(Exception e) {
                this.message = e.toString();
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public List<String> getCreatedFileNames() {
                return Collections.emptyList();
            }
        }

        class DumpSendedResponse implements SendDumpByInfodiodResponse {
            private String message;
            private List<String> createdFileNames;

            public DumpSendedResponse() {

            }

            public DumpSendedResponse(String message, List<String> createdFileNames) {
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

    public class DumpCreated {

        private final String url = "http://" + serviceHost + "/dumpCreated";

        public SendDumpByInfodiodResponse post(String orderNum) {
            SendDumpByInfodiodResponse response = restTemplate.postForObject(url, new Order(orderNum), SendDumpByInfodiodResponse.DumpSendedResponse.class);
            return response;
        }

        public SendDumpByInfodiodResponse post(Order order) {
            SendDumpByInfodiodResponse response = restTemplate.postForObject(url, order, SendDumpByInfodiodResponse.DumpSendedResponse.class);
            return response;
        }

    }
}
