package ru.cbr.turing.dump.postgres;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.cbr.turing.dump.common.DumpController;
import ru.cbr.turing.dump.common.DumpService;
import ru.cbr.turing.dump.common.InfodiodService;
import ru.cbr.turing.dump.infodiod.client.InfodiodServiceClient;

@RestController
public class PgDumpController implements DumpController {

    private final Application.Config config;
    private final DumpService pgDumpService;
    private final InfodiodService pgInfodiodService;

    public PgDumpController(@Autowired Application.Config appConf,
                            @Autowired DumpService pgDumpService,
                            @Autowired InfodiodService pgInfodiodService) {
        this.config = appConf;
        this.pgDumpService = pgDumpService;
        this.pgInfodiodService = pgInfodiodService;

    }

    @Override public ServiceInfo index(){
        return new PgDumpServiceInfo();
    }

    @Override  public DumpService.DumpReport create(DumpService.DumpRequestBody dumpRequestBody){
        return pgDumpService.createDump(dumpRequestBody);
    }

    @Override public CreateAndSendToInfodiodResponse createAndSendToInfodiod(DumpService.DumpRequestBody dumpRequestBody){
        DumpService.DumpReport dumpReport = pgDumpService.createDump(dumpRequestBody);
        if (dumpReport.getStatus().equals(DumpService.DumpReport.DUMP_STATUS.OK)) {
            InfodiodService.InfodiodReport infodiodReport = pgInfodiodService.expCompleted(dumpRequestBody.getOrderNum());

            return new CreateAndSendToInfodiodResponse(dumpReport, infodiodReport);
        } else {
            return new CreateAndSendToInfodiodResponse(dumpReport, new InfodiodService.InfodiodReport(new InfodiodServiceClient.SendDumpByInfodiodResponse.Empty()));
        }
    }

    private class PgDumpServiceInfo implements ServiceInfo {

        public String getVersion() {
            return config.getVersion();
        };

        public Application.Config.Dump getDumpConfiguration() {
            return config.getDump();
        }

        public Application.Config.Infodiod getInfodiodConfiguration() {
            return config.getInfodiod();
        }
    }

}
