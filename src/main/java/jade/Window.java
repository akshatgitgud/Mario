package jade;

import util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFWVidMode;
import java.util.Objects;
import org.lwjgl.glfw.Callbacks.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

//Defining a singleton class so that the window gets called only once
public class Window {
    private int height, width;
    private String title;
    private static Window window = null;
    private long glfwWindow;
    public float r, g, b;
    private float a;
    private boolean fadetoblack = false;
    private static Scene currentScene;

    // Defining the ratio and title of the window
    private Window() {
        this.width = 1280;
        this.height = 720;
        this.title = "Mario";
        r = 1.0f;
        b = 1.0f;
        g = 1.0f;
        a = 1.0f;

    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                // currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();

            default:
                assert false : "Unknown scene" + newScene + " ";
                break;
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");
        init();
        loop();
        // Free the memory
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void init() {
        // To throw error, setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();
        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to Initialize GLFW");
        }
        // Configure glfw
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW Window.");
        }

        // Callback for mouse
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable V-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // CRITICAL: Initialize LWJGL's OpenGL bindings
        // This links LWJGL to the context created by GLFW
        GL.createCapabilities();
        Window.changeScene(0);
    }

    public void loop() {

        while (!glfwWindowShouldClose(glfwWindow)) {
            float beginTime = Time.getTime();
            float endTime = Time.getTime();
            float dt = -1.0f;
            // Poll events
            glfwPollEvents();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);
            if (dt >= 0){
                currentScene.update(dt);
            }
            // // JUST CHECKING INPUTS
            if (fadetoblack) {
                // System.out.println("IS FADING");
                r = Math.max(r - 0.01f, 0);
                g = Math.max(g - 0.01f, 0);
                b = Math.max(b - 0.01f, 0);
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE) && !fadetoblack) {
                fadetoblack = true;
                Window.changeScene(1);
            }
            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
