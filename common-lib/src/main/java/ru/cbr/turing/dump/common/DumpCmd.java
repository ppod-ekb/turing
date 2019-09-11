package ru.cbr.turing.dump.common;


public interface  DumpCmd {

    Result run();

    interface Result {

        Cmd.Result getCmdResult();

        DumpFile getDumpFile();

        LogFile getLogFile();

        boolean isLogAndDumpFileCreated();

        interface DumpFile {
            boolean isDumpFileCreated();
        }

        interface LogFile {
            boolean isLogFileCreated();
        }
    }
}
