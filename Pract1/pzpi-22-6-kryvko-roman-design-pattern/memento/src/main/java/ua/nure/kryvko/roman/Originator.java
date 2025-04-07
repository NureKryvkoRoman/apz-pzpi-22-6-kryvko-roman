package ua.nure.kryvko.roman;

public class Originator {
    private String text;

    public Originator()
    {
    }

    public void setText(String newText) {
        text = newText;
    }

    public void removeText() {
        text = "";
    }

    public String getText() {
        return text;
    }

    public Memento saveState() {
        return new Memento(text);
    }

    public void restore(Memento memento) {
        text = memento.getData();
    }
}
