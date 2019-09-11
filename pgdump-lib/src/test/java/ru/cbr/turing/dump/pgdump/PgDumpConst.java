package ru.cbr.turing.dump.pgdump;

public class PgDumpConst {

    public static final String OUT_PATH = "/tmp";
    public static final String DMP_FILE = "42.dmp";
    public static final String LOG_FILE = "42.log";

    public static final String CMD = "/usr/bin/pg_dump";
    public static final String DB_NAME = "kav";
    public static final String LOG_TO_FILE = OUT_PATH + "/" + LOG_FILE;

    public static final  String HOST = "172.17.0.3";
    public static final  String PORT = "5432";
    public static final  String FORMAT = "c";
    public static final  String USERNAME = "kav";
    public static final  String FILE = OUT_PATH + "/" + DMP_FILE;
    public static final  boolean NO_PASSWORD = true;
    public static final  boolean NO_ROWS = true;
    public static final  boolean VERBOSE = true;


    public static final  String PG_PASSWORD = "kav";
}
