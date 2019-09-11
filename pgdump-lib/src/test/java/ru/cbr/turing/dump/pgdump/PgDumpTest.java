package ru.cbr.turing.dump.pgdump;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static ru.cbr.turing.dump.pgdump.PgDumpConst.*;

public class PgDumpTest {

    @Test
    @Disabled
    public void runTest() {
        PgDump.Config.ConfigBuilder configBuilder = new PgDump.Config.ConfigBuilder();

        configBuilder.cmd(CMD)
                      .dbName(DB_NAME)
                      .dmpFile(OUT_PATH, DMP_FILE)
                      .format(FORMAT)
                      .host(HOST)
                      .port(PORT)
                      .logFile(OUT_PATH, LOG_FILE)
                      .username(USERNAME)
                      .password(PG_PASSWORD);

        PgDump.Config config = configBuilder.build();
        PgDump pgDump = new PgDump(config);

        pgDump.run();
    }
}
