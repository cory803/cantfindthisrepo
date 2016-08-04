/**
 * @author Dalton Harriman (Palidino/Palidino76)
 */
package com.runelive.world.clip.region;

import com.runelive.GameServer;
import com.runelive.cache.Container;
import com.runelive.model.Direction;
import com.runelive.model.GameObject;
import com.runelive.model.Position;
import com.runelive.model.definitions.GameObjectDefinition;
import com.runelive.util.ByteStream;
import com.runelive.world.World;
import com.runelive.world.entity.impl.Character;
import com.runelive.world.entity.impl.npc.NPC;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.GZIPInputStream;

public final class Region {

    public static final int NORTH_WEST_BLOCKED = 0x1;
    public static final int NORTH_BLOCKED = 0x2;
    public static final int NORTH_EAST_BLOCKED = 0x4;
    public static final int EAST_BLOCKED = 0x8;
    public static final int SOUTH_EAST_BLOCKED = 0x10;
    public static final int SOUTH_BLOCKED = 0x20;
    public static final int SOUTH_WEST_BLOCKED = 0x40;
    public static final int WEST_BLOCKED = 0x80;
    public static final int TILE_BLOCKED = 0x100;
    public static final int PROJECTILE_NORTH_WEST_BLOCKED = 0x200;
    public static final int PROJECTILE_NORTH_BLOCKED = 0x400;
    public static final int PROJECTILE_NORTH_EAST_BLOCKED = 0x800;
    public static final int PROJECTILE_EAST_BLOCKED = 0x1000;
    public static final int PROJECTILE_SOUTH_EAST_BLOCKED = 0x2000;
    public static final int PROJECTILE_SOUTH_BLOCKED = 0x4000;
    public static final int PROJECTILE_SOUTH_WEST_BLOCKED = 0x8000;
    public static final int PROJECTILE_WEST_BLOCKED = 0x10000;
    public static final int PROJECTILE_TILE_BLOCKED = 0x20000;
    public static final int UNKNOWN = 0x80000;
    public static final int BLOCKED_TILE = 0x200000;
    public static final int UNLOADED_TILE = 0x1000000;
    private static final ArrayList<GameObject> customObjects = new ArrayList<>();
    private static final ArrayList<Integer> loadedRegions = new ArrayList<>();


    public static GameObject getCustomObject(Position p) {
        for(int i = 0; i < customObjects.size(); i++) {
            if(customObjects.get(i).getPosition() == p) {
                return customObjects.get(i);
            }
        }
        return null;
    }

    public static boolean hasCustomObject(Position p) {
        return getCustomObject(p) != null;
    }

    public static void addCustomObject(GameObject object) {
        if (object == null) {
            new Throwable().printStackTrace();
            return;
        }
        Region.customObjects.add(object);
    }

    public static void removeCustomObject(GameObject object) {
        Region.customObjects.remove(object);
    }

