package ru.cbr.turing.dump.common;

import java.io.*;
import java.util.*;

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

    private final ProcessBuilder processBuilder = new ProcessBuilder();

    public Cmd(List<String> command) {
       this(command, Collections.emptyMap(), null);
    }

    public Cmd(List<String> command, Map<String, String> enviromentVariables) {
        this(command, enviromentVariables, null);
    }

    public Cmd(List<String> command, Map<String, String> enviromentVariables, File outputToFile) {
        processBuilder.command(command);
        processBuilder.redirectErrorStream(true);

        processBuilder.environment().putAll(enviromentVariables);

        if (outputToFile != null) {
            processBuilder.redirectOutput(outputToFile);
        }
    }

    public Result run() {
        try {
            return runOrError();
        } catch (IOException e) {
            e.printStackTrace();
           return new Result.ResultBuilder(CMD_STATUS.INTERNAL_ERROR).error(e.toString()).build();
        }
    }

    public String constructedCmd() {
        return new ConstructedCmd().constructedCmd();
    }

    public String envVars() {
        return new EnviromentVars().envVars();
    }

    private Result runOrError() throws IOException {
        Process process = processBuilder.start();
        //OutputReader outputReader = new OutputReader(process.getInputStream());

        OutputReaderBuilder builder = new OutputReaderBuilder();
        OutputReader outputReader = null;
        File outputFile = processBuilder.redirectOutput().file();
        if (outputFile != null) {
            outputReader = builder.createReader(outputFile);
        } else {
            outputReader = builder.createReader(process);
        }

       /* BufferedReader outputReader = new BufferedReader(
                                            new InputStreamReader(process.getInputStream()));

        String output = readOutput(outputReader);*/
        try {
            CMD_STATUS status = waitFor(process);
            String output = outputReader.read();
            return new Result.ResultBuilder(status)
                             .output(output)
                             .constructedCmd(new ConstructedCmd().constructedCmd())
                             .enviromentVars(new EnviromentVars().envVars()).build();
        } finally {
            outputReader.closeSilence();
        }
    }

    private static class OutputReaderBuilder {

        public OutputReader createReader(Process process) {
            return new OutputReader(process.getInputStream());
        }

        public OutputReader createReader(File file) throws FileNotFoundException {
            return new OutputReader(new FileReader(file));
        }

    }

    private static class OutputReader {

        private BufferedReader outputReader;

        public OutputReader(InputStream is) {
            outputReader = new BufferedReader(new InputStreamReader(is));
        }

        public OutputReader(FileReader fr) {
            outputReader = new BufferedReader(fr);
        }

        public String read() throws IOException  {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = outputReader.readLine()) != null) {
                output.append(line + "\n");
            }
            return output.toString();
        }

        public void closeSilence() {
            try {
                outputReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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


    private class EnviromentVars {

        public String envVars() {
            Map<String, String> envVars = processBuilder.environment();
            if (envVars != null) {
                return getEnvVars(envVars);
            } else {
                return "";
            }
        }

        private String getEnvVars(Map<String, String> envVars) {
            StringBuilder sb = new StringBuilder();
            Set<String> keys = envVars.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                sb.append(key).append("=").append(envVars.get(key)).append(";");
            }
            return sb.toString();
        }

    }

    private class ConstructedCmd {

        public String constructedCmd() {
            StringBuilder sb = new StringBuilder();
            for (String cmd : processBuilder.command()) {
                sb.append(cmd).append(" ");
            }

            return sb.toString().trim();
        }

    }

    public static class Result {

        private final String error;
        private final String stackTrace;
        private final String cmdOutput;
        private final String constructedCmd;
        private final String enviromentVars;
        private final CMD_STATUS status;

        private Result(ResultBuilder builder) {
            this.error = builder.error;
            this.stackTrace = builder.stackTrace;
            this.cmdOutput = builder.output;
            this.constructedCmd = builder.constructedCmd;
            this.enviromentVars = builder.enviromentVars;
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

        public String getConstructedCmd() {
            return constructedCmd;
        }

        public String getEnviromentVars() {
            return enviromentVars;
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
            private String enviromentVars;
            private String output;
            private String error;
            private String stackTrace;

            public Result build() {
                return new Result(this);
            }

            public ResultBuilder(CMD_STATUS status) {
                this.status = status;
            }

            public ResultBuilder enviromentVars(String value) {
                enviromentVars = value;
                return this;
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
