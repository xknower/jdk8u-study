package jdk.jfr.internal.tool;

import java.util.Arrays;
import java.util.Deque;
import java.util.List;

final class Version extends Command {
    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getDescription() {
        return "Display version of the jfr tool";
    }

    @Override
    public void execute(Deque<String> options) {
        System.out.println("1.0");
    }

    protected List<String> getAliases() {
        return Arrays.asList("--version");
    }
}
