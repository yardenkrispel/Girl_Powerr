package com.example.Girl_Power.Logic;

import android.util.Log;

import com.example.Girl_Power.Model.Entity;
import com.example.Girl_Power.Model.EntityType;

import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    private final int cols;
    private final int rows;
    private final int lives;  // max lives
    private int hits;
    private final int interval; // interval between entities creation
    private int intervalCounter;
    private int score;
    private final Random rand = new Random();
    private ArrayList<Entity> entities; // holds coordinates of player and existing obstacles

    public GameManager(){
        this(3, 6, 3, 3);
    }

    public GameManager(int lives, int rows, int cols, int interval){
        this.cols = cols;
        this.rows = rows;
        this.lives = lives;
        this.hits = 0;
        this.score = 0;

        this.interval = interval;
        this.intervalCounter = 0;

        this.entities = new ArrayList<>();
        this.entities.add(new Entity()
                .setCords(rows-1, 2)
                .setEntityType(EntityType.PLAYER));  // create the player entity
    }

    // gets entities coordinates.
    // entities in this context are obstacles, rewards and lives
    public int[][] getEntitiesCords() {
        int[][] cords = new int[entities.size()][2];

        for (int i = 0; i < cords.length; i++){
            cords[i][0] = entities.get(i).getCords()[0];
            cords[i][1] = entities.get(i).getCords()[1];
        }
        return cords;
    }

    public EntityType[] getEntitiesType() {
        EntityType[] types = new EntityType[entities.size()];

        for (int i = 0; i < types.length; i++){
            types[i] = entities.get(i).getEntityType();
        }

        return types;
    }

    public int getHits() {
        return hits;
    }

    public int getScore() {
        return score;
    }

    public boolean isLost(){
        return lives == hits;
    }

    public void generateObstacle(){
        int random_number = rand.nextInt(15);
        EntityType entityType;
        //probability 1/15
        if (hits > 0 && random_number == 0)
            entityType = EntityType.LIFE;

        //probability 12/15
        else if (random_number <= 12)
            entityType = EntityType.OBSTACLE;

        //probability 2/15
        else
            entityType = EntityType.REWARD;

        entities.add(new Entity()
                .setCords(0, rand.nextInt(cols))
                .setEntityType(entityType));

        // reset counter for next obstacle
        intervalCounter = 0;
    }

    public int moveEntities(){
        score += 10;
        int isHit = 0; // 0-not hit, 1-hit obstacle,  2-hit reward, 3-hit life

        Entity player = entities.get(0);
        int[] playerCords = player.getCords();

        for (int i = 1; i < entities.size(); i++){

            Entity entity = entities.get(i);
            int[] entityCords = entity.getCords();
            EntityType entityType = entity.getEntityType();

            entityCords[0]++;

            //entity hits the player
            if (entityCords[0] == rows - 1 && entityCords[1] == playerCords[1]){
                // obstacle hits the player
                if (entityType == EntityType.OBSTACLE){
                    hits++;
                    isHit = 1;
                }
                // reward hits the player
                else if (entityType == EntityType.REWARD) {
                    score += 90;
                    isHit = 2;
                }
                // life hits the player
                else if (entityType == EntityType.LIFE && hits > 0){
                    hits--;
                    isHit = 3;
                }
            }
            //entity out of board
            else if (entityCords[0] == rows){
                entities.remove(i);
                i--;
            }
        }

        intervalCounter++;
        if (intervalCounter == interval)
            generateObstacle();

        return isHit;
    }



    public void movePlayerRight(){
        Log.d("game manger", "movePlayerRight");

        if (entities.get(0).getCords()[1] < cols - 1)
            entities.get(0).getCords()[1]++;
    }

    public void movePlayerLeft(){
        Log.d("game manger", "movePlayerleft");

        if (entities.get(0).getCords()[1] > 0)
            entities.get(0).getCords()[1]--;
    }

    public void resetGame(){
        this.hits = 0;
        this.intervalCounter = 0;
        score = 0;
        this.entities = new ArrayList<>();
        this.entities.add(new Entity()
                .setCords(rows-1, 1)
                .setEntityType(EntityType.PLAYER));
    }
}
