package edu.nyu.oop.util;

import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Collection;

public class TranslationWriter {
    private Queue<SourceOutputCommand> commands;

    public TranslationWriter() {
        this.commands = new ArrayDeque<SourceOutputCommand>();
    }

    public void add(SourceOutputCommand s) {
        if (s == null) return;

        commands.add(s);
    }

    public void addAll(Collection<SourceOutputCommand> s) {
        if (s == null) return;

        commands.addAll(s);
    }

    public void execute(SourceOutputCommand s) {
        if (s == null) return;
        s.outputSourceExecute();
    }

    public void executeAll() {
        while (!this.commands.isEmpty()) {
            execute(this.commands.poll());
        }
    }

}
