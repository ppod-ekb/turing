package ru.cbr.turing.dump.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cbr.turing.dump.common.InfodiodService;
import ru.cbr.turing.dump.infodiod.client.InfodiodServiceClient;

@Service
public class PgDumpInfodiodService implements InfodiodService {

    private final Application.Config.Infodiod config;
    private final InfodiodServiceClient client;

    public PgDumpInfodiodService(@Autowired Application.Config config) {
        this.config = config.getInfodiod();
        this.client = new InfodiodServiceClient(this.config.getServiceHost());
    }

    @Override
    public InfodiodReport expCompleted(String orderNum) {
        try {
            InfodiodServiceClient.SendDumpByInfodiodResponse response = client.dumpCreated().post(orderNum);
            return new InfodiodReport(response);
        }catch (Exception e) {
            return new InfodiodReport(InfodiodReport.INFODIOD_STATUS.ERROR,
                                      new InfodiodServiceClient.SendDumpByInfodiodResponse.ErrorSendDumpResponse(e));
        }
    }
}
