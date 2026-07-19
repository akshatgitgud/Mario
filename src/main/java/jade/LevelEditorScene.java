package jade;

import java.awt.event.KeyEvent;

class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    float timeToChangeScene = 2.0f;

    public LevelEditorScene() {
        System.out.println("Inside Level editor scene");
    };

    @Override
    public void update(float dt) {
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }
        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;

        } else if (changingScene) {
            Window.changeScene(1);
        }
    }

}