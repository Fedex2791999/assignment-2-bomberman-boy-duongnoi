package uet.vteam.bomberman;

import com.almasb.fxgl.ai.AIControl;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.util.Duration;
import uet.vteam.bomberman.entities.character.Bomber.Bomb;
import uet.vteam.bomberman.entities.character.Bomber.Bomber;
import uet.vteam.bomberman.entities.character.Enemy.AiMoving;
import uet.vteam.bomberman.entities.character.Enemy.BossMoveControl;


import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static com.almasb.fxgl.app.DSLKt.texture;


public class GameFactory implements TextEntityFactory {

    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return Entities.builder()
                .at(0, 0)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("grass.png", Game.WIDTH , Game.HEIGHT))
                .renderLayer(RenderLayer.BACKGROUND)
                .build();
    }
    @SpawnSymbol('#')
    public Entity wall(SpawnData data) {
        return Entities.builder()
                .type(BombermanType.WALL)
                .from(data)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("rock.png", Game.SIZE_TILES , Game.SIZE_TILES ))
                .build();
    }

    @SpawnSymbol('*')
    public Entity brick(SpawnData data) {
        return Entities.builder()
                .type(BombermanType.BRICK)
                .from(data)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("brick.png", Game.SIZE_TILES , Game.SIZE_TILES ))
                .build();
    }

    @SpawnSymbol(' ')
    public Entity grass(SpawnData data) {
        return Entities.builder()
                .type(BombermanType.GRASS)
                .from(data)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("grass.png", Game.SIZE_TILES , Game.SIZE_TILES ))
                .build();
    }
  private Supplier<String> trees = new Supplier<String>() {
      private int index = 0;

      private List<String> names = Arrays.asList("guard.tree", "astar.tree", "chaser.tree", "random.tree");

      @Override
      public String get() {
          if (index == names.size()) {
              index = 0;
          }

          return names.get(index++);
      }
  };
   /* @SpawnSymbol('2')
    public Entity newEnemy1(SpawnData data){
        String ainame = trees.get();
        Entity enemy =  Entities.builder()
                .from(data)
                .type(BombermanType.ENEMY)
                .bbox(new HitBox(BoundingShape.box(40,40)))
                .renderLayer(RenderLayer.TOP)// Hiên AI trên cỏ , ko bị mất hình
                //   .bbox(new HitBox(BoundingShape.box(Game.SIZE_TILES, Game.SIZE_TILES)))
                // .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("enemy.png", Game.SIZE_TILES , Game.SIZE_TILES ))
                .with(new CollidableComponent(true))
                .with(new AIControl(ainame),new AiImageController(texture("enemy.png")),new BossMoveControl())
                .build();
        if (ainame.equals("guard.tree")) {
            enemy.removeComponent(AiMoving.class);
        }
        return enemy;
    }*/
  // enemy random
       @SpawnSymbol('1')
    public Entity newEnemy2(SpawnData data){
            Entity enemy =  Entities.builder()
                .from(data)
                .type(BombermanType.ENEMY)
                .bbox(new HitBox(BoundingShape.box(40,40)))
                .renderLayer(RenderLayer.TOP)// Hiên AI trên cỏ , ko bị mất hình
                 .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("enemy.png", Game.SIZE_TILES , Game.SIZE_TILES ))
                .with(new CollidableComponent(true))
                .with( new AiMoving() )
                .build();

        return enemy;
    }



    @Spawns("Powerup Bomb")
    public Entity powerUpBomb(SpawnData data) {
        return Entities.builder()
                .type(BombermanType.ITEM)
                .from(data)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("addflame.png", Game.SIZE_TILES , Game.SIZE_TILES ))
                .with(new CollidableComponent(true))
                .build();
    }
    @Spawns("IncreaseBomb")
    public Entity increaseBomb(SpawnData data) {
        return Entities.builder()
                .type(BombermanType.INCREASEBOMB)
                .from(data)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("addboom.png", Game.SIZE_TILES , Game.SIZE_TILES ))
                .with(new CollidableComponent(true))
                .build();
    }
    @Spawns("IncreaseSpeed")
    public Entity increaseSpeed(SpawnData data) {
        return Entities.builder()
                .type(BombermanType.INCREASESPEED)
                .from(data)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("speed.png", Game.SIZE_TILES , Game.SIZE_TILES ))
                .with(new CollidableComponent(true))
                .build();
    }
  // load player
    @Spawns("Player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);
        return Entities.builder()
                .type(BombermanType.PLAYER)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(40, 40)))
                //.with(physics)
                .with(new CollidableComponent(true))
                .with(new Bomber())// tạo ảnh nhân vật
                .build();

    }
    // load place bomb
    @Spawns("Bomb")
    public  Entity loadBomb(SpawnData data)
    {
        return Entities.builder()
                .type(BombermanType.BOMB)
                .from(data)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("ball-bomb.png",Game.SIZE_TILES,Game.SIZE_TILES))
                .with(new Bomb(data.get("flameItem")))
                .with(new CollidableComponent(true))
                .build();
    }
    @Spawns("Grass")
    public Entity newGrass(SpawnData data) {
        return Entities.builder()
                .type(BombermanType.GRASS)
                .from(data)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("grass.png", Game.SIZE_TILES , Game.SIZE_TILES ))
                .renderLayer(RenderLayer.BACKGROUND).build();
    }

    // load bomb explode
