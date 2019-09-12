package ru.cbr.turing.dump.pgdump;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static ru.cbr.turing.dump.pgdump.PgDumpConst.*;

import static ru.cbr.turing.dump.pgdump.PgDump.Config;
import static ru.cbr.turing.dump.pgdump.PgDump.Config.ConfigBuilder;

public class PgDumpTest {

    @Test
    @Disabled
    public void runTest() {
        ConfigBuilder configBuilder = new ConfigBuilder();

        configBuilder.format(FORMAT)
                      .cmd(CMD)
                      .dbName(DB_NAME)
                      .dmpFile(OUT_PATH, DMP_FILE)
                      .host(HOST)
                      .port(PORT)
                      .logFile(OUT_PATH, LOG_FILE)
                      .username(USERNAME)
                      .password(PG_PASSWORD);

        Config config = configBuilder.build();
        PgDump pgDump = new PgDump(config);

        pgDump.run();
    }
}
