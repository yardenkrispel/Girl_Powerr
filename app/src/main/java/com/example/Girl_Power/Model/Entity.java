package com.example.Girl_Power.Model;

public class Entity {
    private EntityType entityType;
    private final int[] cords;

    public  Entity(){
        cords = new int[2];
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Entity setEntityType(EntityType role) {
        this.entityType = role;
        return this;
    }

    public int[] getCords() {
        return cords;
    }

    public Entity setCords(int x, int y) {
        cords[0] = x;
        cords[1] = y;
        return this;
    }
}
