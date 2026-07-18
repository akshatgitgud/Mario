package Jade;

import org.lwjgl.glfw.GLFW;

public class MouseListner {
    private static MouseListner instance;
    private double scrollx, scrolly;
    private double xPos, yPos, lastx, lasty;
    private boolean mousebuttonPressed[] = new boolean[3];
    private boolean isdragging;

    public MouseListner() {
        this.scrollx = 0.0;
        this.scrolly = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastx = 0.0;
        this.lasty = 0.0;
    }

    public static MouseListner get() {
        if (MouseListner.instance == null) {
            MouseListner.instance = new MouseListner();
        }
        return MouseListner.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        get().lastx = get().xPos;
        get().lasty = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isdragging = get().mousebuttonPressed[0] || get().mousebuttonPressed[1] || get().mousebuttonPressed[2];
    }

    public static void mousebuttonCallback(long window, int button, int action, int mods) {
        if (action == GLFW.GLFW_PRESS) {
            if (button < get().mousebuttonPressed.length) {
                get().mousebuttonPressed[button] = true;
            }
        } else if (action == GLFW.GLFW_RELEASE) {
            if (button < get().mousebuttonPressed.length) {
                get().mousebuttonPressed[button] = false;
                get().isdragging = false;
            }
        }
    }

    public static void mousescrollCallback(long window, double xoffset, double yoffset) {
        get().scrollx = xoffset;
        get().scrolly = yoffset;
    }

    public static void endframe() {
        get().scrollx = 0;
        get().scrolly = 0;
        get().lastx = get().xPos;
        get().lasty = get().yPos;
    }

    public static float getx() {
        return (float) get().xPos;
    }

    public static float gety() {
        return (float) get().yPos;
    }

    public static float getDx() {
        return (float)(get().lastx - get().xPos);
    }

    public static float getDy() {
        return (float)(get().lasty - get().yPos);
    }

    public static float getscrollx() {
        return (float) get().scrollx;
    }

    public static float getscrolly() {
        return (float) get().scrolly;
    }

    public static boolean isdragging() {
        return get().isdragging;
    }

    public static boolean moousebuttondown(int button) {
        if (button < get().mousebuttonPressed.length) {
            return get().mousebuttonPressed[button];
        } else {
            return false;
        }
    }

}
