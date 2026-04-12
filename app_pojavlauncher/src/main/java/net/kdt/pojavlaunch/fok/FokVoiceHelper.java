package net.kdt.pojavlaunch.fok;

public final class FokVoiceHelper {
    private static final String ROOM_PREFIX = "fok-voice-";

    private FokVoiceHelper() {}

    public static String resolveRoomName(FokServerPreset preset) {
        if (preset == null) return ROOM_PREFIX + "genel";

        String source = preset.getVoiceRoom();
        if (source == null || source.trim().isEmpty()) {
            source = preset.getAddress();
        }
        if (source == null || source.trim().isEmpty()) {
            source = preset.getName();
        }

        String sanitized = source
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "")
                .replaceAll("-{2,}", "-");

        if (sanitized.isEmpty()) {
            sanitized = "genel";
        }
        return ROOM_PREFIX + sanitized;
    }

    public static String buildMeetingUrl(FokServerPreset preset) {
        return "https://meet.jit.si/" + resolveRoomName(preset);
    }
}
