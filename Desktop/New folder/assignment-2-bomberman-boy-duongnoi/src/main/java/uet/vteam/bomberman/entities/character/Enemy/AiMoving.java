package uet.vteam.bomberman.entities.character.Enemy;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.entity.components.TypeComponent;
import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uet.vteam.bomberman.BombermanType;
import uet.vteam.bomberman.Game;
import uet.vteam.bomberman.entities.character.Bomber.BomberType;

import java.util.List;

public class AiMoving extends Component {
    private PositionComponent position;
    private BoundingBoxComponent bbox;

    private Direction moveDir;
    private BombermanType type;
    private BombermanType direction;

    public void setMoveDirection(Direction moveDir) {
        this.moveDir = moveDir;
    }

    @Override
    public void onAdded() {
        moveDir = FXGLMath.random(Direction.values()).get();

        // thay đổi trạng thái ran dom sau 0,3 giây
        FXGL.getMasterTimer().runAtInterval(() -> {
            // nhận lại giá trị moveDir sau 0,3 s
            getDir();

        }, Duration.millis(400));

    }

    public void getDir ()
    {
        moveDir = FXGLMath.random(Direction.values()).get();

    }
    private double speed = 4;
    @Override
    public void onUpdate(double tpf) {
        //   speed = tpf * 80;
        switch (moveDir) {
            case UP:
                up();
                break;

            case DOWN:
                down();
                break;

            case LEFT:
                left();
                break;

            case RIGHT:
                right();
                break;
        }

    }

    private void up() {
      //  System.out.println("Up");
        move( - speed);



    }

    private void down() {
       // System.out.println("Down");
        move( speed);

    }

    private void left() {
      //  System.out.println("Left");
        move(  -speed);

    }

    private void right() {
       // System.out.println("Right");
        move( speed);

    }


    public  void move(double speed)
    {
        int x = position.getGridX(Game.SIZE_TILES);
        int y = position.getGridY(Game.SIZE_TILES);
        if(speed != 0){

            if((moveDir== Direction.UP || moveDir== Direction.DOWN)
                    && (getEntity().getX() - x*Game.SIZE_TILES >= -10)
                    && (getEntity().getX() + getEntity().getWidth() - (x+1)*Game.SIZE_TILES <= 10)){

                if((-getEntity().getY() + y*Game.SIZE_TILES > 1) && moveDir == Direction.UP){
                    if(canMove(new Point2D(x*Game.SIZE_TILES, (y-1)*Game.SIZE_TILES))) {
                        entity.translateY( speed);
                    }
                }
                else if((-getEntity().getY() + y*Game.SIZE_TILES < 1) && moveDir == Direction.DOWN){
                    if(canMove(new Point2D(x*Game.SIZE_TILES, (y+1)*Game.SIZE_TILES))){
                        entity.translateY(speed);
                    }

                }
                else if(canMove(new Point2D(x*Game.SIZE_TILES, (y-1)*Game.SIZE_TILES)) || canMove(new Point2D(x*40, (y+1)*40))){


                    entity.translateY(speed);
                }
            }

            if((moveDir == Direction.LEFT || moveDir == Direction.RIGHT)
                    && (getEntity().getY() + getEntity().getHeight() - (y+1) * Game.SIZE_TILES <= 5)){

                if((-getEntity().getX() + x*Game.SIZE_TILES > 1)
                        && moveDir == Direction.LEFT){

                    if(canMove(new Point2D((x-1)*Game.SIZE_TILES, y*Game.SIZE_TILES))
                    ) {
                        entity.translateX( speed);
                    }
                }
                else if((-getEntity().getX() + x*Game.SIZE_TILES < 1)
                        && moveDir == Direction.RIGHT){


                    if(canMove(new Point2D((x+1)*Game.SIZE_TILES, y*Game.SIZE_TILES))){
                        entity.translateX(speed);
                    }

                }
                else if((canMove(new Point2D((x-1)*Game.SIZE_TILES, y*Game.SIZE_TILES))
                        || canMove(new Point2D((x+1)*Game.SIZE_TILES, y*Game.SIZE_TILES)))){
                    entity.translateX(speed);
                }
            }

        }




       /* int x = position.getGridX(BombermanApp.TILE_SIZE);
        int y = position.getGridY(BombermanApp.TILE_SIZE);
        if (moveDir==Direction.LEFT|| moveDir==Direction.RIGHT)
        {
            if ((getEntity().getX() +  speed - (x * 40) > 15 && moveDir==Direction.RIGHT)
                    || ((x * 40) - getEntity().getX() +  speed > 15 && moveDir==Direction.LEFT)) {

                if ((canMove(new Point2D((x + 1) * 40, y * 40)) &&moveDir==Direction.RIGHT)
                        || (canMove(new Point2D((x - 1) * 40, y * 40)) &&moveDir==Direction.LEFT)) {

                    entity.translateX( speed);
                }
            } else if (canMove(new Point2D(x * 40, y * 40))) {
                entity.translateX( speed);
            }


        }
        if (moveDir==Direction.UP || moveDir==Direction.DOWN) {

            if ((getEntity().getY() + speed - (y * 40) > 10 &&moveDir==Direction.DOWN)
                    || ((y * 40) - getEntity().getY() + speed > 10 && moveDir==Direction.UP)) {

                if ((canMove(new Point2D(x * 40, (y + 1) * 40)) && moveDir==Direction.DOWN)
                        || (canMove(new Point2D(x * 40, (y - 1) * 40)) && moveDir==Direction.UP)) {
                    entity.translateY(speed);
                }
            } else if (canMove(new Point2D(x * 40, y * 40))) {
                entity.translateY(speed);
            }

        }*/
    }

   private boolean canMove(Point2D direction) {
       Point2D newPosition = direction;

       return FXGL.getApp()
               .getGameScene()
               .getViewport()
               .getVisibleArea()
               .contains(newPosition)

               &&

               FXGL.getApp()
                       .getGameWorld()
                       .getEntitiesAt(newPosition)
                       .stream()
                       .filter(e -> e.hasComponent(TypeComponent.class))
                       .map(e -> e.getComponent(TypeComponent.class))
                       .noneMatch(type -> type.isType(BombermanType.BRICK)
                               || type.isType(BombermanType.WALL)
                       || type.isType(BombermanType.BOMB)
                       || type.isType(BombermanType.ENEMY));
   }


}
