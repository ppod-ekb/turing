package ru.cbr.turing.dump.oracle;

import ru.cbr.turing.dump.common.Cmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cbr.turing.dump.oraexp.Config;
import ru.cbr.turing.dump.oraexp.Exp;

@Service
public class DumpService {

    private final Application.Config.Dump config;

    public DumpService(@Autowired Application.Config config) {
        this.config = config.getDump();
    }

    public DumpReport createDump(String orderNum, String connectionStr) {
       return createDump(new DumpDefinition(orderNum, connectionStr));
    }

    public DumpReport createDump(DumpDefinition dd) {
        Config expConfig = new ExpConfig(dd.getOrderNum(), dd.getConnectionStr()).config();
        Exp exp = new Exp(expConfig);
        Cmd.Result result = exp.run();

        return new DumpReport(result, expConfig);
    }

    public static class DumpDefinition {

        private String orderNum;
        private String connectionStr;

        public DumpDefinition() {

        }

        public DumpDefinition(String orderNum, String connectionStr) {
            this.orderNum = orderNum;
            this.connectionStr = connectionStr;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getConnectionStr() {
            return connectionStr;
        }

        public void setConnectionStr(String connectionStr) {
            this.connectionStr = connectionStr;
        }
    }


    private class ExpConfig {
        private final String orderNum;
        private final String connectionStr;

        public ExpConfig(String orderNum, String connectionStr) {
            this.orderNum = orderNum;
            this.connectionStr = connectionStr;
        }

        public Config config() {
            Config.ConfigBuilder c = new Config.ConfigBuilder(config.getExpCmd(), config.getClientPath());
            c.conn(connectionStr);
            c.file(config.getOutputPath(), orderNum + config.getDmpFileExtension());
            c.log(config.getOutputPath(), orderNum + config.getLogFileExtension());
            c.rows("n");
            return c.build();
        }
    }

    public static class DumpReport {

        public enum DUMP_STATUS {

            OK("OK"),
            ERROR("ERROR");

            private String code;
            DUMP_STATUS(String code) {
                this.code = code;
            }

            public String getCode() {
                return code;
            }

            public static DUMP_STATUS getStatus(Cmd.CMD_STATUS cmdStatus) {
                if (cmdStatus.equals(Cmd.CMD_STATUS.OK)) {
                    return OK;
                } else {
                    return ERROR;
                }
            }
        }

        private final DUMP_STATUS status;
        private final Cmd.Result expResult;
        private final Config expConfiguration;

        public DumpReport(Cmd.Result expResult, Config expConfiguration) {
            this.expResult = expResult;
            this.expConfiguration = expConfiguration;
            this.status = DUMP_STATUS.getStatus(expResult.getStatus());
        }

        public Cmd.Result getExpResult() {
            return expResult;
        }

        public Config getExpConfiguration() {
            return expConfiguration;
        }

        public DUMP_STATUS getStatus() {
            return status;
        }
    }

}
