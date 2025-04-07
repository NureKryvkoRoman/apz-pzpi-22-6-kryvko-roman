package ua.nure.kryvko.roman;

import java.util.Stack;

public class Caretaker {
    private Originator originator;
    private Stack<Memento> history;

    public Caretaker(Originator originator) {
        this.originator = originator;
        history = new Stack<>();
    }

    public void makeSnapshot() {
        Memento oldState = originator.saveState();
        history.add(oldState);
    }

    public void undo() {
        if (!history.empty()) {
            Memento memento = history.pop();
            originator.restore(memento);
        }
    }
}
