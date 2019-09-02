package ru.cbr.turing.dump.oraexp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class CmdBuilderTest {

    private static final String CLIENT_PATH = "/opt/oracle/instantclient_19_3";
    private static final String CMD = CLIENT_PATH + "/exp";
    private static final String EXPECTED_RESULT = "/opt/oracle/instantclient_19_3/exp kav/kav@172.17.0.4:1521/ORCLPDB1.localdomain file=/tmp/kav.dmp log=/tmp/kav.dmp.log rows=n";

    @Test
    public void cmdBuildTest() {
        ExpCmdBuilder cmdBuilder = new ExpCmdBuilder(CMD, "kav/kav@172.17.0.4:1521/ORCLPDB1.localdomain", CLIENT_PATH);
        cmdBuilder.file("/tmp/kav.dmp");
        cmdBuilder.log("/tmp/kav.dmp.log");
        cmdBuilder.rows("n");

        Cmd cmd = cmdBuilder.build();

        assertEquals(EXPECTED_RESULT, cmd.constructedCmd());
    }
}
