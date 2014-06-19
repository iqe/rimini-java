package io.iqe.rimini;

public class Message {
    private Address address;
    private Object content;

    public Message(Address address, Object content) {
        this.address = address;
        this.content = content;
    }

    public Address getAddress() {
        return address;
    }

    public Object getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("%s: '%s'", address, content);
    }
}
