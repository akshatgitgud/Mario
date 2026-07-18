package Jade;

import org.lwjgl.glfw.GLFW;

public class Keylistner {
    private static Keylistner instance;
    private boolean keypressed[] = new boolean[350];

    private Keylistner() {
    };

    public static Keylistner get() {
        if (Keylistner.instance == null) {
            Keylistner.instance = new Keylistner();
        }
        return Keylistner.instance;
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
