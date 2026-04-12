package net.kdt.pojavlaunch.fok;

import net.kdt.pojavlaunch.LwjglGlfwKeycode;
import org.lwjgl.glfw.CallbackBridge;

public final class FokAuthmeHelper {
    private static final int INITIAL_DELAY_MS = 15000;
    private static final int RETRY_DELAY_MS = 5000;
    private static final int ATTEMPT_COUNT = 12;
    private static final int REGISTER_ATTEMPTS = 3;
    private static final int BETWEEN_KEYS_MS = 80;
    private static final int CHAT_READY_DELAY_MS = 350;
    private static final int BETWEEN_COMMANDS_MS = 700;

    private FokAuthmeHelper() {}

    public static void scheduleAuthCommand() {
        String password = FokPrefs.getPassword();
        if (password == null || password.isEmpty()) return;
        FokServerPreset preset = FokPrefs.getServerPreset(FokPrefs.getSelectedServerIndex());
        if (preset == null || preset.getAddress().isEmpty()) return;

        new Thread(() -> {
            try {
                Thread.sleep(INITIAL_DELAY_MS);
                for (int attempt = 0; attempt < ATTEMPT_COUNT; attempt++) {
                    if (attempt < REGISTER_ATTEMPTS) {
                        sendCommand("/register " + password);
                        Thread.sleep(BETWEEN_COMMANDS_MS);
                    }
                    sendCommand("/login " + password);
                    if (attempt + 1 < ATTEMPT_COUNT) {
                        Thread.sleep(RETRY_DELAY_MS);
                    }
                }
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }, "FOK-AuthMe").start();
    }

    private static void sendKey(int keyCode) throws InterruptedException {
        CallbackBridge.sendKeyPress(keyCode);
        Thread.sleep(BETWEEN_KEYS_MS);
    }

    private static void sendCommand(String command) throws InterruptedException {
        sendKey(LwjglGlfwKeycode.GLFW_KEY_T);
        Thread.sleep(CHAT_READY_DELAY_MS);
        sendString(command);
        Thread.sleep(150);
        sendKey(LwjglGlfwKeycode.GLFW_KEY_ENTER);
    }

    private static void sendString(String text) throws InterruptedException {
        for (int i = 0; i < text.length(); i++) {
            CallbackBridge.sendChar(text.charAt(i), 0);
            Thread.sleep(BETWEEN_KEYS_MS);
        }
    }
}
