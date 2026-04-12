package net.kdt.pojavlaunch.fok;

public class FokServerPreset {
    private final String name;
    private final String address;
    private final String voiceRoom;

    public FokServerPreset(String name, String address) {
        this(name, address, "");
    }

    public FokServerPreset(String name, String address, String voiceRoom) {
        this.name = name == null ? "" : name;
        this.address = address == null ? "" : address;
        this.voiceRoom = voiceRoom == null ? "" : voiceRoom;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getVoiceRoom() {
        return voiceRoom;
    }

    public String displayLabel() {
        if (address.isEmpty()) return name + " — (boş)";
        return name + " — " + address;
    }
}
