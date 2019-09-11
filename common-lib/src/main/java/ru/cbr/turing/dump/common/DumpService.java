package ru.cbr.turing.dump.common;

public interface DumpService {

    DumpReport createDump(DumpRequestBody dumpRequestBody);

    class DumpReport {

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
        private final DumpCmd.Result dumpResult;
        private final DumpConfig dumpConfiguration;

        public DumpReport(DumpCmd.Result dumpResult, DumpConfig dumpConfiguration) {
            this.dumpResult = dumpResult;
            this.dumpConfiguration = dumpConfiguration;
            this.status = DUMP_STATUS.getStatus(dumpResult.getCmdResult().getStatus());
        }

        public DumpCmd.Result getDumpResult() {
            return dumpResult;
        }

        public DumpConfig getDumpConfiguration() {
            return dumpConfiguration;
        }

        public DUMP_STATUS getStatus() {
            return status;
        }
    }


    class DumpRequestBody {

        private String orderNum;
        private String username;
        private String password;
        private String host;
        private String port;
        private String dbName;
        private String encoding;

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }
    }
}
