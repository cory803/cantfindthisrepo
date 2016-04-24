package com.runelive.world.content.skill.impl.herblore;

public enum Herbs {

  GUAM(199, 249, 1, 125),
  MARRENTILL(201, 251, 5, 190),
  TARROMIN(203, 253, 11, 250),
  HARRALANDER(205, 255, 20, 315),
  RANARR(207, 257, 25, 375),
  TOADFLAX(3049, 2998, 30, 400),
  SPIRITWEED(12174, 12172, 35, 430),
  IRIT(209, 259, 40, 440),
  WERGALI(14836, 14854, 30, 225),
  AVANTOE(211, 261, 48, 500),
  KWUARM(213, 263, 54, 565),
  SNAPDRAGON(3051, 3000, 59, 590),
  CADANTINE(215, 265, 65, 625),
  LANTADYME(2485, 2481, 67, 655),
  DWARFWEED(217, 267, 70, 690),
  TORSTOL(219, 269, 75, 750);

  private int grimyHerb, cleanHerb, levelReq, cleaningExp;

  private Herbs(int grimyHerb, int cleanHerb, int levelReq, int cleaningExp) {
    this.grimyHerb = grimyHerb;
    this.cleanHerb = cleanHerb;
    this.levelReq = levelReq;
    this.cleaningExp = cleaningExp;
  }

  public int getGrimyHerb() {
    return grimyHerb;
  }

  public int getCleanHerb() {
    return cleanHerb;
  }

  public int getLevelReq() {
    return levelReq;
  }

  public int getExp() {
    return cleaningExp;
  }

  public static Herbs forId(int herbId) {
    for (Herbs herb : Herbs.values()) {
      if (herb.getGrimyHerb() == herbId) {
        return herb;
      }
    }
    return null;
  }

}
