package com.runelive.model.movement;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

import com.runelive.model.Position;
import com.runelive.world.clip.region.RegionClipping;
import com.runelive.world.entity.impl.Character;

public class PathFinder {

  public static boolean findPath(Character character, int destX, int destY, boolean close,
      int width, int length) {
    Deque<Position> positions =
        find(character.getPosition(), new Position(destX, destY), close, width, length);
    if (positions.isEmpty()) {
      return false;
    }

    Position position = positions.poll();
    character.getMovementQueue().addFirstStep(position);

    while ((position = positions.poll()) != null) {
      character.getMovementQueue().addStep(position);
    }

    return true;
  }

  public static Deque<Position> find(Position start, Position end, boolean close, int width,
      int length) {
    Deque<Position> positions = new ArrayDeque<>();
    int destX = end.getX(), destY = end.getY();
    int localX = start.getLocalX(), localY = start.getLocalY();
    int regionX = start.getRegionX(), regionY = start.getRegionY();
    int z = start.getZ();

    if (destX == localX && destY == localY && !close) {
      return positions;
    }
    destX = destX - 8 * regionX;
    destY = destY - 8 * regionY;
    final int[][] via = new int[104][104];
    final int[][] cost = new int[104][104];
    final LinkedList<Integer> tileQueueX = new LinkedList<Integer>();
    final LinkedList<Integer> tileQueueY = new LinkedList<Integer>();
    for (int xx = 0; xx < 104; xx++)
      for (int yy = 0; yy < 104; yy++)
        cost[xx][yy] = 99999999;
    int curX = localX;
    int curY = localY;
    if (curX > via.length - 1 || curY > via[curX].length - 1)
      return positions;
    if (curX < via.length && curY < via[0].length)
      via[curX][curY] = 99;
    if (curX < cost.length && curY < cost[0].length)
      cost[curX][curY] = 0;
    @SuppressWarnings("unused")
    final int head = 0;
    int tail = 0;
    tileQueueX.add(curX);
    tileQueueY.add(curY);
    boolean foundPath = false;
    final int pathLength = 4000;
    while (tail != tileQueueX.size() && tileQueueX.size() < pathLength) {
      curX = tileQueueX.get(tail);
      curY = tileQueueY.get(tail);
      final int curAbsX = regionX * 8 + curX;
      final int curAbsY = regionY * 8 + curY;
      if (curX == destX && curY == destY) {
        foundPath = true;
        break;
      }
      tail = (tail + 1) % pathLength;

      if (cost.length < curX || cost[curX].length < curY)
        return positions;
      int thisCost = cost[curX][curY] + 1;
      int height = z % 4;

      if (curY > 0 && via[curX][curY - 1] == 0
          && (RegionClipping.getClipping(curAbsX, curAbsY - 1, height) & 0x1280102) == 0) {
        tileQueueX.add(curX);
        tileQueueY.add(curY - 1);
        via[curX][curY - 1] = 1;
        cost[curX][curY - 1] = thisCost;
      }
      if (curX > 0 && via[curX - 1][curY] == 0
          && (RegionClipping.getClipping(curAbsX - 1, curAbsY, height) & 0x1280108) == 0) {
        tileQueueX.add(curX - 1);
        tileQueueY.add(curY);
        via[curX - 1][curY] = 2;
        cost[curX - 1][curY] = thisCost;
      }
      if (curY < 104 - 1 && via[curX][curY + 1] == 0
          && (RegionClipping.getClipping(curAbsX, curAbsY + 1, height) & 0x1280120) == 0) {
        tileQueueX.add(curX);
        tileQueueY.add(curY + 1);
        via[curX][curY + 1] = 4;
        cost[curX][curY + 1] = thisCost;
      }
      if (curX < 104 - 1 && via[curX + 1][curY] == 0
          && (RegionClipping.getClipping(curAbsX + 1, curAbsY, height) & 0x1280180) == 0) {
        tileQueueX.add(curX + 1);
        tileQueueY.add(curY);
        via[curX + 1][curY] = 8;
        cost[curX + 1][curY] = thisCost;
      }
      if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0
          && (RegionClipping.getClipping(curAbsX - 1, curAbsY - 1, height) & 0x128010e) == 0
          && (RegionClipping.getClipping(curAbsX - 1, curAbsY, height) & 0x1280108) == 0
          && (RegionClipping.getClipping(curAbsX, curAbsY - 1, height) & 0x1280102) == 0) {
        tileQueueX.add(curX - 1);
        tileQueueY.add(curY - 1);
        via[curX - 1][curY - 1] = 3;
        cost[curX - 1][curY - 1] = thisCost;
      }
      if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0
          && (RegionClipping.getClipping(curAbsX - 1, curAbsY + 1, height) & 0x1280138) == 0
          && (RegionClipping.getClipping(curAbsX - 1, curAbsY, height) & 0x1280108) == 0
          && (RegionClipping.getClipping(curAbsX, curAbsY + 1, height) & 0x1280120) == 0) {
        tileQueueX.add(curX - 1);
        tileQueueY.add(curY + 1);
        via[curX - 1][curY + 1] = 6;
        cost[curX - 1][curY + 1] = thisCost;
      }
      if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0
          && (RegionClipping.getClipping(curAbsX + 1, curAbsY - 1, height) & 0x1280183) == 0
          && (RegionClipping.getClipping(curAbsX + 1, curAbsY, height) & 0x1280180) == 0
          && (RegionClipping.getClipping(curAbsX, curAbsY - 1, height) & 0x1280102) == 0) {
        tileQueueX.add(curX + 1);
        tileQueueY.add(curY - 1);
        via[curX + 1][curY - 1] = 9;
        cost[curX + 1][curY - 1] = thisCost;
      }
      if (curX < 104 - 1 && curY < 104 - 1 && via[curX + 1][curY + 1] == 0
          && (RegionClipping.getClipping(curAbsX + 1, curAbsY + 1, height) & 0x12801e0) == 0
          && (RegionClipping.getClipping(curAbsX + 1, curAbsY, height) & 0x1280180) == 0
          && (RegionClipping.getClipping(curAbsX, curAbsY + 1, height) & 0x1280120) == 0) {
        tileQueueX.add(curX + 1);
        tileQueueY.add(curY + 1);
        via[curX + 1][curY + 1] = 12;
        cost[curX + 1][curY + 1] = thisCost;
      }
    }
    if (!foundPath)
      if (close) {
        int i_223_ = 1000;
        int thisCost = 100;
        final int i_225_ = 10;
        for (int x = destX - i_225_; x <= destX + i_225_; x++)
          for (int y = destY - i_225_; y <= destY + i_225_; y++)
            if (x >= 0 && y >= 0 && x < 104 && y < 104 && cost[x][y] < 100) {
              int i_228_ = 0;
              if (x < destX)
                i_228_ = destX - x;
              else if (x > destX + width - 1)
                i_228_ = x - (destX + width - 1);
              int i_229_ = 0;
              if (y < destY)
                i_229_ = destY - y;
              else if (y > destY + length - 1)
                i_229_ = y - (destY + length - 1);
              final int i_230_ = i_228_ * i_228_ + i_229_ * i_229_;
              if (i_230_ < i_223_ || i_230_ == i_223_ && cost[x][y] < thisCost) {
                i_223_ = i_230_;
                thisCost = cost[x][y];
                curX = x;
                curY = y;
              }
            }
        if (i_223_ == 1000)
          return positions;
      } else
        return positions;
    tail = 0;
    tileQueueX.set(tail, curX);
    tileQueueY.set(tail++, curY);
    int l5;
    for (int j5 = l5 = via[curX][curY]; curX != localX || curY != localY; j5 = via[curX][curY]) {
      if (j5 != l5) {
        l5 = j5;
        tileQueueX.set(tail, curX);
        tileQueueY.set(tail++, curY);
      }
      if ((j5 & 2) != 0)
        curX++;
      else if ((j5 & 8) != 0)
        curX--;
      if ((j5 & 1) != 0)
        curY++;
      else if ((j5 & 4) != 0)
        curY--;
    }
    final int size = tail--;
    int pathX = regionX * 8 + tileQueueX.get(tail);
    int pathY = regionY * 8 + tileQueueY.get(tail);
    positions.addFirst(new Position(pathX, pathY, z));
    for (int i = 1; i < size; i++) {
      tail--;
      pathX = regionX * 8 + tileQueueX.get(tail);
      pathY = regionY * 8 + tileQueueY.get(tail);
      positions.addLast(new Position(pathX, pathY, z));
    }
    return positions;
  }
}
