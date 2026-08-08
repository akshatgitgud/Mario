package components;

import jade.Component;

public class FontRenderer extends Component {



    public void start() {
        if(gameObject.getComponent(SprintRenderer.class)!=null){
            System.out.println("Found Font Renderer!");
        }
    }

    public void update(float dt) {

    }

}
