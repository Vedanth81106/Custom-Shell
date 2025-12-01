package org.waffles.commands;

import java.io.IOException;

public interface Command {

    public void execute(String... args) throws IOException;
}
