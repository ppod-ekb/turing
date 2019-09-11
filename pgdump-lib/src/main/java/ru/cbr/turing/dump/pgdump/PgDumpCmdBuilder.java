package ru.cbr.turing.dump.pgdump;

import ru.cbr.turing.dump.common.ArgBuilder;
import ru.cbr.turing.dump.common.Cmd;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PgDumpCmdBuilder {

    private enum PG_DUMP_CMD_ARG {
        HOST("--host"),
        PORT("--port"),
        FILE("--file"),
        FORMAT("--format"),
        USERNAME("--username"),
        NO_PASSWORD("--no-password"),
        SCHEMA_ONLY("--schema-only"),
        VERBOSE("--verbose");

        private final String name;

        PG_DUMP_CMD_ARG(String name) {
            this.name = name;
        }

        public String getArgStr(String value) {
            if (value != null && !value.isEmpty()) {
                return name + "=" + value;
            } else {
                return "";
            }
        }

        public String getArgStr(boolean getIfTrue) {
            if (getIfTrue) return name;
            else return "";
        }
    }


    private enum PG_DUMP_ENV_VAR {
        PG_PASSWORD("PGPASSWORD");

        private String name;

        PG_DUMP_ENV_VAR(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
/*
usage PG_DUMP with log console output:
$ /usr/bin/pg_dump --host=172.17.0.3 --port=5432 --format=c --username=kav --file=/tmp/42.dmp --no-password --verbose kav 2>&1 | tee /tmp/42.log
*/


    private final static String LOG_TO_FILE_PREFIX = "2>&1 | tee";

    private final EnvVarContainer envVarContainer = new EnvVarContainer();
    private final ArgContainer argContainer = new ArgContainer();
    private final List<String> command = new ArrayList<String>();

    private final String dbName;
    private final String logFile;
    private final String cmd;

    public PgDumpCmdBuilder(String cmd, String dbName, String logFile) {
        this.cmd = cmd;
        this.dbName = dbName;
        this.logFile = logFile;
    }

    private String logToFileArg() {
        ArgBuilder<String> sb = new ArgBuilder.ArgStringBuilder(new StringBuilder());
        sb.add(LOG_TO_FILE_PREFIX);
        sb.add(logFile);
        return sb.args();
    }

    private List<String> commandList() {
        command.add(cmd);
        command.addAll(argContainer.argBuilder.args());
        command.add(dbName);
        //command.add(logToFileArg());
        return command;
    }

    private Map<String, String> envVarsMap() {
        return envVarContainer.vars;
    }

    public Cmd build() {
        File logToFile = new LogFile(logFile).file();

        return new Cmd(commandList(), envVarsMap(), logToFile);
    }

    public ArgContainer args() {
        return argContainer;
    }

    public EnvVarContainer envVars() {
        return envVarContainer;
    }

    private class LogFile {

        private final String fileFullPath;

        public LogFile(String fileFullPath) {
            this.fileFullPath = fileFullPath;
        }

        public File file() {
            File f = new File(fileFullPath);
           createFileIfItDoesNotExist(f);
           return f;
        }

        private void createFileIfItDoesNotExist(File f) {
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException("can't create log file cause: " + e, e);
                }
            }
        }
    }

    public class ArgContainer {

        private final ArgBuilder<List<String>> argBuilder = new ArgBuilder.ArgListBuilder(new LinkedList<String>());

        public ArgContainer port(String value) {
            argBuilder.addIfNotNullAndNotEmpty(PG_DUMP_CMD_ARG.PORT.getArgStr(value.trim()));
            return this;
        }

        public ArgContainer host(String value) {
            argBuilder.addIfNotNullAndNotEmpty(PG_DUMP_CMD_ARG.HOST.getArgStr(value.trim()));
            return this;
        }

        public ArgContainer file(String value) {
            argBuilder.addIfNotNullAndNotEmpty(PG_DUMP_CMD_ARG.FILE.getArgStr(value.trim()));
            return this;
        }

        public ArgContainer format(String value) {
            argBuilder.addIfNotNullAndNotEmpty(PG_DUMP_CMD_ARG.FORMAT.getArgStr(value.trim()));
            return this;
        }

        public ArgContainer username(String value) {
            argBuilder.addIfNotNullAndNotEmpty(PG_DUMP_CMD_ARG.USERNAME.getArgStr(value.trim()));
            return this;
        }

        public ArgContainer noPassword(boolean noPasswordEnabled) {
            argBuilder.addIfNotNullAndNotEmpty(PG_DUMP_CMD_ARG.NO_PASSWORD.getArgStr(noPasswordEnabled));
            return this;
        }

        public ArgContainer noRows(boolean noRowsEnabled) {
            argBuilder.addIfNotNullAndNotEmpty(PG_DUMP_CMD_ARG.SCHEMA_ONLY.getArgStr(noRowsEnabled));
            return this;
        }

        public ArgContainer verbose(boolean verboseEnabled) {
            argBuilder.addIfNotNullAndNotEmpty(PG_DUMP_CMD_ARG.VERBOSE.getArgStr(verboseEnabled));
            return this;
        }
    }

    public class EnvVarContainer {
        private final Map<String, String> vars = new HashMap<String, String>();

        public EnvVarContainer pgPassword(String value) {
            putIfNotNullAndNotEmpty(value.trim());
            return this;
        }

        private void putIfNotNullAndNotEmpty(String value) {
            if (value != null && !value.isEmpty()) {
                vars.put(PG_DUMP_ENV_VAR.PG_PASSWORD.getName(), value);
            }
        }
    }


}
