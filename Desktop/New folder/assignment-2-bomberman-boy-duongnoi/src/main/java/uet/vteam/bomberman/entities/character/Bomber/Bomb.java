package uet.vteam.bomberman.entities.character.Bomber;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.PositionComponent;
import javafx.geometry.Point2D;
import uet.vteam.bomberman.Bomberman;
import uet.vteam.bomberman.BombermanType;
import uet.vteam.bomberman.Game;

public class Bomb extends Component {
    private static PositionComponent position;
    public static int powerup = 1;
    private int size_bomb = 0;
    private  int flameItem;
    private boolean canExplodeHorizontal=true;
    private boolean canExplodeVertical=true;
    public Bomb(int flameItem) {
        this.flameItem=3;
    }
    private void setPowerUp(int powerup)
    {
        this.powerup = powerup;
    }
    public static int getPowerUp()
    {
        return powerup;
    }
    public static void increasePowerUp()
    {
        if (powerup > 5)
        {
            powerup = 4;
        }
        powerup++;
    }
    // chiều ngang
    public void explodeHorizontal(double xCenterExplode, double yCenterExplode, double xTopExplode, double yTopExplode, int scale){
        if (canExplodeHorizontal) {
            FXGL.getApp()
                    .getGameWorld()
                    .getEntitiesAt(new Point2D(xTopExplode, yTopExplode))
                    .stream()
                    .forEach(e -> {
                        FXGL.getApp().getGameWorld().spawn("centerExplode", xCenterExplode, yCenterExplode);
                        if (e.isType(BombermanType.GRASS)) {
                            // xét vị trí nổ từng thành phần của bomb
                            FXGL.getApp().getGameWorld().spawn("midExplodeX", xTopExplode, yTopExplode).setScaleX(scale);
                        }
                        if (e.isType(BombermanType.BRICK)) {
                            FXGL.getApp().getGameWorld().spawn("midExplodeX", xTopExplode, yTopExplode).setScaleX(scale);
                            FXGL.getApp().getGameWorld().spawn("Grass", xTopExplode, yTopExplode);
                            FXGL.<Bomberman>getAppCast().onWallDestroyed(e);
                           e.removeFromWorld();
                        }
                        if (e.isType(BombermanType.WALL)) {
                            canExplodeHorizontal = false;
                        }
                        if (e.isType(BombermanType.ENEMY))
                        {
                            FXGL.getApp().getGameWorld().spawn("midExplodeX",xTopExplode,yTopExplode).setType(scale);
                            e.removeFromWorld();
                        }
                    });
        }
    }
    // thẳng đứng
    public void explodeVertical(double xCenterExplode, double yCenterExplode, double xTopExplode, double yTopExplode, int scale){
        if(canExplodeVertical) {
            FXGL.getApp()
                    .getGameWorld()
                    .getEntitiesAt(new Point2D(xTopExplode, yTopExplode))
                    .stream()
                    .forEach(e -> {
                        FXGL.getApp().getGameWorld().spawn("centerExplode", xCenterExplode, yCenterExplode);
                        if (e.isType(BombermanType.BRICK)) {
                            FXGL.getApp().getGameWorld().spawn("midExplodeY", xTopExplode, yTopExplode).setScaleY(scale);
                            FXGL.getApp().getGameWorld().spawn("Grass", xTopExplode, yTopExplode);
                            FXGL.<Bomberman>getAppCast().onWallDestroyed(e);
                            e.removeFromWorld();
                        }
                        if (e.isType(BombermanType.GRASS)) {
                          //  System.out.println("grass");
                            FXGL.getApp().getGameWorld().spawn("midExplodeY", xTopExplode, yTopExplode).setScaleY(scale);
                        }
                        if (e.isType(BombermanType.WALL)) {
                           // System.out.println("wall");
                            canExplodeVertical = false;
                        }
                        if (e.isType(BombermanType.ENEMY))
                        {
                            FXGL.getApp().getGameWorld().spawn("midExplodeY", xTopExplode, yTopExplode).setScaleY(scale);
                            e.removeFromWorld();
                        }
                    });
        }
    }
    public void explode(double x, double y){
            for (int i = 0; i < getPowerUp(); i++) {
                size_bomb += Game.SIZE_TILES;
                    explodeHorizontal(x, y, x + size_bomb, y, 1);
                    explodeHorizontal(x, y, x - size_bomb, y, -1);
                    explodeVertical(x, y, x, y - size_bomb,1);
                    explodeVertical(x, y, x, y + size_bomb, -1);
            }

        getEntity().removeFromWorld();// xóa boom khi nổ
    }


}
