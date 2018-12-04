package uet.vteam.bomberman.entities.character.Bomber;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.entity.components.TypeComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uet.vteam.bomberman.Bomberman;
import uet.vteam.bomberman.BombermanType;
import uet.vteam.bomberman.Game;
import uet.vteam.bomberman.entities.character.Bomber.BomberType;


public class Bomber extends Component {
    private static PositionComponent position;
    private static double speed = 0;
    private static double increaseSpeed = 0;
    private  int flameItem=1;
    private AnimatedTexture texture;
    private AnimationChannel bomberRight, bomberUp, bomberDown, bomberRightIdle, bomberUpIdle, bomberDownIdle;
    private BomberType type;
    public BomberType getType() {
        return type;
    }
    private static  int bombsPlaced = 0;
    private static int maxBombs = 3;// chưa đặt được nhiều bom
    public void setType(BomberType type) {
        this.type = type;
    }
    public static void increaseBomb()
    {
        if (maxBombs > 12)
        {
            maxBombs = 12;
        }
        maxBombs+=3;
    }
    public static void increaseSpeed()
    {
        if (increaseSpeed > 210)
        {
            increaseSpeed = 210;
        }
        increaseSpeed += 30;
    }

    public static int getMaxBomb()
    {
        return maxBombs;
    }
    public int getBombPlaced()
    {
        return bombsPlaced;
    }


    // cắt ảnh nhân vật , chưa tạo animation
    public Bomber(){
        bomberRight = new AnimationChannel("bomber_right.png", 8, 40, 40, Duration.seconds(2), 0, 7);
        bomberUp = new AnimationChannel("bomber_back.png", 8, 40,  40, Duration.seconds(2), 0, 7);
        bomberDown = new AnimationChannel("bomber_front.png", 8, 40, 40, Duration.seconds(2), 0, 7);
        bomberRightIdle = new AnimationChannel("bomber_right.png", 8, 40, 40, Duration.seconds(0.1), 0, 0);
        bomberUpIdle = new AnimationChannel("bomber_back.png", 8, 40, 40, Duration.seconds(0.1), 0, 0);
        bomberDownIdle = new AnimationChannel("bomber_front.png", 8, 40, 40, Duration.seconds(0.1), 0, 0);

        texture = new AnimatedTexture(bomberRight);

    }


    @Override
    public void onAdded() {
        entity.setView(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        // set position ví trí ban đầu là 1,1
        // getEntity().getY()  thuốc kiểu double, di chuyển trái phải ko thay đổi tạo độ

        int x = position.getGridX(Game.SIZE_TILES);
        int y = position.getGridY(Game.SIZE_TILES);
        if(speed != 0){

            if((getType() == BomberType.DOWN || getType() == BomberType.UP)
                    && (getEntity().getX() - x*Game.SIZE_TILES >= -10)
                    && (getEntity().getX() + getEntity().getWidth() - (x+1)*Game.SIZE_TILES <= 10)){

                if((-getEntity().getY() + y*Game.SIZE_TILES > 1) && getType() == BomberType.UP){
                    texture.loopAnimationChannel(bomberUp);
                    if(canMove(new Point2D(x*Game.SIZE_TILES, (y-1)*Game.SIZE_TILES))) {
                        entity.translateY(tpf * speed);
                    }
                }
                else if((-getEntity().getY() + y*Game.SIZE_TILES < 1) && getType() == BomberType.DOWN){
                    texture.loopAnimationChannel(bomberDown);
                    if(canMove(new Point2D(x*Game.SIZE_TILES, (y+1)*Game.SIZE_TILES))){
                        entity.translateY(tpf*speed);
                    }

                }
                else if(canMove(new Point2D(x*Game.SIZE_TILES, (y-1)*Game.SIZE_TILES)) || canMove(new Point2D(x*40, (y+1)*40))){
                    if(getType() == BomberType.UP){
                        texture.loopAnimationChannel(bomberUp);
                    }
                    else {
                        texture.loopAnimationChannel(bomberDown);
                    }

                    entity.translateY(tpf*speed);
                }
            }

            if((getType() == BomberType.LEFT || getType() == BomberType.RIGHT)
                    && (getEntity().getY() + getEntity().getHeight() - (y+1) * Game.SIZE_TILES <= 5)){

                if((-getEntity().getX() + x*Game.SIZE_TILES > 1)
                        && getType() == BomberType.LEFT){

                    texture.loopAnimationChannel(bomberRight);
                    if(canMove(new Point2D((x-1)*Game.SIZE_TILES, y*Game.SIZE_TILES))
                    ) {
                        entity.translateX(tpf * speed);
                    }
                }
                else if((-getEntity().getX() + x*Game.SIZE_TILES < 1)
                        && getType() == BomberType.RIGHT){

                    texture.loopAnimationChannel(bomberRight);

                    if(canMove(new Point2D((x+1)*Game.SIZE_TILES, y*Game.SIZE_TILES))){
                        entity.translateX(tpf*speed);
                    }

                }
                else if((canMove(new Point2D((x-1)*Game.SIZE_TILES, y*Game.SIZE_TILES))
                        || canMove(new Point2D((x+1)*Game.SIZE_TILES, y*Game.SIZE_TILES)))){
                    texture.loopAnimationChannel(bomberRight);
                    entity.translateX(tpf*speed);
                }
            }

        }
        speed = speed * 0.7;

        if(Math.abs(speed * tpf) < 1){
            if(getType() == BomberType.RIGHT || getType() == BomberType.LEFT){
                texture.loopAnimationChannel(bomberRightIdle);
            }
            else if (getType() == BomberType.UP){
                texture.loopAnimationChannel(bomberUpIdle);
            }
            else if(getType() == BomberType.DOWN){
                texture.loopAnimationChannel(bomberDownIdle);
            }
        }
    }

    public void moveRight(){
       // System.out.println(getEntity().getX());
        setType(BomberType.RIGHT);
        speed = 170 +increaseSpeed ;
        getEntity().setScaleX(1);
    }

    public void moveLeft(){
        //    System.out.println(getEntity().getX());
        setType(BomberType.LEFT);
        speed = -170 - increaseSpeed;
        getEntity().setScaleX(-1);
    }

    public void moveUp(){
      //  System.out.println(getEntity().getX());
        setType(BomberType.UP);
        speed = -170-increaseSpeed;
        getEntity().setScaleY(1);
    }

    public void moveDown(){
      // System.out.println(getEntity().getX());
        setType(BomberType.DOWN);
        speed = 170+increaseSpeed;
        getEntity().setScaleY(1);
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
                                || type.isType(BombermanType.WALL));
    }

    public void placeBomb() {
        if (bombsPlaced == maxBombs) {
            return;
        }
        bombsPlaced++;
        int x = position.getGridX(Game.SIZE_TILES);
        int y = position.getGridY(Game.SIZE_TILES);
        Entity bomb = FXGL.getApp()
                .getGameWorld()
                .spawn("Bomb", new SpawnData(x * Game.SIZE_TILES, y * Game.SIZE_TILES).put("flameItem",flameItem));
        FXGL.getMasterTimer().runOnceAfter(() -> {
            bomb.getComponent(Bomb.class).explode(x*Game.SIZE_TILES,y*Game.SIZE_TILES);
            bombsPlaced--;
            FXGL.getAudioPlayer().playSound("bomb_bang.wav");
        }, Duration.seconds(2));
       // System.out.println(getBombPlaced());
       // System.out.println("========");
    }

}