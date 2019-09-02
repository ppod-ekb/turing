package ru.cbr.turing.dump.oraexp;


public class Config {

    private final String conn;
    private final String file;
    private final String log;
    private final String rows;
    private final String cmd;
    private final String libraryPath;

    private Config(ConfigBuilder confBuilder) {
        this.conn = confBuilder.conn;
        this.file = confBuilder.file;
        this.log = confBuilder.log;
        this.rows = confBuilder.rows;
        this.cmd = confBuilder.cmd;
        this.libraryPath = confBuilder.libraryPath;
    }

    public String getCmd() {
        return cmd;
    }

    public String getConn() {
        return conn;
    }

    public String getFile() {
        return file;
    }

    public String getLog() {
        return log;
    }

    public String getRows() {
        return rows;
    }

    public String getLibraryPath() {
        return libraryPath;
    }

    public static class ConfigBuilder {

        private final String cmd;
        private final String libraryPath;
        private String conn;
        private String file;
        private String log;
        private String rows = "N";

        public ConfigBuilder(String cmd, String libraryPath) {
            this.cmd = cmd;
            this.libraryPath = libraryPath;
        }

        public ConfigBuilder conn(String conn) {
            this.conn = conn;
            return this;
        }

        public ConfigBuilder file(String file) {
            this.file = file;
            return this;
        }

        public ConfigBuilder file(String outputPath, String fileName) {
            StringBuilder sb = new StringBuilder();
            sb.append(outputPath).append("/").append(fileName);
            this.file = sb.toString();
            return this;
        }

        public ConfigBuilder log(String log) {
            this.log = log;
            return this;
        }

        public ConfigBuilder log(String outputPath, String fileName) {
            StringBuilder sb = new StringBuilder();
            sb.append(outputPath).append("/").append(fileName);
            this.log = sb.toString();
            return this;
        }

        public ConfigBuilder rows(String rows) {
            this.rows = rows;
            return this;
        }

        public Config build(){
            return new Config(this);
        }

    }

}
