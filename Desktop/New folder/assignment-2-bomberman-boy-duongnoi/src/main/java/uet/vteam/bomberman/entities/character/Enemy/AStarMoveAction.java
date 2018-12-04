package uet.vteam.bomberman.entities.character.Enemy;

import com.almasb.fxgl.ai.GoalAction;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.extra.ai.pathfinding.AStarGrid;
import javafx.geometry.Point2D;
import uet.vteam.bomberman.Bomberman;

public class AStarMoveAction extends GoalAction {
    public AStarMoveAction() {
        super("Move");
    }

    private AStarGrid grid;
    private Entity player;

    private PositionComponent position;

    private int targetX;
    private int targetY;

    @Override
    public void start() {
        position = getEntity().getPositionComponent();

        player = ((Bomberman) FXGL.getApp()).getPlayer();
        targetX = (int)((player.getX() + 20) / 40);
        targetY = (int)((player.getY() + 20) / 40);

        getEntity().getComponent(BossMoveControl.class).moveTo(new Point2D(targetX * 40, targetY * 40));
    }

    @Override
    public boolean reachedGoal() {
        return getEntity().getComponent(BossMoveControl.class).isDone();
    }

    @Override
    public void onUpdate(double tpf) {

    }
}
