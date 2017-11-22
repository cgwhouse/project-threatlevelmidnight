package edu.wofford;

public class NamedArgument extends Argument {
    private String nicknames;

    public NamedArgument(String name, String value) {
        super(name);
        this.setValue(value);
        nicknames = "-";
    }

    public void addNickname(String nickname) {
        nicknames += nickname;
    }

    public String getNicknames() {
        return nicknames;
    }
}
