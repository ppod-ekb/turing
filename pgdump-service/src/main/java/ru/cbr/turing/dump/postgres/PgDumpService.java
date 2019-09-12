package ru.cbr.turing.dump.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cbr.turing.dump.common.*;
import ru.cbr.turing.dump.pgdump.PgDump;

@Service
public class PgDumpService implements DumpService {

    private final Application.Config.Dump applicationConfig;

    public PgDumpService(@Autowired Application.Config applicationConfig) {
        this.applicationConfig = applicationConfig.getDump();
    }

    @Override public DumpService.DumpReport createDump(DumpService.DumpRequestBody dumpRequestBody) {
        PgDump.Config config = new PgDumpConfigBuilder(dumpRequestBody).build();
        DumpCmd pgDump = new PgDump(config);
        DumpCmd.Result result = pgDump.run();

        return new DumpService.DumpReport(result, config);

    }

    private class PgDumpConfigBuilder {

        private final DumpService.DumpRequestBody dumpRequestBody;

        PgDumpConfigBuilder(DumpService.DumpRequestBody dumpRequestBody) {
            this.dumpRequestBody = dumpRequestBody;
        }

        public PgDump.Config build() {
            PgDump.Config.ConfigBuilder cb = new PgDump.Config.ConfigBuilder();
            cb.username(dumpRequestBody.getUsername());
            cb.port(dumpRequestBody.getPort());
            cb.password(dumpRequestBody.getPassword());
            cb.host(dumpRequestBody.getHost());
            cb.logFile(applicationConfig.getDmpStorageMountPath(), dumpRequestBody.getOrderNum() + applicationConfig.getLogFileExtension());
            cb.dmpFile(applicationConfig.getDmpStorageMountPath(), dumpRequestBody.getOrderNum() + applicationConfig.getDmpFileExtension());
            cb.format(applicationConfig.getDmpFormat());
            cb.dbName(dumpRequestBody.getDbName());
            cb.cmd(applicationConfig.getPgdumpCmd());

            return cb.build();
        }
    }



}


