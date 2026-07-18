package Jade;

import org.lwjgl.glfw.GLFW;

public class KeyListener {
    private static KeyListener instance;
    private boolean keypressed[] = new boolean[350];

    private KeyListener() {
    };

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keycallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW.GLFW_PRESS) {
            get().keypressed[key] = true;
        } else if (action == GLFW.GLFW_RELEASE) {
            get().keypressed[key] = false;
        }
    }

    public static boolean iskeypressed(int keycode) {
        return get().keypressed[keycode];
    }

}
