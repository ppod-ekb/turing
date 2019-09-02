package ru.cbr.turing.dump.oraexp;

public class Exp {

    private final Config config;

    public Exp(Config config) {
        this.config = config;
    }

    public Cmd.Result run() {
        Cmd cmd = buildCmd();
        return cmd.run();
    }

    private Cmd buildCmd() {
        ExpCmdBuilder builder = new ExpCmdBuilder(config.getCmd(), config.getConn(), config.getLibraryPath());
        builder.file(config.getFile());
        builder.log(config.getLog());
        builder.rows(config.getRows());

        return builder.build();
    }

}
