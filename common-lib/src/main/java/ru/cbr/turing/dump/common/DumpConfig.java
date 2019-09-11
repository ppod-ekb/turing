package ru.cbr.turing.dump.common;

public interface DumpConfig {

    public String getCmd();

    public String getDmpFile();

    public String getLogFile();

    public String getHost();

    public String getPort();

    public String getDbName();

    public String getUsername();

    public String getPassword();
}
