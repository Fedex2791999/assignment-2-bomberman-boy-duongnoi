package uet.vteam.bomberman;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.Level;
import com.almasb.fxgl.extra.ai.pathfinding.AStarGrid;
import com.almasb.fxgl.extra.physics.handlers.CollectibleHandler;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.parser.text.TextLevelParser;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;
import uet.vteam.bomberman.entities.character.Bomber.Bomb;
import uet.vteam.bomberman.entities.character.Bomber.Bomber;

import java.util.Random;


public class Bomberman extends GameApplication {

    private static Entity bomber;
    private static Bomber bomberControl;
    public void setBomber(Entity bomber) {
        this.bomber = bomber;
    }
    public static Entity getBomber() {
        return bomber;
    }
    private AStarGrid grid;

    public AStarGrid getGrid() {
        return grid;
    }
    public Entity getPlayer() {
        return getGameWorld().getSingleton(BombermanType.PLAYER).get();
    }
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle(Game.TITLE);
        settings.setVersion(Game.VERSION);
        settings.setWidth(Game.WIDTH);
        settings.setHeight(Game.HEIGHT);
        settings.setAppIcon("icon (1).png");
    }
    @Override
    protected void initGame() {

        GameFactory factory= new GameFactory();
        getGameWorld().addEntityFactory(factory);
        TextLevelParser levelParser = new TextLevelParser(factory);
        Level level = levelParser.parse("levels/Level1.txt");
        getGameWorld().setLevel(level);
        getGameWorld().spawn("BG");
        bomber = getGameWorld().spawn("Player", Game.SIZE_TILES , Game.SIZE_TILES );// khởi tạo vị trí ban đầu
        bomberControl = bomber.getComponent(Bomber.class);
        // di chuyển màn hình
     //   getGameScene().getViewport().setBounds(-2000,0, 2000, getWidth());
      //  getGameScene().getViewport().bindToEntity(bomber, getWidth()/3, getHeight()/3);

    }
    @Override
    protected void initPhysics() {
        // collition powerUp
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BombermanType.PLAYER, BombermanType.INCREASEBOMB) {
            @Override
            protected void onCollisionBegin(Entity pl, Entity increaseBomb) {
                FXGL.getAudioPlayer().playSound("increase boom.wav");
                increaseBomb.removeFromWorld();
                Bomber.increaseBomb();
              //  System.out.println(Bomber.getMaxBomb());
            }
        });
        //test2 Collision
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BombermanType.PLAYER, BombermanType.ITEM) {
            @Override
            protected void onCollisionBegin(Entity pl, Entity powerup) {
                FXGL.getAudioPlayer().playSound("flame.wav");
                powerup.removeFromWorld();
                Bomb.increasePowerUp();
              //  System.out.println("test");
               // System.out.println(Bomb.getPowerUp());
            }
        });
        // collision speed
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BombermanType.PLAYER, BombermanType.INCREASESPEED) {
            @Override
            protected void onCollisionBegin(Entity pl, Entity increaseSpeed) {
                FXGL.getAudioPlayer().playSound("item Speed.wav");
                increaseSpeed.removeFromWorld();
                Bomber.increaseSpeed();
                //  System.out.println("test");
                // System.out.println(Bomb.getPowerUp());
            }
        });
        // flame collision enemy
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BombermanType.ENEMY, BombermanType.EXPLODE) {
            @Override
            protected void onCollisionBegin(Entity enemy , Entity explode ) {
                enemy.removeFromWorld();
                //  System.out.println("test");
                // System.out.println(Bomb.getPowerUp());
            }
        });
        // flame collision player
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BombermanType.PLAYER, BombermanType.EXPLODE) {
            @Override
            protected void onCollisionBegin(Entity player , Entity explode ) {
                FXGL.getAudioPlayer().playSound("sfx_hit.wav");
                player.removeFromWorld();
                startNewGame();
            }
        });
        // player collision enemy
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BombermanType.PLAYER,BombermanType.ENEMY){
            @Override
            protected  void onCollision(Entity player, Entity enemy)
            {
                FXGL.getAudioPlayer().playSound("sfx_hit.wav");
                startNewGame();
            }

        });

    }
    public void onWallDestroyed(Entity wall) {
        if (FXGLMath.randomBoolean()) {
            int x = wall.getPositionComponent().getGridX(Game.SIZE_TILES);
            int y = wall.getPositionComponent().getGridY(Game.SIZE_TILES);
            Random rand = new Random();
            int rand_int = rand.nextInt(3);
            if (rand_int == 0) getGameWorld().spawn("Powerup Bomb", x * 40, y * 40);
            else if (rand_int == 1) getGameWorld().spawn("IncreaseBomb", x * 40, y * 40);
            else getGameWorld().spawn("IncreaseSpeed", x * 40, y * 40);
            //getGameWorld().spawn("IncreaseBomb", x * 40, y * 40);
            //getGameWorld().spawn("IncreaseSpeed", x * 40, y * 40);
        }
    }
    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                bomberControl.moveUp();
            }
        }, KeyCode.UP);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                bomberControl.moveLeft();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                bomberControl.moveDown();
            }
        }, KeyCode.DOWN);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                bomberControl.moveRight();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Place Bomb") {
            @Override
            protected void onAction() {
                bomberControl.placeBomb();
            }
        }, KeyCode.SPACE);
    }

    @Override
    protected void initUI(){

    }
    @Override
    protected void preInit()
    {
        getAudioPlayer().loopBGM("01_Title Screen.wav");
    }

    public static void main(String[] args) {
        //FXGL.getAudioPlayer().playSound("Bùa Yêu.mp3");
        launch(args);
    }
}