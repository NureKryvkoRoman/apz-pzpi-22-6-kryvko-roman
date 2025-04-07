package ua.nure.kryvko.roman;

public class Main {
    public static void main(String[] args) {
        Originator originator = new Originator();
        Caretaker caretaker = new Caretaker(originator);

        originator.setText("Hello, World!");
        caretaker.makeSnapshot();
        System.out.println("State before erasing: " + originator.getText()); // prints "Hello, World!"

        originator.removeText();
        System.out.println("State after erasing: " + originator.getText()); // prints ""

        caretaker.undo();
        System.out.println("State after restoring: " + originator.getText()); // prints "Hello, World!" again
    }
}