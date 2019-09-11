package ru.cbr.turing.dump.oraexp;

import ru.cbr.turing.dump.common.Cmd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpCmdBuilder {

    private enum EXP_ENV_VAR {
        LD_LIBRARY_PATH("LD_LIBRARY_PATH");

        private String name;

        EXP_ENV_VAR(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private enum EXP_ARG {
        FILE("file"),
        LOG( "log"),
        ROWS("rows");

        private String name;

        EXP_ARG(String name) {
            this.name = name;
        }

        public void printArg(StringBuilder sb, String value) {
            sb.append(name).append("=").append(value);
        }

        public String getArg(String value) {
            return name + "=" + value;
        }
    }

    private List <String> command = new ArrayList<String>();
    private StringBuilder args = new StringBuilder();
    private Map<String, String> envVars = new HashMap<String, String>();

    public ExpCmdBuilder(String cmd, String conn, String libraryPath) {
        command.add(cmd);
        envVars.put(EXP_ENV_VAR.LD_LIBRARY_PATH.getName(), libraryPath);
        this.conn(conn);
    }

    private ExpCmdBuilder conn(String value) {
        args.append(value);
        return this;
    }

    public ExpCmdBuilder file(String value) {
        appendIfNotNull(args, EXP_ARG.FILE, value);
        return this;
    }

    public ExpCmdBuilder log(String value) {
        appendIfNotNull(args, EXP_ARG.LOG, value);
        return this;
    }

    public ExpCmdBuilder rows(String value) {
        appendIfNotNull(args, EXP_ARG.ROWS, value);
        return this;
    }

    public Cmd build() {
        command.add(args.toString());
        return new Cmd(command, envVars);
    }

    private void appendIfNotNull(StringBuilder sb, EXP_ARG arg, String value) {
        if (value != null) {
            sb.append(" ").append(arg.getArg(value));
        }
    }
}
