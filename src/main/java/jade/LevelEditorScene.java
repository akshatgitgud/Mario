package jade;

// import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY_TYPE;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
// import java.awt.event.KeyEvent;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import renderer.Shader;

class LevelEditorScene extends Scene {
    private String vertexShaderSrc = "#version 330 core\n" +
            "layout(location = 0) in vec3 aPos;\n" +
            "layout(location = 1) in vec4 aColor;\n" +
            "out vec4 fColor;\n" +
            "void main() {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" + //
            "\n" + //
            "in vec4 fColor;\n" + //
            "out vec4 color;\n" + //
            "void main() { color = fColor; }";
    private int vertexID, fragmentID, shaderProgramID;
    private float[] vertexArray = {
            // Position //color
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // Top left 1
            0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // Top right 2
            -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,// Bottom left 3
    };
    // Must be in counterclockwise manner
    private int[] elementArray = {
            /*
             * x x
             * 
             * 
             * x x
             */
            2, 1, 0, // Top right Triangle
            0, 1, 3 // Bottom right Triangle
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;

    public LevelEditorScene() {

    };

    @Override
    public void init() {
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        // COMPILE AND LOAD SHADERS

        // LOAD AND COMPILE VERTEX SHADER
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // PASS THE SHADER SOURCE CODE
        glShaderSource(vertexID, vertexShaderSrc);
        // COMPIlE
        glCompileShader(vertexID);

        // Check for errors during compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID));
            assert false : " ";
        }

        // LOAD AND COMPILE VERTEX SHADER
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // PASS THE SHADER SOURCE CODE
        glShaderSource(fragmentID, fragmentShaderSrc);
        // COMPIlE
        glCompileShader(fragmentID);

        // Check for errors during compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID));
            assert false : " ";
        }

        // LINK SHADERS AND CHECK FOR ERRORS
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking shader failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID));
            assert false : " ";
        }
        // Generate VAO , VBO and EBO Buffers Objects, and send to GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO and upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create indices
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // ADD VERTEX ATTRIBUTE BINDERS
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        ;
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        // defaultShader.use();
        // BIND SHADER PROGRAM
        glUseProgram(shaderProgramID);
        // BIND THE VAO WE ARE USING
        glBindVertexArray(vaoID);

        // Enable vertex attributes pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // UNBIND EVERYTHING
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glUseProgram(0);
        defaultShader.detach();
    }

}