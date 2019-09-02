package ru.cbr.turing.dump.oraexp;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpTest {

    private static final String CLIENT_PATH = "/opt/oracle/instantclient_19_3";
    private static final String EXP_CMD = CLIENT_PATH + "/exp";

    private Config getConfig() {
        Config.ConfigBuilder c = new Config.ConfigBuilder(EXP_CMD, CLIENT_PATH);
        c.conn("kav/kav@172.17.0.4:1521/ORCLPDB1.localdomain");
        c.file("/tmp/kav.dmp");
        c.log("/tmp/kav.dmp.log");
        c.rows("n");
        return c.build();
    }

    private void printResult(Cmd.Result r) {
        System.out.println(r.toString());
    }

    @Test
    @Disabled
    public void expTest() {
        Exp exp = new Exp(getConfig());
        Cmd.Result result = exp.run();

        printResult(result);

        assertEquals(result.getStatus().getCode(), 0);
    }
}
