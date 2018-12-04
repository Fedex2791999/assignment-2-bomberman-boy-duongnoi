package uet.vteam.bomberman.entities.character.Enemy;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.extra.ai.pathfinding.AStarGrid;
import com.almasb.fxgl.extra.ai.pathfinding.AStarNode;
import javafx.geometry.Point2D;
import uet.vteam.bomberman.Bomberman;

import java.util.ArrayList;
import java.util.List;

public class BossMoveControl extends Component {
    private List<AStarNode> path = new ArrayList<>();

    private PositionComponent position;

    public boolean isDone() {
        return path.isEmpty();
    }

    // Ai di chuyển vào nhân vật
    public void moveTo(Point2D point) {

        position = getEntity().getPositionComponent();

        if (position.getValue().equals(point))
            return;

        AStarGrid grid = ((Bomberman) FXGL.getApp()).getGrid();

        int startX = (int)(position.getX() /40);
        int startY = (int)(position.getY() / 40);

        int targetX = (int)((point.getX() + 20) / 40);
        int targetY = (int)((point.getY() + 20) / 40);

        path = grid.getPath(startX, startY, targetX, targetY);

        // we use A*, so no need for that
        //getEntity().getComponentOptional(AiMoving.class).ifPresent(c -> c.resume());
    }
    // cập nhật di chuyển của AI
    @Override
    public void onUpdate(double tpf) {
        if (path.isEmpty())
            return;

        double speed = tpf * 60 * 5;

        AStarNode next = path.get(0);

        int nextX = next.getX() *40;
        int nextY = next.getY() * 40;

        double dx = nextX - position.getX();
        double dy = nextY - position.getY();

        if (Math.abs(dx) <= speed)
            position.setX(nextX);
        else
            position.translateX(speed * Math.signum(dx));

        if (Math.abs(dy) <= speed)
            position.setY(nextY);
        else
            position.translateY(speed * Math.signum(dy));

        if (position.getX() == nextX && position.getY() == nextY) {
            path.remove(0);
        }

        if (path.isEmpty()) {
            getEntity().getComponentOptional(AiMoving.class).ifPresent(c -> c.resume());
        }
    }
}
