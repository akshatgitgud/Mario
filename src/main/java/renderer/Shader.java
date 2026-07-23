package renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shader {

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
            // Find first pattern after #type "pattern"
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\n", index);
            String firstPattern = source.substring(index, eol).trim();
            // Find first pattern after #type "pattern"
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token" + firstPattern);
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token" + secondPattern);
            }

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error:Could not open file for shader:'" + filepath + "'";
        }
        System.out.println(vertexSource);
        System.out.println(fragmentSource);
    }
    private int shaderProgramID;
    public void compile() {
        shaderProgramID = glCreateProgram();
        // COMPILE AND LINK SHADER
        int vertexID, fragmentID;

        // LOAD AND COMPILE VERTEX SHADER
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // PASS THE SHADER SOURCE CODE
        glShaderSource(vertexID, vertexSource);
        // COMPIlE
        glCompileShader(vertexID);

        // Check for errors during compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + filepath + "'defaultShader.glsl'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID));
            assert false : " ";
        }

        // LOAD AND COMPILE VERTEX SHADER
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // PASS THE SHADER SOURCE CODE
        glShaderSource(fragmentID, fragmentSource);
        // COMPIlE
        glCompileShader(fragmentID);

        // Check for errors during compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + filepath + "'defaultShader.glsl'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID));
            assert false : " ";
        }

    }

    public void use() {
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
    }
}