    /**
     * Check if the monster can move to the position.
     * @param startX The starting position.
     * @param startY The starting Y position.
     * @param endX The ending X position.
     * @param endY The ending Y position.
     * @param height The height of this check.
     * @param size The size of the attacking npcs.
     * @return if the entity can move.
     */
    public static boolean canMove(int startX, int startY, int endX, int endY, int height, int size) {
        int diffX = endX - startX;
        int diffY = endY - startY;
        height %= 4;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = endX - diffX;
            int currentY = endY - diffY;

            Direction dir = Direction.direction(diffX, diffY);
            if (World.directionBlocked(dir, height, currentX, currentY, size)) {
                return false;
            }

            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++;
            } else if (diffY > 0) {
                diffY--;
            }
        }
        return true;
    }

    /**
     * This method is the easy method.
     * @param startX The starting X position.
     * @param startY The starting Y position.
     * @param endX The ending X position.
     * @param endY The ending Y position.
     * @param height The height of this projectile.
     * @param size The size of the attacking npcs.
     * @return If a projectile can travel.
     */
    public static boolean canProjectileMove(int startX, int startY, int endX, int endY, int height, int size) {
        int diffX = endX - startX;
        int diffY = endY - startY;
        height %= 4;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = endX - diffX;
            int currentY = endY - diffY;
            Direction dir = Direction.direction(diffX, diffY);
            if (World.projectileDirectionBlocked(dir, height, currentX, currentY, size == 0 ? 1 : size)) {
                return false;
            }

            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++; // change
            } else if (diffY > 0) {
                diffY--;
            }
        }
        return true;
    }

    public static boolean canMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
        int diffX = endX - startX;
        int diffY = endY - startY;
        height %= 4;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = endX - diffX;
            int currentY = endY - diffY;
            for (int i = 0; i < xLength; i++) {
                for (int i2 = 0; i2 < yLength; i2++) {
                    if (diffX < 0 && diffY < 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.EAST_BLOCKED
                                | Region.NORTH_EAST_BLOCKED | Region.NORTH_BLOCKED)) != 0
                                || (World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY > 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.WEST_BLOCKED
                                | Region.SOUTH_WEST_BLOCKED | Region.SOUTH_BLOCKED)) != 0
                                || (World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY > 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.SOUTH_BLOCKED
                                | Region.SOUTH_EAST_BLOCKED | Region.EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY < 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.WEST_BLOCKED
                                | Region.NORTH_BLOCKED | Region.NORTH_WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY == 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.WEST_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY == 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.EAST_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY > 0) {
                        if ((World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY < 0) {
                        if ((World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | Region.BLOCKED_TILE | Region.UNKNOWN | Region.TILE_BLOCKED | Region.NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    }
                }
            }
            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++;
            } else if (diffY > 0) {
                diffY--;
            }
        }
        return true;
    }

    public static boolean canProjectileMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
        int diffX = endX - startX;
        int diffY = endY - startY;
        height %= 4;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = endX - diffX;
            int currentY = endY - diffY;
            for (int i = 0; i < xLength; i++) {
                for (int i2 = 0; i2 < yLength; i2++) {
                    if (diffX < 0 && diffY < 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | /*
																												 * BLOCKED_TILE
																												 * |
																												 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_EAST_BLOCKED
                                | Region.PROJECTILE_NORTH_EAST_BLOCKED | Region.PROJECTILE_NORTH_BLOCKED)) != 0
                                || (World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY > 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | /*
																												 * BLOCKED_TILE
																												 * |
																												 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_WEST_BLOCKED
                                | Region.PROJECTILE_SOUTH_WEST_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED)) != 0
                                || (World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY > 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | /*
																												 * BLOCKED_TILE
																												 * |
																												 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED
                                | Region.PROJECTILE_SOUTH_EAST_BLOCKED | Region.PROJECTILE_EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY < 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | /*
																												 * BLOCKED_TILE
																												 * |
																												 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_WEST_BLOCKED
                                | Region.PROJECTILE_NORTH_BLOCKED | Region.PROJECTILE_NORTH_WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | /*
																													 * BLOCKED_TILE
																													 * |
																													 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY == 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																											 * BLOCKED_TILE
																											 * |
																											 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_WEST_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY == 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2, height) & (Region.UNLOADED_TILE | /*
																											 * BLOCKED_TILE
																											 * |
																											 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_EAST_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY > 0) {
                        if ((World.getMask(currentX + i, currentY + i2 + 1, height) & (Region.UNLOADED_TILE | /*
																											 * BLOCKED_TILE
																											 * |
																											 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY < 0) {
                        if ((World.getMask(currentX + i, currentY + i2 - 1, height) & (Region.UNLOADED_TILE | /*
																											 * BLOCKED_TILE
																											 * |
																											 */Region.UNKNOWN | Region.PROJECTILE_TILE_BLOCKED | Region.PROJECTILE_NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    }
                }
            }
            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++; // change
            } else if (diffY > 0) {
                diffY--;
            }
        }
        return true;
    }

    /*public static void displayObjects(Player player) {
        for (GameObject o : Region.customObjects) {
            if (player.getLastKnownRegion().withinRegionNoHeight(o.getPosition()) && (player.usingDungeoneering() || o.getZ() % 4 == player.getHeight() % 4)) {// were gonna do this because we reUse static regions currently.
                player.getPA().object(o.getId(), o.getX(), o.getY(), o.getRotation(), o.getType());
            }
        }
    }*/

    static final int[] ignores = {
            3782, 5250, 5249, 5248
    };

    public static boolean canAttack(Character a, Character b) {
        if (a.getPosition().getZ() != b.getPosition().getZ()) {
            return false;
        }
        if (!a.isPlayer()) {
            NPC n = (NPC) a;
            for (int i : Region.ignores) {
                if (n.getId() == i) {
                    return true;
                }
            }
        }
        if (!b.isPlayer()) {
            NPC n = (NPC) b;
            for (int i : Region.ignores) {
                if (n.getId() == i) {
                    return true;
                }
            }
        }
        return Region.canMove(a.getPosition().getX(), a.getPosition().getY(), b.getPosition().getX(), b.getPosition().getY(), a.getPosition().getZ(), 1);
    }

    public static boolean canAttack(Position a, Character b) {
        if (!b.isPlayer()) {
            NPC n = (NPC) b;
            for (int i : Region.ignores) {
                return true;
            }
        }
        return Region.canMove(a.getX(), a.getY(), b.getPosition().getX(), b.getPosition().getY(), a.getZ(), 1);
    }

    public static boolean canMagicAttack(Character a2, Character b2) {
        if (!a2.isPlayer()) {
            NPC n = (NPC) a2;
            for (int i : Region.ignores) {
                if (n.getId() == i) {
                    return true;
                }
            }
        }
        if (!b2.isPlayer()) {
            NPC n = (NPC) b2;
            for (int i : Region.ignores) {
                if (n.getId() == i) {
                    return true;
                }
            }
        }
        return (Region.canProjectileMove(a2.getPosition().getX(), a2.getPosition().getY(), b2.getPosition().getX(), b2.getPosition().getY(), a2.getPosition().getZ(), a2.getSize() > 1 ? a2.getSize() / 2 : 1) ||
                Region.canProjectileMove(b2.getPosition().getX(), b2.getPosition().getY(), a2.getPosition().getX(), a2.getPosition().getY(), b2.getPosition().getZ(), a2.getSize() > 1 ? a2.getSize() / 2 : 1));
    }

    public static boolean method219(Position position, int destX, int destY, int objectType, int rotation) {
        int srcX = position.getX();
        int srcY = position.getY();
        if (srcX == destX && srcY == destY) {
            return true;
        }
        int z = position.getZ() % 4;
        switch (objectType) {
            case 0: {
                switch (rotation) {
                    case 0: {
                        if (srcX == destX - 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & (Region.SOUTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & (Region.NORTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 1: {
                        if (srcX == destX && srcY == destY + 1) {
                            return true;
                        }
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.EAST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.WEST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 2: {
                        if (srcX == destX + 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & (Region.SOUTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & (Region.NORTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 3: {
                        if (srcX == destX && srcY == destY - 1) {
                            return true;
                        }
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.EAST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.WEST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                }
                return false;
                // throw new AssertionError();
            }
            case 2: {
                switch (rotation) {
                    case 0: {
                        if (srcX == destX - 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.WEST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & (Region.NORTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 1: {
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.EAST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & (Region.NORTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 2: {
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.EAST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & (Region.SOUTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1) {
                            return true;
                        }
                        return false;
                    }
                    case 3: {
                        if (srcX == destX - 1 && srcY == destY) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & (Region.SOUTH_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & (Region.WEST_BLOCKED | Region.TILE_BLOCKED)) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1) {
                            return true;
                        }
                        return false;
                    }
                }
                return false;
                // throw new AssertionError();
            }
            case 9: {
                if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static boolean method220(Position position, int destX, int destY, int objectType, int rotation) {
        int srcX = position.getX();
        int srcY = position.getY();
        if (srcX == destX && srcY == destY) {
            return true;
        }
        int z = position.getZ() % 4;
        switch (objectType) {
            case 4:
                //System.out.println("here");
                if (srcX + 1 == destX || srcY + 1 == destY || srcX - 1 == destX || srcY - 1 == destY) {
                    return true;
                }
            case 6:
            case 7: {
                if (objectType == 7) {
                    rotation = (rotation + 2) & 3;
                }
                switch (rotation) {
                    case 0: {
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 1: {
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 2: {
                        if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0) {
                            return true;
                        }
                        return false;
                    }
                    case 3: {
                        if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0) {
                            return true;
                        }
                        if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0) {
                            return true;
                        }
                        return false;
                    }
                }
                return false;
                // throw new AssertionError();
            }
            case 8: {
                if (srcX == destX && srcY == destY + 1 && (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX && srcY == destY - 1 && (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX - 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0) {
                    return true;
                }
                if (srcX == destX + 1 && srcY == destY && (World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    static {
        //Region.printMasks(2370, 3133, 3);
    }

    public static boolean newReached(Position position, GameObject o, int minimumX, int minimumY, int maximumX, int maximumY, int mask) {
        int z = position.getZ() % 4;
        int srcX = position.getX();
        int srcY = position.getY();

        if (srcX == minimumX + o.getDefinition().getSizeX() && srcY >= minimumY && srcY <= maximumY &&
                ((World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0)) {
            return true;
        }
        if (srcX == minimumX - o.getDefinition().getSizeX()  && srcY >= minimumY && srcY <= maximumY &&
                (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0) {
            return true;
        }
        if (srcY == minimumY - o.getDefinition().getSizeY()  && srcX >= minimumX && srcX <= maximumX &&
                (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0) {
            return true;
        }
        if (srcY == minimumY + o.getDefinition().getSizeY() && srcX >= minimumX && srcX <= maximumX &&
                (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0) {
            return true;
        }
        return false;
    }

    public static boolean reached(Position position, int minimumX, int minimumY, int maximumX, int maximumY, int mask) {
        int z = position.getZ() % 4;
        int srcX = position.getX();
        int srcY = position.getY();

        if (srcX >= minimumX && srcX <= maximumX && srcY >= minimumY && srcY <= maximumY) {
            return true;
        }
        if (srcX == minimumX - 1 && srcY >= minimumY && srcY <= maximumY && (World.getMask(srcX, srcY, z) & Region.EAST_BLOCKED) == 0 && (mask & Region.EAST_BLOCKED) == 0) {
            return true;
        }
        if (srcX == maximumX + 1 && srcY >= minimumY && srcY <= maximumY && (World.getMask(srcX, srcY, z) & Region.WEST_BLOCKED) == 0 && (mask & Region.NORTH_BLOCKED) == 0) {
            return true;
        }
        if (srcY == minimumY - 1 && srcX >= minimumX && srcX <= maximumX && (World.getMask(srcX, srcY, z) & Region.NORTH_BLOCKED) == 0 && (mask & Region.NORTH_EAST_BLOCKED) == 0) {
            return true;
        }
        if (srcY == maximumY + 1 && srcX >= minimumX && srcX <= maximumX && (World.getMask(srcX, srcY, z) & Region.SOUTH_BLOCKED) == 0 && (mask & Region.NORTH_WEST_BLOCKED) == 0) {
            return true;
        }

        return false;
    }

    private boolean loaded;
    private final int area;
    private int[][][] collisionMasks = new int[4][64][64];
    private List<GameObject> objects = new LinkedList<>();

    public List<GameObject> getObjects() {
        return objects;
    }

    public Region(int area) {
        this.area = area;
    }

    public  int getMask(int x, int y, int z) {
        return collisionMasks[z][x % 64][y % 64];
    }

    public void flag(int x, int y, int z, int mask) {
        collisionMasks[z][x % 64][y % 64] |= mask;
    }

    public void unflag(int x, int y, int z, int mask) {
        collisionMasks[z][x % 64][y % 64] ^= mask;
    }

    public void remove(GameObject object) {
        objects.remove(object);
    }

    public void add(GameObject object) {
        objects.add(object);
    }

    public GameObject getObject(int id, int z, int x, int y) {
        for (GameObject object : objects) {
            if (id != object.getId()) {
                continue;
            }
            if (z == object.getPosition().getZ() && x == object.getPosition().getX() && y == object.getPosition().getY()) {
                return object;
            }
        }
        return null;
    }

    public GameObject getObject(Position position, int type) {
        for (GameObject object : objects) {
            if(object == null) {
                continue;
            }
            if (type != object.getType()) {
                continue;
            }
            if (position.equals(object.getPosition())) {
                return object;
            }
        }
        return null;
    }

    public GameObject getCustomObejct(Position position) {
        for (GameObject object : customObjects) {
            if(object == null) {
                continue;
            }
            if(position.equals(object.getPosition())) {
                return object;
            }
        }
        return null;
    }

    public GameObject getObject(Position position) {
        for (GameObject object : objects) {
            if(object == null) {
                continue;
            }
            if (position.equals(object.getPosition())) {
                return object;
            }
        }
        return null;
    }

    public Object[] getAllObjects(Position position) {
        ArrayList<GameObject> cachedObjects = new ArrayList<>();
        int size = 0;

        for (GameObject object : objects) {
            if (object == null) {
                continue;
            }
            if(position.equals(object.getPosition())) {
                cachedObjects.add(object);
            }
        }

        return cachedObjects.toArray();
    }

    static byte[] degzip(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        if (bytes.length == 0) {
            return bytes;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
            byte[] buffer = new byte[Byte.BYTES * 1024];

            while (true) {
                int read = gzip.read(buffer);
                if (read == -1) {
                    break;
                }

                os.write(buffer, 0, read);
            }
        }
        return os.toByteArray();
    }

    /**
     * Reads a 'smart' (either a {@code byte} or {@code short} depending on the
     * value) from the specified buffer.
     *
     * @param buffer
     *            The buffer.
     * @return The 'smart'.
     */
    // TODO: Move to a utility class
    public static int readSmart(ByteBuffer buffer) {
        int peek = buffer.get(buffer.position()) & 0xFF;
        if (peek < 128) {
            return buffer.get() & 0xFF;
        }
        return (buffer.getShort() & 0xFFFF) - 32768;
    }

    public static void init() throws IOException {

        class MapDefinition {
            private final int id;
            private final int objects;
            private final int terrain;

            public MapDefinition(int id, int objects, int terrain) {
                this.id = id;
                this.objects = objects;
                this.terrain = terrain;
            }

        }

        GameObjectDefinition.init();

        Path path = Paths.get("data", "clipping");

        // Decodes map definitions
        ByteBuffer buffer = ByteBuffer.wrap(Files.readAllBytes(path.resolve("map_index")));
        Map<Integer, MapDefinition> definitions = new HashMap<>();
        int size = buffer.getShort() & 0xFFFF;
        World.regions = new HashMap<>();
        for (int i = 0; i < size; i++) {
            int id = buffer.getShort() & 0xFFFF;
            int terrain = buffer.getShort() & 0xFFFF;
            int objects = buffer.getShort() & 0xFFFF;
            definitions.put(id, new MapDefinition(id, objects, terrain));
            World.regions.computeIfAbsent(id, Region::new);
            //put(id, new Region(id, terrain, objects));
        }

        // Decodes terrain and objects
        for (MapDefinition definition : definitions.values()) {
            int id = definition.id;
            int x = (id >> 8) * 64;
            int y = (id & 0xFF) * 64;

            byte[] objects = degzip(path.resolve("maps").resolve(definition.objects + ".gz"));
            byte[] terrain = degzip(path.resolve("maps").resolve(definition.terrain + ".gz"));

            if (objects.length == 0) {
                // System.out.println("Objects for region: [x, y, id, file] - ["
                // + x + ", " + y + ", " + id + ", " + definition.objects + "]
                // do not exist.");
                continue;
            }

            if (terrain.length == 0) {
                // System.out.println("Terrain for region: [x, y, id, file] - ["
                // + x + ", " + y + ", " + id + ", " + definition.terrain + "]
                // does not exist.");
                continue;
            }

            loadMaps(x, y, ByteBuffer.wrap(objects), ByteBuffer.wrap(terrain));
        }

        System.out.println("Loaded " + definitions.size() + " map definitions.");
    }

    private static void loadMaps(int absX, int absY, ByteBuffer objectStream, ByteBuffer groundStream) {
        byte[][][] heightMap = new byte[4][64][64];
        try {
            for (int z = 0; z < 4; z++) {
                for (int tileX = 0; tileX < 64; tileX++) {
                    for (int tileY = 0; tileY < 64; tileY++) {
                        while (true) {
                            int tileType = groundStream.get() & 0xFF;
                            if (tileType == 0) {
                                break;
                            } else if (tileType == 1) {
                                groundStream.get();
                                break;
                            } else if (tileType <= 49) {
                                groundStream.get();
                            } else if (tileType <= 81) {
                                heightMap[z][tileX][tileY] = (byte) (tileType - 49);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                for (int i2 = 0; i2 < 64; i2++) {
                    for (int i3 = 0; i3 < 64; i3++) {
                        if ((heightMap[i][i2][i3] & 1) == 1) {
                            int height = i;
                            if ((heightMap[1][i2][i3] & 2) == 2) {
                                height--;
                            }
                            if (height >= 0 && height <= 3) {
                                World.flag(absX + i2, absY + i3, height, 0x200000);
                            }
                        }
                    }
                }
            }
            int objectId = -1;
            int incr;
            while ((incr = readSmart(objectStream)) != 0) {
                objectId += incr;
                int location = 0;
                int incr2;
                while ((incr2 = readSmart(objectStream)) != 0) {
                    location += incr2 - 1;
                    int localX = location >> 6 & 0x3f;
                    int localY = location & 0x3f;
                    int height = location >> 12;
                    int objectData = objectStream.get() & 0xFF;
                    int type = objectData >> 2;
                    int direction = objectData & 0x3;
                    if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
                        continue;
                    }
                    if ((heightMap[1][localX][localY] & 2) == 2) {
                        height--;
                    }
                    if (height >= 0 && height <= 3)
                        World.addObject(objectId, absX + localX, absY + localY, height, type, direction); // Add
                    // object
                    // to
                    // clipping
                }
            }
        } catch (Exception cause) {
            System.out.println("Unable to load maps in region: " + ((((absX >> 3) / 8) << 8) + ((absY >> 3) / 8))
                    + " pos: " + absX + ", " + absY);
        }
    }

    public void load() {
        if (loaded) {
            return;
        }
        loaded = true;/*
        try {
            this.objects = new LinkedList<>();
            this.collisionMasks = new int[4][64][64];
            byte[] objectDataBytes = GameServer.cache.getFile(4, objectFile);
            byte[] groundDataBytes = GameServer.cache.getFile(4, groundFile);
            if (objectDataBytes == null || objectDataBytes.length == 0 || groundDataBytes == null || groundDataBytes.length == 0) {
                System.err.println("Could not get " + (objectDataBytes == null || objectDataBytes.length == 0 ? ("object file " + objectFile) : ("ground file " + groundFile)));
                return;
            }
            ByteStream objectData = new ByteStream(Container.unpack(objectDataBytes));
            ByteStream groundData = new ByteStream(Container.unpack(groundDataBytes));
            int regionX = (area >> 8) * 64;
            int regionY = (area & 0xFF) * 64;
            byte[][][] someArray = new byte[4][64][64];
            for (int i = 0; i < 4; i++) {
                for (int i2 = 0; i2 < 64; i2++) {
                    for (int i3 = 0; i3 < 64; i3++) {
                        while (true) {
                            int v = groundData.getUnsignedByte();
                            if (v == 0) {
                                break;
                            } else if (v == 1) {
                                groundData.skip();
                                break;
                            } else if (v <= 49) {
                                groundData.skip();
                            } else if (v <= 81) {
                                someArray[i][i2][i3] = (byte) (v - 49);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                for (int i2 = 0; i2 < 64; i2++) {
                    for (int i3 = 0; i3 < 64; i3++) {
                        if ((someArray[i][i2][i3] & 1) == 1) {
                            int z = i;
                            if ((someArray[1][i2][i3] & 2) == 2) {
                                z--;
                            }
                            if (z >= 0 && z <= 3) {
                                int realX = regionX + i2;
                                int realY = regionY + i3;
                                this.flag(realX, realY, z, Region.BLOCKED_TILE);
                            }
                        }
                    }
                }
            }
            int objectId = -1;
            int incr;
            while ((incr = objectData.getUSmart()) != 0) {
                objectId += incr;
                int location = 0;
                int incr2;
                while ((incr2 = objectData.getUSmart()) != 0) {
                    location += incr2 - 1;
                    int localX = location >> 6 & 0x3f;
                    int localY = location & 0x3f;
                    int height = location >> 12;
                    int info = objectData.getUnsignedByte();
                    int type = info >> 2;
                    int direction = info & 0x3;
                    if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
                        continue;
                    }
                    if ((someArray[1][localX][localY] & 2) == 2) {
                        height--;
                    }
                    if (height >= 0 && height <= 3) {
                        World.addObject(objectId, regionX + localX, regionY + localY, height, type, direction);
                    }
                }
            }

        } catch (Throwable e) {
            System.err.println("Corrupt map: g-" + groundFile + " o-" + objectFile + " loc-(" + ((area >> 8) * 64) + ", " + ((area & 0xFF) * 64) + ")");
            e.printStackTrace();
        }*/
    }

    public void removeFlag(int x, int y, int z) {
        collisionMasks[z][x % 64][y % 64] = 0;
    }
}