package ru.cbr.turing.dump.common;

public class DumpConfig  {
    private final String cmd;
    private final String dmpFile;
    private final String logFile;
    private final String host;
    private final String port;
    private final String dbName;
    private final String username;
    private final String password;
    private final String encoding;

    public String getCmd() {
        return cmd;
    }

    public String getDmpFile() {
        return dmpFile;
    }

    public String getLogFile() {
        return logFile;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEncoding() {
        return encoding;
    }

    protected DumpConfig(ConfigBuilder b) {
        cmd = b.cmd;
        dmpFile = b.dmpFile;
        logFile = b.logFile;
        host = b.host;
        port = b.port;
        dbName = b.dbName;
        username = b.username;
        password = b.password;
        encoding = b.encoding;
    }

    public static abstract class ConfigBuilder {

        private String cmd;
        private String host;
        private String port;
        private String dbName;
        private String username;
        private String password;
        private String dmpFile;
        private String logFile;
        private String encoding;

        public abstract DumpConfig build();

        public ConfigBuilder cmd(String value) {
            cmd = value;
            return this;
        }

        public ConfigBuilder host(String value) {
            host = value;
            return this;
        }

        public ConfigBuilder port(String value) {
            port = value;
            return this;
        }

        public ConfigBuilder dbName(String value) {
            dbName = value;
            return this;
        }

        public ConfigBuilder username(String value) {
            username = value;
            return this;
        }

        public ConfigBuilder password(String value) {
            password = value;
            return this;
        }

        public ConfigBuilder encoding(String value) {
            encoding = value;
            return this;
        }

        public ConfigBuilder dmpFile(String value) {
            dmpFile = value;
            return this;
        }

        public ConfigBuilder logFile(String value) {
            logFile = value;
            return this;
        }

        public ConfigBuilder dmpFile(String path, String fileName) {
            dmpFile = fullPath(path, fileName);
            return this;
        }

        public ConfigBuilder logFile(String path, String fileName) {
            logFile = fullPath(path, fileName);
            return this;
        }

        private String fullPath(String path, String fileName) {
            StringBuilder sb = new StringBuilder();
            sb.append(path).append("/").append(fileName);
            return sb.toString();
        }
    }
}
