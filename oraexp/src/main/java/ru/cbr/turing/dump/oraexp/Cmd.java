package ru.cbr.turing.dump.oraexp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Cmd {

    public enum CMD_STATUS {
        OK(0),
        INTERNAL_ERROR(-10000),
        CMD_NOT_FOUND(127),
        CMD_CANCELED(-10001),
        UNKNOWN;

        private int code;

        CMD_STATUS(int code) {
            this.code = code;
        }

        CMD_STATUS(){
        }

        public int getCode() {
            return code;
        }
    }

    private final List<String> command;
    private final ProcessBuilder processBuilder = new ProcessBuilder();

    public Cmd(String cmd) {
        this(Arrays.asList("/bin/bash", "-c", cmd));
    }

    public Cmd(List<String> command) {
        this.command = command;
        processBuilder.command(command);
        processBuilder.redirectErrorStream(true);
    }

    public Cmd(List<String> command, Map<String, String> enviromentVariables) {
        this(command);
        processBuilder.environment().putAll(enviromentVariables);
    }

    public String constructedCmd() {
        StringBuilder sb = new StringBuilder();
        for (String cmd : command) {
            sb.append(cmd).append(" ");
        }

        return sb.toString().trim();
    }

    public Result run() {
        try {
            return runOrError();
        } catch (IOException e) {
            e.printStackTrace();
           return new Result.ResultBuilder(CMD_STATUS.INTERNAL_ERROR).error(e.toString()).build();
        }
    }

    private Result runOrError() throws IOException {
        Process process = processBuilder.start();

        BufferedReader outputReader = new BufferedReader(
                                            new InputStreamReader(process.getInputStream()));

        String output = readOutput(outputReader);
        try {
            CMD_STATUS status = waitFor(process);
            return new Result.ResultBuilder(status).output(output).constructedCmd(constructedCmd()).build();
        } finally {
            closeSilence(outputReader);
        }
    }

    private CMD_STATUS waitFor(Process process) {
        try {
            return getStatus(process.waitFor());
        } catch (InterruptedException e) {
            process.destroy();
            return CMD_STATUS.CMD_CANCELED;
        }
    }

    private String readOutput(BufferedReader reader) throws IOException {
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }
        return output.toString();
    }

    private void closeSilence(BufferedReader r) {
        try {
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CMD_STATUS getStatus(int code) {
        switch (code) {
            case 0 :
                return CMD_STATUS.OK;
            case 127:
                return CMD_STATUS.CMD_NOT_FOUND;
            case -10000:
                return CMD_STATUS.INTERNAL_ERROR;
            case -10001:
                return CMD_STATUS.CMD_CANCELED;
            default:
                return CMD_STATUS.UNKNOWN;
        }
    }

    public static class Result {

        private final String error;
        private final String stackTrace;
        private final String cmdOutput;
        private final String constructedCmd;
        private final CMD_STATUS status;

        private Result(ResultBuilder builder) {
            this.error = builder.error;
            this.stackTrace = builder.stackTrace;
            this.cmdOutput = builder.output;
            this.constructedCmd = builder.constructedCmd;
            this.status = builder.status;
        }

        public String getError() {
            return error;
        }

        public String getStackTrace() {
            return stackTrace;
        }

        public String getCmdOutput() {
            return cmdOutput;
        }

        public CMD_STATUS getStatus() {
            return status;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("exit code: ").append(status.getCode());
            if (constructedCmd != null) sb.append(", constructed cmd: ").append(constructedCmd);
            if (cmdOutput != null) sb.append(", cmd output: ").append(cmdOutput);
            if (error != null) sb.append(", error: ").append(error);
            if (stackTrace != null) sb.append(", java stack trace: ").append(stackTrace);
            return sb.toString();
        }

        public static class ResultBuilder {

            private final CMD_STATUS status;
            private String constructedCmd;
            private String output;
            private String error;
            private String stackTrace;

            public Result build() {
                return new Result(this);
            }

            public ResultBuilder(CMD_STATUS status) {
                this.status = status;
            }

            public ResultBuilder constructedCmd(String value) {
                constructedCmd = value;
                return this;
            }

            public ResultBuilder output(String value) {
                output = value;
                return this;
            }

            public ResultBuilder error(String value) {
                error = value;
                return this;
            }

            public ResultBuilder stackTrace(String value) {
                stackTrace = value;
                return this;
            }
        }
    }
}
