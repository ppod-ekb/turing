package ru.cbr.turing.dump.postgres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@SpringBootApplication
@ComponentScan({"ru.cbr.turing.dump.postgres"})
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        Config appConfig = context.getBean(Config.class);
        OutputDir outputDir = new OutputDir(appConfig.getDump().getDmpStorageMountPath());
        outputDir.mkdirs();
    }

    public static class OutputDir {
        private final String path;

        public OutputDir(String path) {
            this.path = path;
        }

        public void mkdirs() {
            File f = new File(path);
            f.mkdirs();
        }
    }

    @Configuration
    @EnableConfigurationProperties
    @ConfigurationProperties("configuration")
    public static class Config {

        private Infodiod infodiod;
        private Dump dump;
        private String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Infodiod getInfodiod() {
            return infodiod;
        }

        public void setInfodiod(Infodiod infodiod) {
            this.infodiod = infodiod;
        }

        public Dump getDump() {
            return dump;
        }

        public void setDump(Dump dump) {
            this.dump = dump;
        }

        public static class Infodiod {

            private String serviceHost;

            public String getServiceHost() {
                return serviceHost;
            }

            public void setServiceHost(String serviceHost) {
                this.serviceHost = serviceHost;
            }

        }

        public static class Dump {

            private String pgdumpCmd;
            private String dmpStorageMountPath;
            private String dmpFileExtension;
            private String logFileExtension;
            private String dmpFormat;

            public String getPgdumpCmd() {
                return pgdumpCmd;
            }

            public void setPgdumpCmd(String pgdumpCmd) {
                this.pgdumpCmd = pgdumpCmd;
            }

            public String getDmpStorageMountPath() {
                return dmpStorageMountPath;
            }

            public void setDmpStorageMountPath(String dmpStorageMountPath) {
                this.dmpStorageMountPath = dmpStorageMountPath;
            }

            public String getDmpFileExtension() {
                return dmpFileExtension;
            }

            public void setDmpFileExtension(String dmpFileExtension) {
                this.dmpFileExtension = dmpFileExtension;
            }

            public String getLogFileExtension() {
                return logFileExtension;
            }

            public void setLogFileExtension(String logFileExtension) {
                this.logFileExtension = logFileExtension;
            }

            public String getDmpFormat() {
                return dmpFormat;
            }

            public void setDmpFormat(String dmpFormat) {
                this.dmpFormat = dmpFormat;
            }
        }
    }

}
