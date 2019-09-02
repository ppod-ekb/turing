package ru.cbr.turing.dump.oracle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.beans.factory.annotation.Autowired;
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
@ComponentScan({"ru.cbr.turing.dump.oracle"})
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        Config appConfig = context.getBean(Config.class);
        OutputDir outputDir = new OutputDir(appConfig.getDump().getOutputPath());
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
            private String dumpCreatedAction;

            public String getServiceHost() {
                return serviceHost;
            }

            public void setServiceHost(String serviceHost) {
                this.serviceHost = serviceHost;
            }

            public String getDumpCreatedUrl() {
                return serviceHost + dumpCreatedAction;
            }

            public String getDumpCreatedAction() {
                return dumpCreatedAction;
            }

            public void setDumpCreatedAction(String dumpCreatedAction) {
                this.dumpCreatedAction = dumpCreatedAction;
            }
        }

        public static class Dump {

            private String profile;
            private String clientPath;
            private String expCmd;
            private String outputPath;
            private String dmpFileExtension;
            private String logFileExtension;

            public String getProfile() {
                return profile;
            }

            public void setProfile(String profile) {
                this.profile = profile;
            }

            public String getClientPath() {
                return clientPath;
            }

            public void setClientPath(String clientPath) {
                this.clientPath = clientPath;
            }

            public String getExpCmd() {
                return expCmd;
            }

            public void setExpCmd(String expCmd) {
                this.expCmd = expCmd;
            }

            public String getOutputPath() {
                return outputPath;
            }

            public void setOutputPath(String outputPath) {
                this.outputPath = outputPath;
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
        }
    }

}
