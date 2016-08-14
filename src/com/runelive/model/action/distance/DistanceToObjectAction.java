package com.runelive.model.action.distance;

import com.runelive.executable.Executable;
import com.runelive.model.GameObject;
import com.runelive.model.action.PlayerAction;
import com.runelive.model.definitions.GameObjectDefinition;
import com.runelive.world.clip.region.Region;
import com.runelive.world.entity.impl.player.Player;

public final class DistanceToObjectAction extends PlayerAction {
    private final GameObject gameObject;
    private final int action;
    private final int objectType;
    private final int rotation;
    private final int minimumX;
    private final int minimumY;
    private  int maximumX;
    private  int maximumY;
    private final int mask;
    boolean preSkip = false;
    boolean nextTo = false;

    public DistanceToObjectAction(Player player, GameObject gameObject, int action) {
        super(player);
        this.gameObject = gameObject;
        this.action = action;
        this.minimumX = gameObject.getPosition().getX();
        this.minimumY = gameObject.getPosition().getY();
        int type = gameObject.getType();
        int rotation = gameObject.getRotation();
        if (type == 10 || type == 11 || type == 22) {
            this.maximumX = (minimumX + gameObject.getLengthX()) - 1;
            this.maximumY = (minimumY + gameObject.getLengthY()) - 1;
            this.objectType = 0;
            this.rotation = 0;
            int mask = GameObjectDefinition.forId(gameObject.getId()).anInt768;
            if (rotation != 0) {
                mask = (mask << rotation & 0xF) + (mask >> 4 - rotation);
            }
            this.mask = mask;
        } else {
            this.maximumX = -1;
            this.maximumY = -1;
            this.objectType = type + 1;
            this.rotation = rotation;
            this.mask = 0;
        }
        nextTo = player.distance(gameObject) <= 1;
    }

    @Override
    public ActionPolicy getActionPolicy() {
        return ActionPolicy.CLEAR;
    }

    private boolean reached() {
        if (objectType != 0) {
            if (objectType < 5 || objectType == 10) {
                if (Region.method219(player.getPosition(), minimumX, minimumY, objectType - 1, rotation)) {
                    return true;
                }
            }
            if (objectType < 10) {
                if (Region.method220(player.getPosition(), minimumX, minimumY, objectType - 1, rotation)) {
                    return true;
                }
            }
        } else {
            if (Region.reached(player.getPosition(), minimumX, minimumY, maximumX, maximumY, mask)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int execute() {
        if (!this.reached() && !(gameObject.getId() == 10596 && player.getPosition().getY() == 9562)) {
            return 1;
        }
        if(!preSkip) {
            player.setPositionToFace(gameObject.getPosition());
            preSkip = true;
            return !nextTo ? 2 : 1;
        }
        switch (action) {
            case 1:
                player.getActions().firstClickObject(gameObject);
                return Executable.STOP;
            case 2:
                player.getActions().secondClickObject(gameObject);
                return Executable.STOP;
            case 3:
                player.getActions().thirdClickObject(gameObject);
                return Executable.STOP;
            case 4:
                player.getActions().fourthClickObject(gameObject);
                return Executable.STOP;
            case 5:
                player.getActions().fifthClickObject(gameObject);
                return Executable.STOP;
            /*case 6: //item on object
                return Executable.STOP;*/
        }
        return Executable.STOP;
    }
}