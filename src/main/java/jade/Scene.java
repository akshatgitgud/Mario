package jade;

// This class sets a template for all the other scene classes which inherit it

public abstract class Scene {

    protected Camera camera;

    public Scene() {
    }

    public void init() {
    }

    public abstract void update(float dt);

}
