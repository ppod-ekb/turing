package ru.cbr.turing.dump.postgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cbr.turing.dump.common.*;
import ru.cbr.turing.dump.pgdump.PgDump;

@Service
public class PgDumpService implements DumpService {

    private final Application.Config.Dump config;

    public PgDumpService(@Autowired Application.Config config) {
        this.config = config.getDump();
    }

    @Override public DumpService.DumpReport createDump(DumpService.DumpRequestBody dumpRequestBody) {
        PgDump.Config config = new PgDumpConfigFactory(dumpRequestBody).create();
        DumpCmd pgDump = new PgDump(config);
        DumpCmd.Result result = pgDump.run();

        return new DumpService.DumpReport(result, config);
    }

    private class PgDumpConfigFactory implements DumpConfigFactory<PgDump.Config> {

        private final DumpService.DumpRequestBody dumpRequestBody;

        PgDumpConfigFactory(DumpService.DumpRequestBody dumpRequestBody) {
            this.dumpRequestBody = dumpRequestBody;
        }

        public PgDump.Config create() {
            PgDump.Config.ConfigBuilder cb = new PgDump.Config.ConfigBuilder();
            cb.username(dumpRequestBody.getUsername());
            cb.port(dumpRequestBody.getPort());
            cb.password(dumpRequestBody.getPassword());
            cb.host(dumpRequestBody.getHost());
            cb.logFile(config.getDmpStorageMountPath(), dumpRequestBody.getOrderNum() + config.getLogFileExtension());
            cb.dmpFile(config.getDmpStorageMountPath(), dumpRequestBody.getOrderNum() + config.getDmpFileExtension());
            cb.format(config.getDmpFormat());
            cb.dbName(dumpRequestBody.getDbName());
            cb.cmd(config.getPgdumpCmd());

            return cb.build();
        }
    }



}


