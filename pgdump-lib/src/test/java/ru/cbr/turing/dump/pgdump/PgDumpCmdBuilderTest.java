package ru.cbr.turing.dump.pgdump;

import org.junit.jupiter.api.Test;
import ru.cbr.turing.dump.common.Cmd;

import static ru.cbr.turing.dump.pgdump.PgDumpConst.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PgDumpCmdBuilderTest {
    // 2>&1 | tee /tmp/42.log
    private static final String CMD_EXPECTED_RESULT = "/usr/bin/pg_dump --host=172.17.0.3 --port=5432 --format=c --username=kav --file=/tmp/42.dmp --no-password --schema-only --verbose kav";
    private static final String ENV_EXPECTED_RESULT = "PGPASSWORD=kav;";

    @Test
    public void buildCmdTest() {
        PgDumpCmdBuilder b = new PgDumpCmdBuilder(CMD, DB_NAME, LOG_TO_FILE);

        b.args().host(HOST)
                .port(PORT)
                .format(FORMAT)
                .username(USERNAME)
                .file(FILE)
                .noPassword(NO_PASSWORD)
                .noRows(NO_ROWS)
                .verbose(VERBOSE);

        b.envVars().pgPassword(PG_PASSWORD);

        Cmd cmd = b.build();

        assertEquals(CMD_EXPECTED_RESULT, cmd.constructedCmd());
        assertEquals(true, cmd.envVars().contains(ENV_EXPECTED_RESULT));
    }
}
