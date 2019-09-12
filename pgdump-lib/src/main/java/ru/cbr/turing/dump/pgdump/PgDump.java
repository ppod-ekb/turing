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
        PgDumpCmdBuilder b = new PgDumpCmdBuilder(config.getCmd(), config.getDbName(), config.getLogFile());
        b.args().file(config.getDmpFile())
                .host(config.getHost())
                .port(config.getPort())
                .username(config.getUsername())
                .format(config.getFormat())
                .noPassword(true)
                .noRows(true)
                .verbose(true);

        b.envVars().pgPassword(config.getPassword());

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
            File f = new File(config.getLogFile());
            return f.exists() && f.isFile();
        }
    }

    private class DumpFile implements DumpCmd.Result.DumpFile {

        @Override
        public boolean isDumpFileCreated() {
            File f = new File(config.getDmpFile());
            return f.exists() && f.isFile();
        }
    }

    public static class Config extends DumpConfig {
        private final String format;

        private Config(ConfigBuilder b) {
            super(b);
            format = b.format;
        }

        public String getFormat() {
            return format;
        }

        public static class ConfigBuilder extends DumpConfig.ConfigBuilder {
            private String format;

            @Override public Config build() {
                return new Config(this);
            }

            public ConfigBuilder format(String value) {
                format = value;
                return this;
            }
        }
    }
}