@Spawns("centerExplode")
public Entity newCenterExploe(SpawnData data){
    return Entities.builder()
            .type(BombermanType.EXPLODE)
            .from (data)
            .bbox(new HitBox(BoundingShape.box(Game.SIZE_TILES, Game.SIZE_TILES)))
            // .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("centerExplode1.png",40,40))
            .viewFromAnimatedTexture(texture("centerExplode1.png", 40, 40).toAnimatedTexture(1, Duration.seconds(0.3)), false, true)
            .with(new CollidableComponent(true))
            .build();
}

    @Spawns("topExplodeX")
    public Entity newTopExploeX(SpawnData data){
        return Entities.builder()
                .type(BombermanType.EXPLODE)
                .from (data)
                .bbox(new HitBox(BoundingShape.box(Game.SIZE_TILES, Game.SIZE_TILES)))
                // .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("topExplode1.png",40,40))
                .viewFromAnimatedTexture(texture("topExplode1.png", 40, 40).toAnimatedTexture(1, Duration.seconds(0.3)), false, true)
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("topExplodeY")
    public Entity newTopExploeY(SpawnData data){
        return Entities.builder()
                .type(BombermanType.EXPLODE)
                .from (data)
                .bbox(new HitBox(BoundingShape.box(Game.SIZE_TILES, Game.SIZE_TILES)))
                //.viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("topExplode2.png",40,40))
                .viewFromAnimatedTexture(texture("topExplode2.png", 40, 40).toAnimatedTexture(1, Duration.seconds(0.3)), false, true)
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("midExplodeX")
    public Entity newMidExploeX(SpawnData data){
        return Entities.builder()
                .type(BombermanType.EXPLODE)
                .from (data)
                .bbox(new HitBox(BoundingShape.box(Game.SIZE_TILES, Game.SIZE_TILES)))
                // .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("midExplode1.png",40,40))
                .viewFromAnimatedTexture(texture("midExplode1.png", 40, 40).toAnimatedTexture(1, Duration.seconds(0.3)), false, true)
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("midExplodeY")
    public Entity newMidExploeY(SpawnData data){
        return Entities.builder()
                .type(BombermanType.EXPLODE)
                .from (data)
                .bbox(new HitBox(BoundingShape.box(Game.SIZE_TILES, Game.SIZE_TILES)))
                //  .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("midExplode2.png",40,40))
                .viewFromAnimatedTexture(texture("midExplode2.png", 40, 40).toAnimatedTexture(1, Duration.seconds(0.3)), false, true)
                .with(new CollidableComponent(true))
                .build();
    }

    @Override
    public char emptyChar() {
        return ' ';
    }

    @Override
    public int blockWidth() {
        return Game.SIZE_TILES;
    }

    @Override
    public int blockHeight() {
        return Game.SIZE_TILES;
    }
}