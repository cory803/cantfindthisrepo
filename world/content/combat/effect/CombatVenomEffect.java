package com.ikov.world.content.combat.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.ikov.engine.task.Task;
import com.ikov.model.CombatIcon;
import com.ikov.model.Hit;
import com.ikov.model.Hitmask;
import com.ikov.model.Item;
import com.ikov.world.entity.impl.Character;

/**
 * A {@link Task} implementation that handles the venom process.
 * 
 * @author lare96
 */
public class CombatVenomEffect extends Task {

    /** The entity being inflicted with venom. */
    private Character entity;

    /**
     * Create a new {@link CombatVenomEffect}.
     * 
     * @param entity
     *            the entity being inflicted with venom.
     */
    public CombatVenomEffect(Character entity) {
        super(33, entity, false);
        this.entity = entity;
    }

    /**
     * Holds all of the different strengths of venom.
     * 
     * @author lare96
     */
    public enum VenomType {
        MILD(50),
        EXTRA(70),
        SUPER(120);

        /** The starting damage for this venom type. */
        private int damage;

        /**
         * Create a new {@link VenomType}.
         * 
         * @param damage
         *            the starting damage for this venom type.
         */
        private VenomType(int damage) {
            this.damage = damage;
        }

        /**
         * Gets the starting damage for this venom type.
         * 
         * @return the starting damage for this venom type.
         */
        public int getDamage() {
            return damage;
        }
    }

    @Override
    public void execute() {

        // Stop the task if the entity is unregistered.
        if (!entity.isRegistered() || !entity.isVenomed()) {
            this.stop();
            return;
        }

        // Deal the damage, then try and decrement the damage count.
        entity.dealDamage(new Hit(entity.getAndDecrementVenomDamage(), Hitmask.DARK_PURPLE, CombatIcon.NONE));
       /* if(entity.isPlayer()) {
        	((Player)entity).getPacketSender().sendInterfaceRemoval();
        }*/
    }

    /**
     * The small utility class that manages all of the combat venom data.
     * 
     * @author lare96
     * @author Advocatus
     */
    public static final class CombatVenomData {

        /** The map of all of the different weapons that venom. */
        // Increase the capacity of the map as more elements are added.
        private static final Map<Integer, VenomType> types = new HashMap<>(97);

        /** Load all of the venom data. */
        public static void init() {
            types.put(817, VenomType.MILD);
            types.put(816, VenomType.MILD);
            types.put(818, VenomType.MILD);
            types.put(831, VenomType.MILD);
            types.put(812, VenomType.MILD);
            types.put(813, VenomType.MILD);
            types.put(814, VenomType.MILD);
            types.put(815, VenomType.MILD);
            types.put(883, VenomType.MILD);
            types.put(885, VenomType.MILD);
            types.put(887, VenomType.MILD);
            types.put(889, VenomType.MILD);
            types.put(891, VenomType.MILD);
            types.put(893, VenomType.MILD);
            types.put(870, VenomType.MILD);
            types.put(871, VenomType.MILD);
            types.put(872, VenomType.MILD);
            types.put(873, VenomType.MILD);
            types.put(874, VenomType.MILD);
            types.put(875, VenomType.MILD);
            types.put(876, VenomType.MILD);
            types.put(834, VenomType.MILD);
            types.put(835, VenomType.MILD);
            types.put(832, VenomType.MILD);
            types.put(833, VenomType.MILD);
            types.put(836, VenomType.MILD);
            types.put(1221, VenomType.MILD);
            types.put(1223, VenomType.MILD);
            types.put(1219, VenomType.MILD);
            types.put(1229, VenomType.MILD);
            types.put(1231, VenomType.MILD);
            types.put(1225, VenomType.MILD);
            types.put(1227, VenomType.MILD);
            types.put(1233, VenomType.MILD);
            types.put(1253, VenomType.MILD);
            types.put(1251, VenomType.MILD);
            types.put(1263, VenomType.MILD);
            types.put(1261, VenomType.MILD);
            types.put(1259, VenomType.MILD);
            types.put(1257, VenomType.MILD);
            types.put(3094, VenomType.MILD);

            types.put(5621, VenomType.EXTRA);
            types.put(5620, VenomType.EXTRA);
            types.put(5617, VenomType.EXTRA);
            types.put(5616, VenomType.EXTRA);
            types.put(5619, VenomType.EXTRA);
            types.put(5618, VenomType.EXTRA);
            types.put(5629, VenomType.EXTRA);
            types.put(5628, VenomType.EXTRA);
            types.put(5631, VenomType.EXTRA);
            types.put(5630, VenomType.EXTRA);
            types.put(5645, VenomType.EXTRA);
            types.put(5644, VenomType.EXTRA);
            types.put(5647, VenomType.EXTRA);
            types.put(5646, VenomType.EXTRA);
            types.put(5643, VenomType.EXTRA);
            types.put(5642, VenomType.EXTRA);
            types.put(5633, VenomType.EXTRA);
            types.put(5632, VenomType.EXTRA);
            types.put(5634, VenomType.EXTRA);
            types.put(5660, VenomType.EXTRA);
            types.put(5656, VenomType.EXTRA);
            types.put(5657, VenomType.EXTRA);
            types.put(5658, VenomType.EXTRA);
            types.put(5659, VenomType.EXTRA);
            types.put(5654, VenomType.EXTRA);
            types.put(5655, VenomType.EXTRA);
            types.put(5680, VenomType.EXTRA);

            types.put(5623, VenomType.SUPER);
            types.put(5622, VenomType.SUPER);
            types.put(5625, VenomType.SUPER);
            types.put(5624, VenomType.SUPER);
            types.put(5627, VenomType.SUPER);
            types.put(5626, VenomType.SUPER);
            types.put(5698, VenomType.SUPER);
            types.put(5730, VenomType.SUPER);
            types.put(5641, VenomType.SUPER);
            types.put(5640, VenomType.SUPER);
            types.put(5637, VenomType.SUPER);
            types.put(5636, VenomType.SUPER);
            types.put(5639, VenomType.SUPER);
            types.put(5638, VenomType.SUPER);
            types.put(5635, VenomType.SUPER);
            types.put(5661, VenomType.SUPER);
            types.put(5662, VenomType.SUPER);
            types.put(5663, VenomType.SUPER);
            types.put(5652, VenomType.SUPER);
            types.put(5653, VenomType.SUPER);
            types.put(5648, VenomType.SUPER);
            types.put(5649, VenomType.SUPER);
            types.put(5650, VenomType.SUPER);
            types.put(5651, VenomType.SUPER);
            types.put(5667, VenomType.SUPER);
            types.put(5666, VenomType.SUPER);
            types.put(5665, VenomType.SUPER);
            types.put(5664, VenomType.SUPER);
        }

        /**
         * Gets the venom type of the specified item.
         * 
         * @param item
         *            the item to get the venom type of.
         * 
         * @return the venom type of the specified item, or <code>null</code>
         *         if the item is not able to venom the victim.
         */
        public static Optional<VenomType> getVenomType(Item item) {
            if (item == null || item.getId() < 1 || item.getAmount() < 1)
                return Optional.empty();
            return Optional.ofNullable(types.get(item.getId()));
        }

        /** Default private constructor. */
        private CombatVenomData() {}
    }
}
