package ru.cbr.turing.dump.common;

import ru.cbr.turing.dump.infodiod.client.InfodiodServiceClient;

public interface InfodiodService {

    InfodiodReport expCompleted(String orderNum);

    class InfodiodReport {

        public enum INFODIOD_STATUS {
            OK,
            ERROR
        }

        private final InfodiodServiceClient.SendDumpByInfodiodResponse response;
        private final INFODIOD_STATUS status;

        public InfodiodReport(InfodiodServiceClient.SendDumpByInfodiodResponse response) {
            this(INFODIOD_STATUS.OK, response);
        }

        public InfodiodReport(INFODIOD_STATUS status, InfodiodServiceClient.SendDumpByInfodiodResponse response) {
            this.response = response;
            this.status = status;
        }

        public InfodiodServiceClient.SendDumpByInfodiodResponse getResponse() {
            return response;
        }
    }

}
