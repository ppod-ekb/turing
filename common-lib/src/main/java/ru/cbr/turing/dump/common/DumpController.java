package ru.cbr.turing.dump.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.cbr.turing.dump.common.DumpController;
import ru.cbr.turing.dump.common.DumpService;


public interface DumpController {

    @RequestMapping("/")
    ServiceInfo index();

    @RequestMapping(value = "/create", method = RequestMethod.POST,
                    consumes = "application/json", produces = "application/json")
    DumpService.DumpReport create(@RequestBody DumpService.DumpRequestBody dumpRequestBody);

    @RequestMapping(value = "/createAndSendToInfodiod", method = RequestMethod.POST,
                    consumes = "application/json", produces = "application/json")
    CreateAndSendToInfodiodResponse createAndSendToInfodiod(@RequestBody DumpService.DumpRequestBody dumpRequestBody);

    interface ServiceInfo<D,I> {

        public String getVersion();

        public D getDumpConfiguration();

        public I getInfodiodConfiguration();
    }

    class CreateAndSendToInfodiodResponse {
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
}
