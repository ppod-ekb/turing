package ru.cbr.turing.dump.pgdump;

import ru.cbr.turing.dump.common.Cmd;
import ru.cbr.turing.dump.common.DumpCmd;
import ru.cbr.turing.dump.common.DumpConfig;

import java.io.File;

public class PgDump implements DumpCmd {

    private final Config config;

    public PgDump(Config config) {
       this.config = config;
    }

    public DumpCmd.Result run() {
        Cmd cmd = buildCmd();
        Cmd.Result cmdResult = cmd.run();
        return new Result(cmdResult);
    }

    private Cmd buildCmd() {
        PgDumpCmdBuilder b = new PgDumpCmdBuilder(config.cmd, config.dbName, config.logFile);
        b.args().file(config.dmpFile)
                .host(config.host)
                .port(config.port)
                .username(config.username)
                .format(config.format)
                .noPassword(true)
                .noRows(true)
                .verbose(true);

        b.envVars().pgPassword(config.password);

        return b.build();
    }

    private class Result implements DumpCmd.Result {

        private final Cmd.Result cmdResut;
        private final DumpCmd.Result.DumpFile dumpFile = new PgDump.DumpFile();
        private final DumpCmd.Result.LogFile logFile = new PgDump.LogFile();


        public Result(Cmd.Result cmdResut) {
            this.cmdResut = cmdResut;
        }

        @Override
        public Cmd.Result getCmdResult() {
            return cmdResut;
        }

        @Override
        public DumpFile getDumpFile() {
            return dumpFile;
        }

        @Override
        public LogFile getLogFile() {
            return logFile;
        }

        @Override
        public boolean isLogAndDumpFileCreated() {
            return dumpFile.isDumpFileCreated() && logFile.isLogFileCreated();
        }
    }

    private class LogFile implements DumpCmd.Result.LogFile {

        @Override
        public boolean isLogFileCreated() {
            File f = new File(config.logFile);
            return f.exists() && f.isFile();
        }
    }

    private class DumpFile implements DumpCmd.Result.DumpFile {

        @Override
        public boolean isDumpFileCreated() {
            File f = new File(config.dmpFile);
            return f.exists() && f.isFile();
        }
    }

    public static class Config implements DumpConfig {
        private final String cmd;
        private final String dmpFile;
        private final String logFile;
        private final String host;
        private final String port;
        private final String dbName;
        private final String username;
        private final String password;
        private final String format;

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

        public String getFormat() {
            return format;
        }

        private Config(ConfigBuilder b) {
            cmd = b.cmd;
            dmpFile = b.dmpFile;
            logFile = b.logFile;
            host = b.host;
            port = b.port;
            dbName = b.dbName;
            username = b.username;
            password = b.password;
            format = b.format;
        }

        public static class ConfigBuilder {

            private String cmd;
            private String host;
            private String port;
            private String dbName;
            private String username;
            private String password;
            private String dmpFile;
            private String logFile;
            private String format;

            public Config build() {
                return new Config(this);
            }

            public ConfigBuilder format(String value) {
                format = value;
                return this;
            }

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
}
