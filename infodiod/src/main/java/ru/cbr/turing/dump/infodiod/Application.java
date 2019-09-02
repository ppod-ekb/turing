package ru.cbr.turing.dump.infodiod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan({"ru.cbr.turing.dump.infodiod"})
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    @EnableConfigurationProperties
    @ConfigurationProperties("configuration")
    public static class Config {
        private String version;
        private String dmpFileExtension;
        private String logFileExtension;
        private String storageMountPath;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
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

        public String getStorageMountPath() {
            return storageMountPath;
        }

        public void setStorageMountPath(String storageMountPath) {
            this.storageMountPath = storageMountPath;
        }
    }
}