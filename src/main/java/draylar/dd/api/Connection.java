package draylar.dd.api;

public class Connection {

    private final Room a;
    private final Room b;

    public Connection(Room a, Room b) {
        this.a = a;
        this.b = b;
    }

    public boolean contains(Room a, Room b) {
        return (a == this.a && b == this.b) || (a == this.b || b == this.a);
    }
}
