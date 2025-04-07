package ua.nure.kryvko.roman;

public class Memento {
    private final String text;

    public Memento(String text) {
        this.text = text;
    }

    public String getData() {
        return text;
    }
}
