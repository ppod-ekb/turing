package ru.cbr.turing.dump.oracle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cbr.turing.dump.infodiod.client.InfodiodServiceClient;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DumpController {

    private final Application.Config config;
    private final DumpService dumpService;
    private final InfodiodService infodiodService;

    public DumpController(@Autowired Application.Config config,
                          @Autowired DumpService dumpService,
                          @Autowired InfodiodService infodiodService) {
        this.config = config;
        this.dumpService = dumpService;
        this.infodiodService = infodiodService;
    }

    @RequestMapping("/")
    public ServiceInfo index(){
        return new ServiceInfo(config);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public DumpService.DumpReport create(@RequestParam String orderNum,
                                         @RequestParam String connectionStr){
        return dumpService.createDump(orderNum, connectionStr);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public DumpService.DumpReport create(@RequestBody DumpService.DumpDefinition dd){
        return dumpService.createDump(dd);
    }

    @RequestMapping(value = "/sendToInfodiod", method = RequestMethod.POST, produces = "application/json")
    public InfodiodService.InfodiodReport sendToInfodiod(@RequestParam String orderNum){
         return infodiodService.expCompleted(orderNum);

    }

    @RequestMapping(value = "/createAndSendToInfodiod", method = RequestMethod.POST, produces = "application/json")
    public CreateAndSendToInfodiodResponse createAndSendToInfodiod(@RequestParam String orderNum,
                                                                   @RequestParam String connectionStr){
       return createAndSendToInfodiod(new DumpService.DumpDefinition(orderNum, connectionStr));
    }

    @RequestMapping(value = "/createAndSendToInfodiod", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public CreateAndSendToInfodiodResponse createAndSendToInfodiod(@RequestBody DumpService.DumpDefinition dd){
        DumpService.DumpReport dumpReport = dumpService.createDump(dd);
        if (dumpReport.getStatus().equals(DumpService.DumpReport.DUMP_STATUS.OK)) {
            InfodiodService.InfodiodReport infodiodReport = infodiodService.expCompleted(dd.getOrderNum());

            return new CreateAndSendToInfodiodResponse(dumpReport, infodiodReport);
        } else {
            return new CreateAndSendToInfodiodResponse(dumpReport, new InfodiodService.InfodiodReport(new InfodiodServiceClient.SendDumpByInfodiodResponse.Empty()));
        }
    }

    public static class CreateAndSendToInfodiodResponse {
        private final DumpService.DumpReport dumpReport;
        private final InfodiodService.InfodiodReport infodiodReport;

        public CreateAndSendToInfodiodResponse(DumpService.DumpReport dumpReport, InfodiodService.InfodiodReport infodiodReport) {
            this.dumpReport = dumpReport;
            this.infodiodReport = infodiodReport;
        }

        public DumpService.DumpReport getDumpReport() {
            return dumpReport;
        }

        public InfodiodService.InfodiodReport getInfodiodReport() {
            return infodiodReport;
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
    public static class ServiceInfo {

        private final String version;
        private final String oracleClientPath;
        private final String infodiodDumpCreatedUrl;

        public ServiceInfo(Application.Config config) {
            version = config.getVersion();
            oracleClientPath = config.getDump().getClientPath();
            infodiodDumpCreatedUrl = config.getInfodiod().getDumpCreatedUrl();
        }

        public String getVersion() {
            return version;
        }

        public String getOracleClientPath() {
            return oracleClientPath;
        }

        public String getInfodiodDumpCreatedUrl() {
            return infodiodDumpCreatedUrl;
        }
    }


}
