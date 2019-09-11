package ru.cbr.turing.dump.common;

import java.util.List;

public interface ArgBuilder<T> {

    void addIfNotNullAndNotEmpty(String value);

    void add(String value);

    T args();

    class ArgStringBuilder implements ArgBuilder<String> {

        private final StringBuilder sb;

        public ArgStringBuilder(StringBuilder sb) {
            this.sb = sb;
        }

        public ArgStringBuilder(ArgStringBuilder sb) {
            this(sb.stringBuilder());
        }

        public StringBuilder stringBuilder() {
            return sb;
        }

        public void addIfNotNullAndNotEmpty(String value) {
            if (value != null && !value.isEmpty()) {
                add(value);
            }
        }

        public void add(String value) {
            sb.append(" ").append(value);
        }

        public String args() {
            return toString();
        }

        @Override public String toString() {
            return sb.toString().trim();
        }
    }


    class ArgListBuilder implements ArgBuilder<List<String>> {

        private final List<String> args;

        public ArgListBuilder(List<String> args) {
            this.args = args;
        }

        public List<String> args() {
            return args;
        }

        public void addIfNotNullAndNotEmpty(String value) {
            if (value != null && !value.isEmpty()) {
                add(value);
            }
        }

        public void add(String value) {
            args.add(value);
        }


        @Override public String toString() {
            StringBuilder sb = new StringBuilder();
            for (String v : args) {
                sb.append(" ").append(v);
            }
            return sb.toString().trim();
        }
    }

}
