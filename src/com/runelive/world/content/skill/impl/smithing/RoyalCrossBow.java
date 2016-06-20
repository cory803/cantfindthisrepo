package com.runelive.world.content.skill.impl.smithing;

import com.runelive.model.Animation;
import com.runelive.model.Graphic;
import com.runelive.model.Skill;
import com.runelive.world.World;
import com.runelive.world.entity.impl.player.Player;

/**
 * Created by Dave on 20/06/2016.
 */
public class RoyalCrossBow {

    public static void makeCrossbow(Player player) {
        if(player.getInventory().containsAll(11620, 11621, 11622, 11623, 2347) && player.getSkillManager().getCurrentLevel(Skill.SMITHING) >= 99) {
            player.getInteractingObject().performGraphic(new Graphic(2123));
            player.performAnimation(new Animation(898));
            player.getInventory().delete(11620, 1);
            player.getInventory().delete(11621, 1);
            player.getInventory().delete(11622, 1);
            player.getInventory().delete(11623, 1);
            player.getInventory().add(11624, 1);
            World.sendMessage("<icon=0><shad=ff0000>News: "+player.getUsername()+" has just forged a Dragonbone crossbow!");
            player.getSkillManager().addExperience(Skill.SMITHING, 250_000);
        } else {
            player.getPacketSender().sendMessage("You need a smithing level of 99 to make a Royal Crossbow, a hammer & you also");
            player.getPacketSender().sendMessage("need the following components: Royal stabiliser, frame, sight & torsion spring.");
        }
    }
}
