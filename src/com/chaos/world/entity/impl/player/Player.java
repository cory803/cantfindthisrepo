package com.chaos.world.entity.impl.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.chaos.model.*;
import com.chaos.model.action.ActionQueue;
import com.chaos.model.options.OptionContainer;
import com.chaos.model.player.ActionHandler;
import com.chaos.model.player.GameModeAssistant;
import com.chaos.model.player.dialog.Dialog;
import com.chaos.model.player.dialog.DialogHandler;
import com.chaos.net.mysql.impl.Hiscores;
import com.chaos.world.content.Kraken;
import com.chaos.world.content.skill.impl.thieving.Thieving;
import org.jboss.netty.channel.Channel;

import com.chaos.GameSettings;
import com.chaos.engine.task.Task;
import com.chaos.engine.task.TaskManager;
import com.chaos.engine.task.impl.PlayerDeathTask;
import com.chaos.engine.task.impl.WalkToTask;
import com.chaos.model.container.impl.Bank;
import com.chaos.model.container.impl.Bank.BankSearchAttributes;
import com.chaos.model.container.impl.Equipment;
import com.chaos.model.container.impl.Inventory;
import com.chaos.model.container.impl.PlayerOwnedShopContainer;
import com.chaos.model.container.impl.PriceChecker;
import com.chaos.model.container.impl.Shop;
import com.chaos.model.definitions.WeaponAnimations;
import com.chaos.model.definitions.WeaponInterfaces;
import com.chaos.model.definitions.WeaponInterfaces.WeaponInterface;
import com.chaos.model.input.Input;
import com.chaos.net.PlayerSession;
import com.chaos.net.SessionState;
import com.chaos.net.login.LoginDetailsMessage;
import com.chaos.net.packet.PacketSender;
import com.chaos.util.Stopwatch;
import com.chaos.world.content.Achievements.AchievementAttributes;
import com.chaos.world.content.BankPin.BankPinAttributes;
import com.chaos.world.content.BonusManager;
import com.chaos.world.content.DropLog.DropLogEntry;
import com.chaos.world.content.KillsTracker.KillsEntry;
import com.chaos.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.chaos.world.content.PointsHandler;
import com.chaos.world.content.Trading;
import com.chaos.world.content.clan.ClanChat;
import com.chaos.world.content.combat.CombatFactory;
import com.chaos.world.content.combat.CombatType;
import com.chaos.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.chaos.world.content.combat.effect.CombatVenomEffect.CombatVenomData;
import com.chaos.world.content.combat.magic.CombatSpell;
import com.chaos.world.content.combat.magic.CombatSpells;
import com.chaos.world.content.combat.prayer.CurseHandler;
import com.chaos.world.content.combat.prayer.PrayerHandler;
import com.chaos.world.content.combat.pvp.PlayerKillingAttributes;
import com.chaos.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.chaos.world.content.combat.strategy.CombatStrategies;
import com.chaos.world.content.combat.strategy.CombatStrategy;
import com.chaos.world.content.combat.weapon.CombatSpecial;
import com.chaos.world.content.combat.weapon.FightType;
import com.chaos.world.content.minigames.MinigameAttributes;
import com.chaos.world.content.minigames.impl.Dueling;
import com.chaos.world.content.minigames.impl.Dueling.DuelRule;
import com.chaos.world.content.minigames.impl.zulrah.Zulrah;
import com.chaos.world.content.pos.PosDetails;
import com.chaos.world.content.pos.PosOffer;
import com.chaos.world.content.skill.SkillManager;
import com.chaos.world.content.skill.impl.farming.Farming;
import com.chaos.world.content.skill.impl.slayer.Slayer;
import com.chaos.world.content.skill.impl.summoning.Pouch;
import com.chaos.world.content.skill.impl.summoning.Summoning;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.npc.NPC;

public class Player extends Character {

    public Player(PlayerSession playerIO) {
        super(GameSettings.DEFAULT_POSITION_VARROCK.copy());
        this.session = playerIO;
    }

	public boolean isSpecialPlayer() {
        if (GameSettings.SPECIAL_PLAYERS.contains(getUsername().toLowerCase()))
            return true;
		return false;
	}

    public int idNpcSpawn = 0;
    public boolean canWalkNpcSpawn = false;
    public int radiusNpcSpawn = 0;

    public int withdrawX = 0;
    public int getWithdrawX() {
        return this.withdrawX;
    }
    public void setWithdrawX(int x) {
        this.withdrawX = x;
    }
    public int summoningAdd = 0;
    public boolean claimingStoreItems = false;
    public int debugRollWins;
    public int debugRollLosses;
    private int passwordChange = 0;
    public boolean forceOffline = false;
    public String changingPasswordOf = "none";
    public int reset_stats_1 = 0;

    public static Map<PosDetails, PosOffer> foundOffers = new HashMap<PosDetails, PosOffer>();

    public int getPasswordChange() {
        return passwordChange;
    }

    public void setPasswordChange(int val) {
        this.passwordChange = val;
    }

    public boolean synchronizedLogout = false;

    public boolean ge_return = false;

    public int currentScroll = -1;

    public boolean hasDoneGrandExchangeReturn() {
        return ge_return;
    }

    public boolean spawnedCerberus = false;

    public void setDoneGrandExchangeReturn(boolean abb) {
        ge_return = abb;
    }

    public int completionist_clicked = 0;

    @Override
    public void appendDeath() {
        if (!isDying) {
            isDying = true;
            TaskManager.submit(new PlayerDeathTask(this));
        }
    }

    @Override
    public int getConstitution() {
        return getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
    }

    @Override
    public Character setConstitution(int constitution) {
        if (isDying) {
            return this;
        }
        skillManager.setCurrentLevel(Skill.CONSTITUTION, constitution);
        packetSender.sendSkill(Skill.CONSTITUTION);
        if (getConstitution() <= 0 && !isDying)
            appendDeath();
        return this;
    }

    @Override
    public void heal(int amount) {
        int level = skillManager.getMaxLevel(Skill.CONSTITUTION);
        if ((skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount) >= level) {
            setConstitution(level);
			/*
			 * if ((skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount)
			 * >= calculateMaxLifePoints()) {
			 * setConstitution(calculateMaxLifePoints()); Torva test ^
			 */
        } else {
            setConstitution(skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount);
        }
    }

    @Override
    public int getBaseAttack(CombatType type) {
        if (type == CombatType.RANGED)
            return skillManager.getCurrentLevel(Skill.RANGED);
        else if (type == CombatType.MAGIC)
            return skillManager.getCurrentLevel(Skill.MAGIC);
        return skillManager.getCurrentLevel(Skill.ATTACK);
    }

    @Override
    public int getBaseDefence(CombatType type) {
        if (type == CombatType.MAGIC)
            return skillManager.getCurrentLevel(Skill.MAGIC);
        return skillManager.getCurrentLevel(Skill.DEFENCE);
    }

    @Override
    public int getAttackSpeed() {
        int speed = weapon.getSpeed();
        String weapon = equipment.get(Equipment.WEAPON_SLOT).getDefinition().getName();
        int weaponId = equipment.get(Equipment.WEAPON_SLOT).getId();
        if (getCurrentlyCasting() != null) {
            if (equipment.get(Equipment.WEAPON_SLOT).getId() == 21108) {
                return 4;
            }
            if (getCurrentlyCasting() == CombatSpells.BLOOD_BLITZ.getSpell()
                    || getCurrentlyCasting() == CombatSpells.SHADOW_BLITZ.getSpell()
                    || getCurrentlyCasting() == CombatSpells.SMOKE_BLITZ.getSpell()
                    || getCurrentlyCasting() == CombatSpells.ICE_BLITZ.getSpell()) {
                return 5;
            } else {
                return 5;
            }
        }
        if (weaponId == 1419) {
            speed -= 2;
        }
        if (fightType == FightType.CROSSBOW_RAPID) {
            speed -= 1;
        }
        if (fightType == FightType.LONGBOW_RAPID  || weaponId == 6522 && fightType == FightType.KNIFE_RAPID) {
            if (weaponId != 11235 && weaponId != 21016 && weaponId != 21017 && weaponId != 21018 && weaponId != 21019
                    && weaponId != 21020 && weaponId != 21021 && weaponId != 21022 && weaponId != 21023) {
                speed--;
            }
        } else if (weaponId != 6522 && weaponId != 15241 && (fightType == FightType.JAVELIN_RAPID)|| weaponId == 11730) {
            speed -= 2;
        }
        if(weapon.contains("rapier") || weapon.contains("hatchet")) {
            return 5;
        } else if(weapon.contains("maul") || weapon.contains("longsword")) {
            return 6;
        } else if(weaponId == 21089) {
            return 5;
        } else if(weaponId == 21100) {
            return 4;
        } else if(fightType == FightType.SHORTBOW_RAPID) {
            return 3;
        } else if(weapon.contains("shortbow") && fightType != FightType.SHORTBOW_RAPID) {
            return 4;
        } else if(fightType == FightType.DART_RAPID || fightType == FightType.KNIFE_RAPID) {
            return 2;
        } else if(fightType == FightType.THROWNAXE_RAPID) {
            return 4;
        } else if(weapon.contains("throwing axe") && fightType != FightType.THROWNAXE_RAPID) {
            return 5;
        } else if(weapon.contains("blowpipe")) {
            if(this.getCombatBuilder() != null) {
                if(this.getCombatBuilder().getVictim() != null) {
                    if (this.getCombatBuilder().getVictim().isNpc()) {
                        if(fightType == FightType.BLOWPIPE_RAPID) {
                            return 2;
                        } else {
                            return 3;
                        }
                    } else {
                        if(fightType == FightType.BLOWPIPE_RAPID) {
                            return 3;
                        } else {
                            return 4;
                        }
                    }

                }
            }
        }
        return speed;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public boolean[] lastDuelRules = new boolean[DuelRule.values().length];
    public String yell_tag = "invalid_yell_tag_set";

    public void setYellTag(String tag) {
        yell_tag = tag;
    }

    public String getYellTag() {
        return yell_tag;
    }

    public int meleeMaxHit = 0;
    public int rangeMaxHit = 0;
    public int magicMaxHit = 0;

    public int getMeleeMaxHit() {
        return this.meleeMaxHit;
    }

    public int getRangeMaxHit() {
        return this.rangeMaxHit;
    }

    public int getMagicMaxHit() {
        return this.magicMaxHit;
    }

    public void setMeleeMaxHit(int newMax) {
        this.meleeMaxHit = newMax;
    }

    public void setRangeMaxHit(int newMax) {
        this.rangeMaxHit = newMax;
    }

    public void setMagicMaxHit(int newMax) {
        this.magicMaxHit = newMax;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player)) {
            return false;
        }

        Player p = (Player) o;
        return p.getIndex() == getIndex() || p.getUsername().equals(username);
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public void poisonVictim(Character victim, CombatType type) {
        if (type == CombatType.MELEE || weapon == WeaponInterface.DART || weapon == WeaponInterface.KNIFE
                || weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.JAVELIN) {
            CombatFactory.poisonEntity(victim, CombatPoisonData.getPoisonType(equipment.get(Equipment.WEAPON_SLOT)));
        } else if (type == CombatType.RANGED) {
            CombatFactory.poisonEntity(victim,
                    CombatPoisonData.getPoisonType(equipment.get(Equipment.AMMUNITION_SLOT)));
        }
    }

    @Override
    public void venomVictim(Character victim, CombatType type) {
        int weaponId = equipment.get(Equipment.WEAPON_SLOT).getId();
        int helmet = equipment.get(Equipment.HEAD_SLOT).getId();
        if ((type == CombatType.RANGED && weapon == WeaponInterface.BLOWPIPE) || (weaponId == 21077)) {
            CombatFactory.venomEntity(victim, CombatVenomData.getVenomType(equipment.get(Equipment.WEAPON_SLOT)));
        } else if (helmet == 21107) {
            CombatFactory.venomEntity(victim, CombatVenomData.getVenomType(equipment.get(Equipment.HEAD_SLOT)));
        }
    }

    @Override
    public CombatStrategy determineStrategy() {
        if (specialActivated && castSpell == null) {

            if (combatSpecial.getCombatType() == CombatType.MELEE) {
                return CombatStrategies.getDefaultMeleeStrategy();
            } else if (combatSpecial.getCombatType() == CombatType.RANGED) {
                setRangedWeaponData(RangedWeaponData.getData(this));
                return CombatStrategies.getDefaultRangedStrategy();
            } else if (combatSpecial.getCombatType() == CombatType.MAGIC) {
                return CombatStrategies.getDefaultMagicStrategy();
            }
        }

        if (castSpell != null || autocastSpell != null) {
            return CombatStrategies.getDefaultMagicStrategy();
        }

        RangedWeaponData data = RangedWeaponData.getData(this);
        if (data != null) {
            setRangedWeaponData(data);
            return CombatStrategies.getDefaultRangedStrategy();
        }

        return CombatStrategies.getDefaultMeleeStrategy();
    }

    public int responseId = 2;
    public boolean xpRate = true;
    // public boolean loginQue = false;

    // public boolean getLoginQue() {
    // return loginQue;
    // }

    public void setXpRate(boolean bbb) {
        xpRate = bbb;
    }

	/*
	 * public void setLoginQue(boolean bbb) { loginQue = bbb; }
	 */

    public void setResponse(int a2) {
        responseId = a2;
    }

    public boolean getXpRate() {
        return xpRate;
    }

    public int getResponse() {
        return responseId;
    }

    public void process() {
        process.sequence();
    }

    public boolean saveFile;

    public void dispose() {
        save();
        packetSender.sendLogout();
    }

    public void save() {
        if (session.getState() != SessionState.LOGGED_IN && session.getState() != SessionState.LOGGING_OUT) {
            return;
        }
        if (GameSettings.MYSQL_PLAYER_SAVING)
            PlayerSaving.saveGame(this);
        if (GameSettings.JSON_PLAYER_SAVING)
            PlayerSaving.save(this);
    }

    public boolean logout() {
        if (getCombatBuilder().isBeingAttacked() || getCombatBuilder().isAttacking()) {
            getPacketSender().sendMessage("You must wait 10 seconds after being out of combat before doing this.");
            return false;
        }
        if (getConstitution() <= 0 || isDying || settingUpCannon || crossingObstacle) {
            getPacketSender().sendMessage("You cannot log out at the moment.");
            return false;
        }
        if(GameSettings.HIGHSCORE_CONNECTIONS) {
            new Hiscores(this).execute();
        }
        return true;
    }

    public void restart() {
        setFreezeDelay(0);
        setOverloadPotionTimer(0);
        setPrayerRenewalPotionTimer(0);
        setSpecialPercentage(100);
        setSpecialActivated(false);
        CombatSpecial.updateBar(this);
        setHasVengeance(false);
        setSkullTimer(0);
        setSkullIcon(0);
        setTeleblockTimer(0);
        setPoisonDamage(0);
        setVenomDamage(0);
        getPacketSender().sendConstitutionOrbPoison(false);
        getPacketSender().sendConstitutionOrbVenom(false);
        setStaffOfLightEffect(0);
        performAnimation(new Animation(65535));
        WeaponInterfaces.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
        WeaponAnimations.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
        PrayerHandler.deactivateAll(this);
        CurseHandler.deactivateAll(this);
        getEquipment().refreshItems();
        getInventory().refreshItems();
        for (Skill skill : Skill.values())
            getSkillManager().setCurrentLevel(skill, getSkillManager().getMaxLevel(skill));
        setRunEnergy(100);
        setDying(false);
        getWalkingQueue().clear();
        getUpdateFlag().flag(Flag.APPEARANCE);
    }

    public boolean busy() {
        return interfaceId > 0 || isBanking || shopping || posShopping || trading.inTrade() || dueling.inDuelScreen
                || isResting;
    }

    public void walkTo(int offsetX, int offsetY) {
        if (!player.playerLocked) {
            return;
        }
        player.getWalkingQueue().clear();
        player.getWalkingQueue().addClippedStep(player.getPosition().getX() + offsetX, player.getPosition().getY() + offsetY);
    }

	/*
	 * Fields
	 */

    /*** STRINGS ***/
    private String username;
    private String password;
    private long serial_number;
    private String mac_address;
    private String computer_address;
    private String emailAddress;
    private String hostAddress;
    private String clanChatName;
    private String salt = "";
    private String hashedPassword;

    /*** LONGS **/
    private Long longUsername;
    public long lastThieve;
    public long lastAgilityClick;
    private long moneyInPouch;
    private long totalPlayTime;
    // Timers (Stopwatches)
    private final Stopwatch tabTimer = new Stopwatch();
    private final Stopwatch quickChat = new Stopwatch();
    private final Stopwatch yellTimer = new Stopwatch();
    private final Stopwatch voteTimer = new Stopwatch();
    private final Stopwatch dragon_scimitar_timer = new Stopwatch();
    private final Stopwatch sqlTimer = new Stopwatch();
    private final Stopwatch specTimer = new Stopwatch();
    public boolean pestControlSolo = false;
    private final Stopwatch foodTimer = new Stopwatch();
    private final Stopwatch comboFoodDelay = new Stopwatch();
    private final Stopwatch potionTimer = new Stopwatch();
    private final Stopwatch lastRunRecovery = new Stopwatch();
    private final Stopwatch clickDelay = new Stopwatch();
    private final Stopwatch stuckDelay = new Stopwatch();
    private final Stopwatch forumDelay = new Stopwatch();
    private final Stopwatch lastItemPickup = new Stopwatch();
    private final Stopwatch lastVengeance = new Stopwatch();
    private final Stopwatch lastLoot = new Stopwatch();
    private final Stopwatch lastRoll = new Stopwatch();
    private final Stopwatch lastAuth = new Stopwatch();
    private final Stopwatch lastResource = new Stopwatch();
    private final Stopwatch emoteDelay = new Stopwatch();
    private final Stopwatch specialRestoreTimer = new Stopwatch();
    private final Stopwatch lastSummon = new Stopwatch();
    private final Stopwatch recordedLogin = new Stopwatch();
    private final Stopwatch tolerance = new Stopwatch();
    private final Stopwatch lougoutTimer = new Stopwatch();
    private final Stopwatch dragonfireShield = new Stopwatch();
    private final Stopwatch dragonSpear = new Stopwatch();
    private final Stopwatch duelTimer = new Stopwatch();
    private final Stopwatch summoningTimer = new Stopwatch();

    /*** INSTANCES ***/
    private final CopyOnWriteArrayList<KillsEntry> killsTracker = new CopyOnWriteArrayList<KillsEntry>();
    public final ArrayList<String> playersAttacked = new ArrayList<String>();
    private final CopyOnWriteArrayList<DropLogEntry> dropLog = new CopyOnWriteArrayList<DropLogEntry>();
    private final List<Player> localPlayers = new LinkedList<Player>();
    private final List<NPC> localNpcs = new LinkedList<NPC>();

    public PlayerSession session;
    public LoginDetailsMessage logindetailsmessage;
    public Channel channel;
    private final PlayerTimers playerTimers = new PlayerTimers(this);
    private final ActionHandler actionHandler = new ActionHandler(this);
    private final ActionQueue actionQueue = new ActionQueue(this);
    private final PlayerProcess process = new PlayerProcess(this);
    private final PlayerKillingAttributes playerKillingAttributes = new PlayerKillingAttributes(this);
    private final MinigameAttributes minigameAttributes = new MinigameAttributes();
    private final BankPinAttributes bankPinAttributes = new BankPinAttributes();
    private final BankSearchAttributes bankSearchAttributes = new BankSearchAttributes();
    private final AchievementAttributes achievementAttributes = new AchievementAttributes();
    private CharacterAnimations characterAnimations = new CharacterAnimations();
    private final BonusManager bonusManager = new BonusManager();
    private final PointsHandler pointsHandler = new PointsHandler(this);
    private final PacketSender packetSender = new PacketSender(this);
    private final Appearance appearance = new Appearance(this);
    private StaffRights staffRights = StaffRights.PLAYER;
    private DonatorRights donatorRights = DonatorRights.PLAYER;
    private Thieving thieving = new Thieving(this);
    private SkillManager skillManager = new SkillManager(this);
    private PlayerRelations relations = new PlayerRelations(this);
    private GameModeAssistant gameModeAssistant = new GameModeAssistant(this);
    private ChatMessage chatMessages = new ChatMessage();
    private Inventory inventory = new Inventory(this);
    private Equipment equipment = new Equipment(this);
    private PriceChecker priceChecker = new PriceChecker(this);
    private Trading trading = new Trading(this);
    private Dueling dueling = new Dueling(this);
    private Slayer slayer = new Slayer(this);
    private Farming farming = new Farming(this);
    private Summoning summoning = new Summoning(this);
    private Bank[] bankTabs = new Bank[9];
    private PlayerInteractingOption playerInteractingOption = PlayerInteractingOption.NONE;
    private CombatType lastCombatType = CombatType.MELEE;
    private FightType fightType = FightType.UNARMED_PUNCH;
    private Prayerbook prayerbook = Prayerbook.NORMAL;
    private MagicSpellbook spellbook = MagicSpellbook.NORMAL;
    private LoyaltyTitles loyaltyTitle = LoyaltyTitles.NONE;
    private ClanChat currentClanChat;
    private Input inputHandling;
    private WalkToTask walkToTask;
    private Shop shop;
    private PlayerOwnedShopContainer player_owned_shop;
    private GameObject interactingObject;
    private Item interactingItem;
    public Dialog currentDialog;
    private DwarfCannon cannon;
    private CombatSpell autocastSpell, castSpell, previousCastSpell;
    private RangedWeaponData rangedWeaponData;
    private CombatSpecial combatSpecial;
    private WeaponInterface weapon;
    private Item untradeableDropItem;
    private Object[] usableObject;
    private Task currentTask;
    private Position resetPosition;
    private Kraken kraken = new Kraken();
    private Pouch selectedPouch;
    private final DialogHandler dialogueHandler = new DialogHandler(this);
    private OptionContainer optionContainer = new OptionContainer(this);

    /*** INTS ***/
    private int[] vestaCharges = new int[8];
    private int[] statiusCharges = new int[8];
    private int[] zurielsCharges = new int[8];
    private int[] morrigansCharges = new int[8];
    private int[] corruptVestaCharges = new int[8];
    private int[] corruptStatiusCharges = new int[8];
    private int[] corruptZurielsCharges = new int[8];
    private int[] corruptMorrigansCharges = new int[8];
    public int[] allKeys = { 1543, 1545, 1546, 1547, 1548 };
    public int voteCount = 0;
    private int npcClickId;
    private int questPoints = 0;
    private int bossPoints = 0;
    private int lastBoss = 0;
    private int warningPoints = 0;
    private int votesClaimed = 0;
    public int barrowsChestsLooted = 0;
    public int barrowsChestRewards = 0;
    private int[] brawlerCharges = new int[9];
    private int[] forceMovement = new int[7];
    private int[] leechedBonuses = new int[7];
    private int[] ores = new int[2];
    private int[] constructionCoords;
    public int[] offsetX = new int[50];
    public int[] offsetY = new int[50];
    private int recoilCharges;
    private float runEnergy = 100;
    private int currentBankTab;
    private int interfaceId, walkableInterfaceId, multiIcon;
    private int dialogueActionId;
    private int overloadPotionTimer, prayerRenewalPotionTimer;
    private int fireImmunity, fireDamageModifier;
    private int amountDonated;
    private int credits;
    private int wildernessLevel;
    private int fireAmmo;
    private int specialPercentage = 100;
    private int skullIcon = -1, skullTimer;
    private int teleblockTimer;
    private int dragonFireImmunity;
    private int poisonImmunity;
    private int venomImmunity;
    private int shadowState;
    private int effigy;
    private int dfsCharges;
    private int playerViewingIndex;
    private int staffOfLightEffect;
    private int minutesBonusExp = -1;
    private int selectedGeSlot = -1;
    private int selectedGeItem = -1;
    private int geQuantity;
    private int gePricePerItem;
    private int selectedSkillingItem;
    private int currentBookPage;
    private int storedRuneEssence, storedPureEssence;
    private int trapsLaid;
    private int skillAnimation;
    private int houseServant;
    private int houseServantCharges;
    private int servantItemFetch;
    private int portalSelected;
    private int constructionInterface;
    private int buildFurnitureId;
    private int buildFurnitureX;
    private int buildFurnitureY;
    private int combatRingType;
    private int forum_connections;
    private int forum_connections_rank;

    /**
     * The loot potential available while using lootshare.
     */
    public int lootSharePotential;

    private Player killed_player;

    /*** BOOLEANS ***/
    private boolean invisible = false;
    public boolean ignoreClip = true;
    private boolean canWearDungItems = false;
    private boolean revsWarning = true;
    private boolean passedRandom = true;
    private boolean bossSolo = true;
    private boolean unlockedLoyaltyTitles[] = new boolean[12];
    private boolean[] crossedObstacles = new boolean[7];
    private boolean processFarming;
    private boolean crossingObstacle;
    private boolean targeted;
    private boolean isBanking, noteWithdrawal, swapMode;
    private boolean regionChange, allowRegionChangePacket;
    private boolean isDying;
    private boolean isRunning = true, isResting;
    private boolean experienceLocked;
    private boolean showIpAddressOnLogin;
    private boolean showHomeOnLogin = true;
    private boolean capeChanges = true;
    private boolean clientExitTaskActive;
    private boolean drainingPrayer;
    private boolean shopping;
    private boolean posShopping;
    private boolean settingUpCannon;
    private boolean hasVengeance;
    private boolean killsTrackerOpen;
    private boolean questTabOpen;
    private boolean acceptingAid;
    private boolean autoRetaliate;
    private boolean autocast;
    private boolean specialActivated;
    private boolean isDoingTutorial;
    private boolean isCoughing;
    private boolean playerLocked;
    private boolean recoveringSpecialAttack;
    private boolean soundsActive, musicActive;
    private boolean newPlayer;
    private boolean openBank;
    private boolean tutorialContinue;
    private boolean skipTutorialContinue;
    private boolean loginAccountPin;
    private boolean inActive;
    private boolean inConstructionDungeon;
    private boolean isBuildingMode;
    private boolean voteMessageSent;
    private boolean receivedStarter;
    private boolean canVote = true;
    private long last_login = -1;
    private String last_ip_address;
    private long last_serial_address;
    private String last_mac_address;
    private String last_computer_address;
    public int dailyTaskDate = 0;
    public int dailyTask = 0;
    public int dailyTaskProgress = 0;
    public int homeLocation = 0;
    public boolean completedDailyTask = false;

    private List<String> notes = new ArrayList<>();
    private List<Integer> noteColours = new ArrayList<>();
    private String temp_note = "";

    private Zulrah zulrah = new Zulrah(this);

    public Zulrah getZulrah() {
        return this.zulrah;
    }

    // Toxic weapons
    public int toxic_staff_charges = 0;

    /*
     * Getters & Setters
     */


    public String getTempNote() {
        return temp_note;
    }

    public void setTempNote(String text) {
        temp_note = text;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> list) {
        notes = list;
    }

    public void addNote(String note) {
        notes.add(note);
    }

    public void deleteNote(int index) {
        notes.remove(index);
    }

    public void setNote(int index, String note) {
        notes.set(index, note);
    }

    public void setNoteColours(List<Integer> list) {
        noteColours = list;
    }

    public List<Integer> getNoteColours() {
        return noteColours;
    }

    public void addNoteColour(int colour) {
        noteColours.add(colour);
    }

    public void deleteNoteColour(int index) {
        noteColours.remove(index);
    }

    public void setNoteColour(int index, int colour) {
        noteColours.set(index, colour);
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public PlayerTimers getPlayerTimers() {
        return playerTimers;
    }

    public ActionHandler getActions() {
        return actionHandler;
    }

    public ActionQueue getActionQueue() {
        return actionQueue;
    }

    public Thieving getThieving() {
        return this.thieving;
    }

    public DialogHandler getDialog() {
        return dialogueHandler;
    }

    public void setNpcClickId(int index) {
        this.npcClickId = index;
    }

    public int getNpcClickId() {
        return npcClickId;
    }

    public OptionContainer getOptionContainer() {
        return optionContainer;
    }
    public GameModeAssistant getGameModeAssistant() {
        return this.gameModeAssistant;
    }

    public boolean isCanWearDungItems() {
        return canWearDungItems;
    }

    public void setCanWearDungItems(boolean canWearDungItems) {
        this.canWearDungItems = canWearDungItems;
    }

    public int getQuestPoints() {
        return questPoints;
    }

    public Player getKilledPlayer() {
        return killed_player;
    }

    public void setKilledPlayer(Player pp) {
        killed_player = pp;
    }

    public void setQuestPoints(int questPoints) {
        this.questPoints = questPoints;
    }

    public void addQuestPoints(int questPoints) {
        this.questPoints += questPoints;
    }

    public boolean getRevsWarning() {
        return revsWarning;
    }

    public void setRevsWarning(boolean revsWarning) {
        this.revsWarning = revsWarning;
    }

    public int getBossPoints() {
        return bossPoints;
    }

    public void setBossPoints(int bossPoints) {
        this.bossPoints = bossPoints;
    }

    public void addBossPoints(int bossPoints) {
        this.bossPoints += bossPoints;
    }

    public void minusBossPoints(int bossPoints) {
        this.bossPoints -= bossPoints;
    }

    public boolean isJailed() {
        return this.playerTimers.getJailTicks() != -1;
    }

    public boolean isPassedRandom() {
        return passedRandom;
    }

    public void setPassedRandom(boolean passedRandom) {
        this.passedRandom = passedRandom;
    }

    public boolean isBossSolo() {
        return bossSolo;
    }

    public void setBossSolo(boolean bossSolo) {
        this.bossSolo = bossSolo;
    }

    public int getLastBoss() {
        return lastBoss;
    }

    public void setLastBoss(int lastBoss) {
        this.lastBoss = lastBoss;
    }

    public int getWarningPoints() {
        return warningPoints;
    }

    public void setWarningPoints(int warningPoints) {
        this.warningPoints = warningPoints;
    }

    public void addWarningPoints(int warningPoints) {
        this.warningPoints += warningPoints;
    }

    public void minusWarningPoints(int warningPoints) {
        this.warningPoints -= warningPoints;
    }

    public boolean isCanVote() {
        return canVote;
    }

    public void setCanVote(boolean canVote) {
        this.canVote = canVote;
    }

    public int getVotesClaimed() {
        return votesClaimed;
    }

    public void setVotesClaimed(int votesClaimed) {
        this.votesClaimed += votesClaimed;
    }

    public PlayerSession getSession() {
        return session;
    }

    public LoginDetailsMessage getLoginDetailsMessage() {
        return logindetailsmessage;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channell) {
        this.channel = channell;
    }

    public void setLoginDetailsMessage(LoginDetailsMessage channell) {
        this.logindetailsmessage = channell;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public PriceChecker getPriceChecker() {
        return priceChecker;
    }

	/*
	 * Getters and setters
	 */

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setHashedPassword(String pass) {
        this.hashedPassword = pass;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setLastLogin(long last_login) {
        this.last_login = last_login;
    }

    public void setLastIpAddress(String last_ip) {
        last_ip_address = last_ip;
    }

    public void setDoingTotorial(boolean doing_it) {
        isDoingTutorial = doing_it;
    }

    public void setLastSerialAddress(long last_serial) {
        last_serial_address = last_serial;
    }

    public void setLastMacAddress(String mac) {
        last_mac_address = mac;
    }

    public void setLastComputerAddress(String add) {
        last_computer_address = add;
    }

    public boolean doingTutorial() {
        return isDoingTutorial;
    }

    public long getLastLogin() {
        return last_login;
    }

    public String getLastIpAddress() {
        return last_ip_address;
    }

    public long getLastSerialAddress() {
        return last_serial_address;
    }

    public String getLastMacAddress() {
        return last_mac_address;
    }

    public String getLastComputerAddress() {
        return last_computer_address;
    }

    public Player setUsername(String username) {
        this.username = username;
        return this;
    }

    public String last_bank_serial = "";
    public String last_bank_ip = "";
    public int combat_hit_tick = 0;
    public boolean has_combat_tick = false;

    public boolean isYellMute() {
        return this.playerTimers.getYellTicks() != -1;
    }

    public boolean yell_toggle = true;

    public boolean tourney_toggle = false;

    public String getLastBankSerial() {
        return last_bank_serial;
    }

    public boolean yellToggle() {
        return yell_toggle;
    }

    public boolean tourneyToggle() {
        return tourney_toggle;
    }

    public void setYellToggle(boolean new_yell) {
        yell_toggle = new_yell;
    }

    public void setTourneyToggle(boolean new_yell) {
        tourney_toggle = new_yell;
    }

    public String getLastBankIp() {
        return last_bank_ip;
    }

    public void setLastBankSerial(String serial) {
        last_bank_serial = serial;
    }

    public void setLastBankIp(String ip) {
        last_bank_ip = ip;
    }

    public Long getLongUsername() {
        return longUsername;
    }

    public Player setLongUsername(Long longUsername) {
        this.longUsername = longUsername;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String address) {
        this.emailAddress = address;
    }

    public Player setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public Player setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
        return this;
    }

    public long getSerialNumber() {
        return serial_number;
    }

    public String getMacAddress() {
        return mac_address;
    }

    public String getComputerAddress() {
        return computer_address;
    }

    public Player setSerialNumber(long serial_number) {
        this.serial_number = serial_number;
        return this;
    }

    public Player setMacAddress(String macc) {
        this.mac_address = macc;
        return this;
    }

    public Player setComputerAddress(String add) {
        this.computer_address = add;
        return this;
    }

    public StaffRights getStaffRights() {
        return staffRights;
    }

    public Kraken getKraken() {
        return this.kraken;
    }

    public void resetKraken() {
        this.getKraken().reset();
        this.kraken = new Kraken();
    }

    public DonatorRights getDonatorRights() {
        return donatorRights;
    }
    public int getCrown() {
        if(this.getStaffRights().isStaff()) {
            return this.getStaffRights().getCrown();
        } else if(this.getDonatorRights().isDonator()) {
            return this.getDonatorRights().getCrown();
        } else {
            return this.getGameModeAssistant().getGameMode().getCrown();
        }
    }

    public void setStaffRights(StaffRights rights) {
       this.staffRights = rights;
    }

    public void setDonatorRights(DonatorRights rights) {
       this.donatorRights = rights;
    }

    public ChatMessage getChatMessages() {
        return chatMessages;
    }

    public PacketSender getPacketSender() {
        return packetSender;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public PlayerRelations getRelations() {
        return relations;
    }

    public PlayerKillingAttributes getPlayerKillingAttributes() {
        return playerKillingAttributes;
    }

    public PointsHandler getPointsHandler() {
        return pointsHandler;
    }

    public int getToxicStaffCharges() {
        return toxic_staff_charges;
    }

    public void setToxicStaffCharges(int charges1) {
        toxic_staff_charges = charges1;
    }

    public void addToxicStaffCharges(int charges1) {
        toxic_staff_charges += charges1;
    }

    public boolean isImmuneToDragonFire() {
        return dragonFireImmunity > 0;
    }

    public int getDragonFireImmunity() {
        return dragonFireImmunity;
    }

    public void setDragonFireImmunity(int dragonFireImmunity) {
        this.dragonFireImmunity = dragonFireImmunity;
    }

    public void incrementDragonFireImmunity(int amount) {
        dragonFireImmunity += amount;
    }

    public void decrementDragonFireImmunity(int amount) {
        dragonFireImmunity -= amount;
    }

    public int getPoisonImmunity() {
        return poisonImmunity;
    }

    public int getVenomImmunity() {
        return poisonImmunity;
    }

    public void setPoisonImmunity(int poisonImmunity) {
        this.poisonImmunity = poisonImmunity;
    }

    public void setVenomImmunity(int venomImmunity) {
        this.venomImmunity = venomImmunity;
    }

    public void incrementPoisonImmunity(int amount) {
        poisonImmunity += amount;
    }

    public void incrementVenomImmunity(int amount) {
        venomImmunity += amount;
    }

    public void decrementPoisonImmunity(int amount) {
        poisonImmunity -= amount;
    }

    public void decrementVenomImmunity(int amount) {
        venomImmunity -= amount;
    }

    public boolean isAutoRetaliate() {
        return autoRetaliate;
    }

    public void setAutoRetaliate(boolean autoRetaliate) {
        this.autoRetaliate = autoRetaliate;
    }

    /**
     * @return the castSpell
     */
    public CombatSpell getCastSpell() {
        return castSpell;
    }

    /**
     * @param castSpell
     *            the castSpell to set
     */
    public void setCastSpell(CombatSpell castSpell) {
        this.castSpell = castSpell;
    }

    public CombatSpell getPreviousCastSpell() {
        return previousCastSpell;
    }

    public void setPreviousCastSpell(CombatSpell previousCastSpell) {
        this.previousCastSpell = previousCastSpell;
    }

    /**
     * @return the autocast
     */
    public boolean isAutocast() {
        return autocast;
    }

    /**
     * @param autocast
     *            the autocast to set
     */
    public void setAutocast(boolean autocast) {
        this.autocast = autocast;
    }

    /**
     * @return the skullTimer
     */
    public int getSkullTimer() {
        return skullTimer;
    }

    public boolean getPestControlSolo() {
        return pestControlSolo;
    }

    public void setPestControlSolo(boolean solo) {
        pestControlSolo = solo;
    }

    /**
     * @param skullTimer
     *            the skullTimer to set
     */
    public void setSkullTimer(int skullTimer) {
        this.skullTimer = skullTimer;
    }

    public void decrementSkullTimer() {
        skullTimer -= 50;
    }

    /**
     * @return the skullIcon
     */
    public int getSkullIcon() {
        return skullIcon;
    }

    /**
     * @param skullIcon
     *            the skullIcon to set
     */
    public void setSkullIcon(int skullIcon) {
        this.skullIcon = skullIcon;
    }

    /**
     * @return the teleblockTimer
     */
    public int getTeleblockTimer() {
        return teleblockTimer;
    }

    /**
     * @param teleblockTimer
     *            the teleblockTimer to set
     */
    public void setTeleblockTimer(int teleblockTimer) {
        this.teleblockTimer = teleblockTimer;
    }

    public void decrementTeleblockTimer() {
        teleblockTimer--;
    }

    /**
     * @return the autocastSpell
     */
    public CombatSpell getAutocastSpell() {
        return autocastSpell;
    }

    /**
     * @param autocastSpell
     *            the autocastSpell to set
     */
    public void setAutocastSpell(CombatSpell autocastSpell) {
        this.autocastSpell = autocastSpell;
    }

    /**
     * @return the specialPercentage
     */
    public int getSpecialPercentage() {
        return specialPercentage;
    }

    /**
     * @param specialPercentage
     *            the specialPercentage to set
     */
    public void setSpecialPercentage(int specialPercentage) {
        this.specialPercentage = specialPercentage;
    }

    /**
     * @return the fireAmmo
     */
    public int getFireAmmo() {
        return fireAmmo;
    }

    /**
     * @param fireAmmo
     *            the fireAmmo to set
     */
    public void setFireAmmo(int fireAmmo) {
        this.fireAmmo = fireAmmo;
    }

    public int getWildernessLevel() {
        return wildernessLevel;
    }

    public void setWildernessLevel(int wildernessLevel) {
        this.wildernessLevel = wildernessLevel;
    }

    /**
     * @return the combatSpecial
     */
    public CombatSpecial getCombatSpecial() {
        return combatSpecial;
    }

    /**
     * @param combatSpecial
     *            the combatSpecial to set
     */
    public void setCombatSpecial(CombatSpecial combatSpecial) {
        this.combatSpecial = combatSpecial;
    }

    /**
     * @return the specialActivated
     */
    public boolean isSpecialActivated() {
        return specialActivated;
    }

    /**
     * @param specialActivated
     *            the specialActivated to set
     */
    public void setSpecialActivated(boolean specialActivated) {
        this.specialActivated = specialActivated;
    }

    public void decrementSpecialPercentage(int drainAmount) {
        this.specialPercentage -= drainAmount;

        if (specialPercentage < 0) {
            specialPercentage = 0;
        }
    }

    public void incrementSpecialPercentage(int gainAmount) {
        this.specialPercentage += gainAmount;

        if (specialPercentage > 100) {
            specialPercentage = 100;
        }
    }

    /**
     * @return the rangedAmmo
     */
    public RangedWeaponData getRangedWeaponData() {
        return rangedWeaponData;
    }

    /**
     * the rangedAmmo to set
     */
    public void setRangedWeaponData(RangedWeaponData rangedWeaponData) {
        this.rangedWeaponData = rangedWeaponData;
    }

    /**
     * @return the weapon.
     */
    public WeaponInterface getWeapon() {
        return weapon;
    }

    public int getShieldId() {
        return equipment.get(Equipment.SHIELD_SLOT).getId();
    }

    /**
     * @param weapon
     *            the weapon to set.
     */
    public void setWeapon(WeaponInterface weapon) {
        this.weapon = weapon;
    }

    /**
     * @return the fightType
     */
    public FightType getFightType() {
        return fightType;
    }

    /**
     * @param fightType
     *            the fightType to set
     */
    public void setFightType(FightType fightType) {
        this.fightType = fightType;
    }

    public Bank[] getBanks() {
        return bankTabs;
    }

    public Bank getBank(int index) {
        return bankTabs[index];
    }

    public Player setBank(int index, Bank bank) {
        this.bankTabs[index] = bank;
        return this;
    }

    public boolean isAcceptAid() {
        return acceptingAid;
    }

    public void setAcceptAid(boolean acceptingAid) {
        this.acceptingAid = acceptingAid;
    }

    public Trading getTrading() {
        return trading;
    }

    public Dueling getDueling() {
        return dueling;
    }

    public CopyOnWriteArrayList<KillsEntry> getKillsTracker() {
        return killsTracker;
    }

    public CopyOnWriteArrayList<DropLogEntry> getDropLog() {
        return dropLog;
    }

    public void setWalkToTask(WalkToTask walkToTask) {
        this.walkToTask = walkToTask;
    }

    public WalkToTask getWalkToTask() {
        return walkToTask;
    }

    public Player setSpellbook(MagicSpellbook spellbook) {
        this.spellbook = spellbook;
        return this;
    }

    public MagicSpellbook getSpellbook() {
        return spellbook;
    }

    public Player setPrayerbook(Prayerbook prayerbook) {
        this.prayerbook = prayerbook;
        return this;
    }

    public Prayerbook getPrayerbook() {
        return prayerbook;
    }

    /**
     * The player's local players list.
     */
    public List<Player> getLocalPlayers() {
        return localPlayers;
    }

    /**
     * The player's local npcs list getter
     */
    public List<NPC> getLocalNpcs() {
        return localNpcs;
    }

    public Player setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
        return this;
    }

    public int getInterfaceId() {
        return this.interfaceId;
    }

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean isDying) {
        this.isDying = isDying;
    }

    public int[] getForceMovement() {
        return forceMovement;
    }

    public Player setForceMovement(int[] forceMovement) {
        this.forceMovement = forceMovement;
        return this;
    }

    /**
     * @return the equipmentAnimation
     */
    public CharacterAnimations getCharacterAnimations() {
        return characterAnimations;
    }

    /**
     * @return the equipmentAnimation
     */
    public void setCharacterAnimations(CharacterAnimations equipmentAnimation) {
        this.characterAnimations = equipmentAnimation.clone();
    }

    public LoyaltyTitles getLoyaltyTitle() {
        return loyaltyTitle;
    }

    public void setLoyaltyTitle(LoyaltyTitles loyaltyTitle) {
        this.loyaltyTitle = loyaltyTitle;
    }

    public void setWalkableInterfaceId(int interfaceId2) {
        this.walkableInterfaceId = interfaceId2;
    }

    public PlayerInteractingOption getPlayerInteractingOption() {
        return playerInteractingOption;
    }

    public Player setPlayerInteractingOption(PlayerInteractingOption playerInteractingOption) {
        this.playerInteractingOption = playerInteractingOption;
        return this;
    }

    public int getMultiIcon() {
        return multiIcon;
    }

    public Player setMultiIcon(int multiIcon) {
        this.multiIcon = multiIcon;
        return this;
    }

    public int getWalkableInterfaceId() {
        return walkableInterfaceId;
    }

    public boolean soundsActive() {
        return soundsActive;
    }

    public void setSoundsActive(boolean soundsActive) {
        this.soundsActive = soundsActive;
    }

    public boolean musicActive() {
        return musicActive;
    }

    public void setMusicActive(boolean musicActive) {
        this.musicActive = musicActive;
    }

    public BonusManager getBonusManager() {
        return bonusManager;
    }

    public float getRunEnergy() {
        return runEnergy;
    }

    public Player setRunEnergy(float runEnergy) {
        this.runEnergy = runEnergy;
        this.packetSender.sendRunEnergy();
        return this;
    }

    public Player setLoadRunEnery(float runEnergy) {
        this.runEnergy = runEnergy;
        return this;
    }

    public Stopwatch getLastRunRecovery() {
        return lastRunRecovery;
    }

    public Player setResting(boolean isResting) {
        this.isResting = isResting;
        return this;
    }

    public boolean isResting() {
        return isResting;
    }

    public void setMoneyInPouch(long moneyInPouch) {
        this.moneyInPouch = moneyInPouch;
    }

    public long getMoneyInPouch() {
        return moneyInPouch;
    }

    public int getMoneyInPouchAsInt() {
        return moneyInPouch > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) moneyInPouch;
    }

    public int loyaltyRank = 0;

    public int getLoyaltyRank() {
        return loyaltyRank;
    }

    public void setLoyaltyRank(int ii) {
        loyaltyRank = ii;
    }

    public boolean experienceLocked() {
        return experienceLocked;
    }

    public void setExperienceLocked(boolean experienceLocked) {
        this.experienceLocked = experienceLocked;
    }

    public boolean hasCompletedDailyTask() {
        return completedDailyTask;
    }

    public void setTaskCompletion(boolean completedDailyTask) {
        this.completedDailyTask = completedDailyTask;
    }

    public boolean showIpAddress() {
        return showIpAddressOnLogin;
    }

    public void setCapeChanges(boolean capeChanges) {
        this.capeChanges = capeChanges;
    }

    public void setShowHomeOnLogin(boolean showHomeOnLogin) {
        this.showHomeOnLogin = showHomeOnLogin;
    }

    public boolean showHomeOnLogin() {
        return showHomeOnLogin;
    }

    public void setShowIpAddressOnLogin(boolean showIpAddressOnLogin) {
        this.showIpAddressOnLogin = showIpAddressOnLogin;
    }

    public void setClientExitTaskActive(boolean clientExitTaskActive) {
        this.clientExitTaskActive = clientExitTaskActive;
    }

    public boolean isClientExitTaskActive() {
        return clientExitTaskActive;
    }

    public Player setCurrentClanChat(ClanChat clanChat) {
        this.currentClanChat = clanChat;
        return this;
    }

    public ClanChat getCurrentClanChat() {
        return currentClanChat;
    }

    public String getClanChatName() {
        return clanChatName;
    }

    public Player setClanChatName(String clanChatName) {
        this.clanChatName = clanChatName;
        return this;
    }

    public void setInputHandling(Input inputHandling) {
        this.inputHandling = inputHandling;
    }

    public Input getInputHandling() {
        return inputHandling;
    }

    public boolean isDrainingPrayer() {
        return drainingPrayer;
    }

    public void setDrainingPrayer(boolean drainingPrayer) {
        this.drainingPrayer = drainingPrayer;
    }

    public Stopwatch getClickDelay() {
        return clickDelay;
    }

    public Stopwatch getStuckDelay() {
        return stuckDelay;
    }

    public Stopwatch getForumDelay() {
        return forumDelay;
    }

    public int[] getLeechedBonuses() {
        return leechedBonuses;
    }

    public Stopwatch getLastItemPickup() {
        return lastItemPickup;
    }

    public Stopwatch getLastSummon() {
        return lastSummon;
    }

    public BankSearchAttributes getBankSearchingAttributes() {
        return bankSearchAttributes;
    }

    public AchievementAttributes getAchievementAttributes() {
        return achievementAttributes;
    }

    public BankPinAttributes getBankPinAttributes() {
        return bankPinAttributes;
    }

    public int getCurrentBankTab() {
        return currentBankTab;
    }

    public Player setCurrentBankTab(int tab) {
        this.currentBankTab = tab;
        return this;
    }

    public boolean isBanking() {
        return isBanking;
    }

    public Player setBanking(boolean isBanking) {
        this.isBanking = isBanking;
        return this;
    }

    public void setNoteWithdrawal(boolean noteWithdrawal) {
        this.noteWithdrawal = noteWithdrawal;
    }

    public boolean withdrawAsNote() {
        return noteWithdrawal;
    }

    public void setSwapMode(boolean swapMode) {
        this.swapMode = swapMode;
    }

    public boolean swapMode() {
        return swapMode;
    }

    public boolean isShopping() {
        return shopping;
    }

    public boolean isPlayerOwnedShopping() {
        return posShopping;
    }

    public void setShopping(boolean shopping) {
        this.shopping = shopping;
    }

    public void setPlayerOwnedShopping(boolean shopping) {
        this.posShopping = shopping;
    }

    public Shop getShop() {
        return shop;
    }

    public PlayerOwnedShopContainer getPlayerOwnedShop() {
        return player_owned_shop;
    }

    public Player setShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public Player setPlayerOwnedShop(PlayerOwnedShopContainer shop) {
        this.player_owned_shop = shop;
        return this;
    }

    public GameObject getInteractingObject() {
        return interactingObject;
    }

    public Player setInteractingObject(GameObject interactingObject) {
        this.interactingObject = interactingObject;
        return this;
    }

    public Item getInteractingItem() {
        return interactingItem;
    }

    public void setInteractingItem(Item interactingItem) {
        this.interactingItem = interactingItem;
    }

    public int getDialogueActionId() {
        return dialogueActionId;
    }

    public void setSettingUpCannon(boolean settingUpCannon) {
        this.settingUpCannon = settingUpCannon;
    }

    public boolean isSettingUpCannon() {
        return settingUpCannon;
    }

    public Player setCannon(DwarfCannon cannon) {
        this.cannon = cannon;
        return this;
    }

    public DwarfCannon getCannon() {
        return cannon;
    }

    public int getOverloadPotionTimer() {
        return overloadPotionTimer;
    }

    public void setOverloadPotionTimer(int overloadPotionTimer) {
        this.overloadPotionTimer = overloadPotionTimer;
    }

    public int getPrayerRenewalPotionTimer() {
        return prayerRenewalPotionTimer;
    }

    public void setPrayerRenewalPotionTimer(int prayerRenewalPotionTimer) {
        this.prayerRenewalPotionTimer = prayerRenewalPotionTimer;
    }

    public Stopwatch getSpecialRestoreTimer() {
        return specialRestoreTimer;
    }

    public boolean[] getUnlockedLoyaltyTitles() {
        return unlockedLoyaltyTitles;
    }

    public void setUnlockedLoyaltyTitles(boolean[] unlockedLoyaltyTitles) {
        this.unlockedLoyaltyTitles = unlockedLoyaltyTitles;
    }

    public void setUnlockedLoyaltyTitles(int index, boolean unlockedLoyaltyTitles) {
        this.unlockedLoyaltyTitles[index] = unlockedLoyaltyTitles;
    }

    public void setUnlockedLoyaltyTitle(int index) {
        unlockedLoyaltyTitles[index] = true;
    }

    public Stopwatch getEmoteDelay() {
        return emoteDelay;
    }

    public MinigameAttributes getMinigameAttributes() {
        return minigameAttributes;
    }

    public int getFireImmunity() {
        return fireImmunity;
    }

    public Player setFireImmunity(int fireImmunity) {
        this.fireImmunity = fireImmunity;
        return this;
    }

    public int getFireDamageModifier() {
        return fireDamageModifier;
    }

    public Player setFireDamageModifier(int fireDamageModifier) {
        this.fireDamageModifier = fireDamageModifier;
        return this;
    }

    public boolean hasVengeance() {
        return hasVengeance;
    }

    public void setHasVengeance(boolean hasVengeance) {
        this.hasVengeance = hasVengeance;
    }

    public Stopwatch getLastVengeance() {
        return lastVengeance;
    }

    public Stopwatch getLastLoot() {
        return lastLoot;
    }

    public Stopwatch getLastRoll() {
        return lastRoll;
    }

    public Stopwatch getLastAuthTime() {
        return lastAuth;
    }

    public Stopwatch getLastResource() {
        return lastResource;
    }

    public Stopwatch getTolerance() {
        return tolerance;
    }

    public boolean isTargeted() {
        return targeted;
    }

    public void setTargeted(boolean targeted) {
        this.targeted = targeted;
    }

    public int getAmountDonated() {
        return amountDonated;
    }

    public void setAmountDonated(int amtdon) {
        amountDonated = amtdon;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int points, boolean add) {
        if (add)
            this.credits += points;
        else
            this.credits = points;
    }

    public void incrementAmountDonated(int amountDonated) {
        this.amountDonated += amountDonated;
    }

    public void addCredits(int cred) {
        this.credits += cred;
    }

    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    public void setTotalPlayTime(long amount) {
        this.totalPlayTime = amount;
    }

    public Stopwatch getRecordedLogin() {
        return recordedLogin;
    }

    public Player setRegionChange(boolean regionChange) {
        this.regionChange = regionChange;
        return this;
    }

    public boolean isChangingRegion() {
        return this.regionChange;
    }

    public void setAllowRegionChangePacket(boolean allowRegionChangePacket) {
        this.allowRegionChangePacket = allowRegionChangePacket;
    }

    public boolean isAllowRegionChangePacket() {
        return allowRegionChangePacket;
    }

    public boolean isKillsTrackerOpen() {
        return killsTrackerOpen;
    }

    public boolean isQuestTabOpen() {
        return questTabOpen;
    }

    public void setKillsTrackerOpen(boolean killsTrackerOpen) {
        this.killsTrackerOpen = killsTrackerOpen;
    }

    public void setQuestTabOpen(boolean questTabOpene) {
        this.questTabOpen = questTabOpene;
    }

    public boolean isCoughing() {
        return isCoughing;
    }

    public void setCoughing(boolean isCoughing) {
        this.isCoughing = isCoughing;
    }

    public int getShadowState() {
        return shadowState;
    }

    public void setShadowState(int shadow) {
        this.shadowState = shadow;
    }

    public boolean isPlayerLocked() {
        return playerLocked;
    }

    public Player setPlayerLocked(boolean playerLocked) {
        this.playerLocked = playerLocked;
        return this;
    }

    public Stopwatch getYellTimer() {
        return yellTimer;
    }

    public Stopwatch getQuickChat() {
        return quickChat;
    }

    public Stopwatch getVoteTimer() {
        return voteTimer;
    }

    public Stopwatch getTabTimer() {
        return tabTimer;
    }

    public Stopwatch getDragonScimitarTimer() {
        return dragon_scimitar_timer;
    }

    public Stopwatch getSqlTimer() {
        return sqlTimer;
    }

    public Stopwatch getSpecTimer() {
        return specTimer;
    }

    public Stopwatch getFoodTimer() {
        return foodTimer;
    }

    public Stopwatch getComboFoodDelay() {
        return comboFoodDelay;
    }

    public Stopwatch getPotionTimer() {
        return potionTimer;
    }

    public Item getUntradeableDropItem() {
        return untradeableDropItem;
    }

    public void setUntradeableDropItem(Item untradeableDropItem) {
        this.untradeableDropItem = untradeableDropItem;
    }

    public boolean isRecoveringSpecialAttack() {
        return recoveringSpecialAttack;
    }

    public void setRecoveringSpecialAttack(boolean recoveringSpecialAttack) {
        this.recoveringSpecialAttack = recoveringSpecialAttack;
    }

    public CombatType getLastCombatType() {
        return lastCombatType;
    }

    public void setLastCombatType(CombatType lastCombatType) {
        this.lastCombatType = lastCombatType;
    }

    public int getEffigy() {
        return this.effigy;
    }

    public void setEffigy(int effigy) {
        this.effigy = effigy;
    }

    public int getDfsCharges() {
        return dfsCharges;
    }

    public void incrementDfsCharges(int amount) {
        this.dfsCharges += amount;
    }

    public void setNewPlayer(boolean newPlayer) {
        this.newPlayer = newPlayer;
    }

    public boolean newPlayer() {
        return newPlayer;
    }

    public Stopwatch getLogoutTimer() {
        return lougoutTimer;
    }

    public Stopwatch getDragonfireShield() {
        return dragonfireShield;
    }

    public Stopwatch getDragonSpear() {
        return dragonSpear;
    }

    public Stopwatch getDuelTimer() {
        return duelTimer;
    }

    public Stopwatch getSummoningTimer() {
        return summoningTimer;
    }

    public Player setUsableObject(Object[] usableObject) {
        this.usableObject = usableObject;
        return this;
    }

    public Player setUsableObject(int index, Object usableObject) {
        this.usableObject[index] = usableObject;
        return this;
    }

    public Object[] getUsableObject() {
        return usableObject;
    }

    public int getPlayerViewingIndex() {
        return playerViewingIndex;
    }

    public void setPlayerViewingIndex(int playerViewingIndex) {
        this.playerViewingIndex = playerViewingIndex;
    }

    public boolean hasStaffOfLightEffect() {
        return staffOfLightEffect > 0;
    }

    public int getStaffOfLightEffect() {
        return staffOfLightEffect;
    }

    public void setStaffOfLightEffect(int staffOfLightEffect) {
        this.staffOfLightEffect = staffOfLightEffect;
    }

    public void decrementStaffOfLightEffect() {
        this.staffOfLightEffect--;
    }

    public void decrementForumConnections() {
        this.forum_connections--;
    }

    public boolean openBank() {
        return openBank;
    }

    public boolean continueTutorial() {
        return tutorialContinue;
    }

    public boolean continueSkipTutorial() {
        return skipTutorialContinue;
    }

    public boolean continueLoginAccountPin() {
        return loginAccountPin;
    }

    public void setOpenBank(boolean openBank) {
        this.openBank = openBank;
    }

    public void setContinueSkipTutorial(boolean b) {
        this.skipTutorialContinue = b;
    }

    public void setLoginAccountPin(boolean b) {
        this.loginAccountPin = b;
    }

    private boolean passwordChanging;

    public void setPasswordChanging(boolean b) {
        this.passwordChanging = b;
    }

    public boolean getPasswordChanging() {
        return passwordChanging;
    }

    public void setContinueTutorial(boolean tut) {
        this.tutorialContinue = tut;
    }

    public int getMinutesBonusExp() {
        return minutesBonusExp;
    }

    public void setMinutesBonusExp(int minutesBonusExp, boolean add) {
        this.minutesBonusExp = (add ? this.minutesBonusExp + minutesBonusExp : minutesBonusExp);
    }

    public void setInactive(boolean inActive) {
        this.inActive = inActive;
    }

    public boolean isInActive() {
        return inActive;
    }

    public int getSelectedGeItem() {
        return selectedGeItem;
    }

    public void setSelectedGeItem(int selectedGeItem) {
        this.selectedGeItem = selectedGeItem;
    }

    public int getGeQuantity() {
        return geQuantity;
    }

    public void setGeQuantity(int geQuantity) {
        this.geQuantity = geQuantity;
    }

    public int getGePricePerItem() {
        return gePricePerItem;
    }

    public void setGePricePerItem(int gePricePerItem) {
        this.gePricePerItem = gePricePerItem;
    }

    public void setSelectedGeSlot(int slot) {
        this.selectedGeSlot = slot;
    }

    public int getSelectedGeSlot() {
        return selectedGeSlot;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public int getSelectedSkillingItem() {
        return selectedSkillingItem;
    }

    public void setSelectedSkillingItem(int selectedItem) {
        this.selectedSkillingItem = selectedItem;
    }

    public boolean shouldProcessFarming() {
        return processFarming;
    }

    public void setProcessFarming(boolean processFarming) {
        this.processFarming = processFarming;
    }

    public Pouch getSelectedPouch() {
        return selectedPouch;
    }

    public void setSelectedPouch(Pouch selectedPouch) {
        this.selectedPouch = selectedPouch;
    }

    public int getCurrentBookPage() {
        return currentBookPage;
    }

    public void setCurrentBookPage(int currentBookPage) {
        this.currentBookPage = currentBookPage;
    }

    public int getStoredRuneEssence() {
        return storedRuneEssence;
    }

    public void setStoredRuneEssence(int storedRuneEssence) {
        this.storedRuneEssence = storedRuneEssence;
    }

    public int getStoredPureEssence() {
        return storedPureEssence;
    }

    public void setStoredPureEssence(int storedPureEssence) {
        this.storedPureEssence = storedPureEssence;
    }

    public int getTrapsLaid() {
        return trapsLaid;
    }

    public void setTrapsLaid(int trapsLaid) {
        this.trapsLaid = trapsLaid;
    }

    public boolean isCrossingObstacle() {
        return crossingObstacle;
    }

    public Player setCrossingObstacle(boolean crossingObstacle) {
        this.crossingObstacle = crossingObstacle;
        return this;
    }

    public boolean[] getCrossedObstacles() {
        return crossedObstacles;
    }

    public boolean getCrossedObstacle(int i) {
        return crossedObstacles[i];
    }

    public Player setCrossedObstacle(int i, boolean completed) {
        crossedObstacles[i] = completed;
        return this;
    }

    public void setCrossedObstacles(boolean[] crossedObstacles) {
        this.crossedObstacles = crossedObstacles;
    }

    public void setCrossedObstacles(int index, boolean crossedObstacles) {
        this.crossedObstacles[index] = crossedObstacles;
    }

    public int getSkillAnimation() {
        return skillAnimation;
    }

    public Player setSkillAnimation(int animation) {
        this.skillAnimation = animation;
        return this;
    }

    public int[] getOres() {
        return ores;
    }

    public void setOres(int[] ores) {
        this.ores = ores;
    }

    public void setResetPosition(Position resetPosition) {
        this.resetPosition = resetPosition;
    }

    public Position getResetPosition() {
        return resetPosition;
    }

    public Slayer getSlayer() {
        return slayer;
    }

    public Summoning getSummoning() {
        return summoning;
    }

    public Farming getFarming() {
        return farming;
    }

    public boolean inConstructionDungeon() {
        return inConstructionDungeon;
    }

    public void setInConstructionDungeon(boolean inConstructionDungeon) {
        this.inConstructionDungeon = inConstructionDungeon;
    }

    public int getHouseServant() {
        return houseServant;
    }

    public void setHouseServant(int houseServant) {
        this.houseServant = houseServant;
    }

    public int getHouseServantCharges() {
        return this.houseServantCharges;
    }

    public void setHouseServantCharges(int houseServantCharges) {
        this.houseServantCharges = houseServantCharges;
    }

    public void incrementHouseServantCharges() {
        this.houseServantCharges++;
    }

    public int getServantItemFetch() {
        return servantItemFetch;
    }

    public void setServantItemFetch(int servantItemFetch) {
        this.servantItemFetch = servantItemFetch;
    }

    public int getPortalSelected() {
        return portalSelected;
    }

    public void setPortalSelected(int portalSelected) {
        this.portalSelected = portalSelected;
    }

    public boolean isBuildingMode() {
        return this.isBuildingMode;
    }

    public void setIsBuildingMode(boolean isBuildingMode) {
        this.isBuildingMode = isBuildingMode;
    }

    public int[] getConstructionCoords() {
        return constructionCoords;
    }

    public void setConstructionCoords(int[] constructionCoords) {
        this.constructionCoords = constructionCoords;
    }

    public int getBuildFurnitureId() {
        return this.buildFurnitureId;
    }

    public void setBuildFuritureId(int buildFuritureId) {
        this.buildFurnitureId = buildFuritureId;
    }

    public int getBuildFurnitureX() {
        return this.buildFurnitureX;
    }

    public void setBuildFurnitureX(int buildFurnitureX) {
        this.buildFurnitureX = buildFurnitureX;
    }

    public int getBuildFurnitureY() {
        return this.buildFurnitureY;
    }

    public void setBuildFurnitureY(int buildFurnitureY) {
        this.buildFurnitureY = buildFurnitureY;
    }

    public int getCombatRingType() {
        return this.combatRingType;
    }

    public int getForumConnections() {
        return this.forum_connections;
    }

    public int getForumConnectionsRank() {
        return this.forum_connections_rank;
    }

    public void addForumConnections(int abcc) {
        this.forum_connections += abcc;
    }

    public void setForumConnections(int abccc) {
        this.forum_connections_rank = abccc;
    }

    public void setForumConnectionsRank(int abccc) {
        this.forum_connections_rank = abccc;
    }

    public void setCombatRingType(int combatRingType) {
        this.combatRingType = combatRingType;
    }

    public int getConstructionInterface() {
        return this.constructionInterface;
    }

    public void setConstructionInterface(int constructionInterface) {
        this.constructionInterface = constructionInterface;
    }

    public int[] getBrawlerChargers() {
        return this.brawlerCharges;
    }

    public void setBrawlerCharges(int[] brawlerCharges) {
        this.brawlerCharges = brawlerCharges;
    }

    public void setBrawlerCharges(int brawlerCharges, int index) {
        this.brawlerCharges[index] = brawlerCharges;
    }

    public int getRecoilCharges() {
        return this.recoilCharges;
    }

    public int setRecoilCharges(int recoilCharges) {
        return this.recoilCharges = recoilCharges;
    }

    public boolean voteMessageSent() {
        return this.voteMessageSent;
    }

    public void setVoteMessageSent(boolean voteMessageSent) {
        this.voteMessageSent = voteMessageSent;
    }

    public boolean didReceiveStarter() {
        return receivedStarter;
    }

    public void setReceivedStarter(boolean receivedStarter) {
        this.receivedStarter = receivedStarter;
    }

    private boolean drankQuestPotion = false;

    public void drankBraverly(boolean potion) {
        drankQuestPotion = potion;
    }

    public boolean getDrankBraverly() {
        return drankQuestPotion;
    }

    public boolean bossingSystem = false;

    public boolean inBossingSystem() {
        return bossingSystem;
    }

    public void setBossingSystem(boolean bossSystem) {
        bossingSystem = bossSystem;
    }

    public boolean hasAnnouncedMax() {
        return maxAnnounce;
    }

    public void setAnnounceMax(boolean max) {
        maxAnnounce = max;
    }

    public boolean maxAnnounce = false;

    public String dice_other_name = "";

    public int dice_other_amount = 32;

    public boolean dice_other = false;

    public boolean boost_stats = false;

    public int gambler_id = 0;

    private boolean hasNext;

    public boolean hasNext() {
        return hasNext;
    }

    public void setHasNext(boolean b) {
        this.hasNext = b;
    }

    public int[] getVestaCharges() {
        return vestaCharges;
    }

    public void setVestaCharges(int[] vestaCharges) {
        this.vestaCharges = vestaCharges;
    }

    public int[] getStatiusCharges() {
        return statiusCharges;
    }

    public void setStatiusCharges(int[] statiusCharges) {
        this.statiusCharges = statiusCharges;
    }

    public int[] getZurielsCharges() {
        return zurielsCharges;
    }

    public void setZurielsCharges(int[] zurielsCharges) {
        this.zurielsCharges = zurielsCharges;
    }

    public int[] getMorrigansCharges() {
        return morrigansCharges;
    }

    public void setMorrigansCharges(int[] morrigansCharges) {
        this.morrigansCharges = morrigansCharges;
    }

    public int setVestaCharges(int index, int charges) {
        return this.vestaCharges[index] = charges;
    }

    public int setStatiusCharges(int index, int charges) {
        return this.statiusCharges[index] = charges;
    }

    public int setZurielsCharges(int index, int charges) {
        return this.zurielsCharges[index] = charges;
    }

    public int setMorrigansCharges(int index, int charges) {
        return this.morrigansCharges[index] = charges;
    }

    public int[] getCorruptVestaCharges() {
        return corruptVestaCharges;
    }

    public void setCorruptVestaCharges(int[] corruptVestaCharges) {
        this.corruptVestaCharges = corruptVestaCharges;
    }

    public int[] getCorruptStatiusCharges() {
        return corruptStatiusCharges;
    }

    public void setCorruptStatiusCharges(int[] corruptStatiusCharges) {
        this.corruptStatiusCharges = corruptStatiusCharges;
    }

    public int[] getCorruptZurielsCharges() {
        return corruptZurielsCharges;
    }

    public void setCorruptZurielsCharges(int[] corruptZurielsCharges) {
        this.corruptZurielsCharges = corruptZurielsCharges;
    }

    public int[] getCorruptMorrigansCharges() {
        return corruptMorrigansCharges;
    }

    public void setCorruptMorrigansCharges(int[] corruptMorrigansCharges) {
        this.corruptMorrigansCharges = corruptMorrigansCharges;
    }

    public int getBarrowsChestsLooted() {
        return barrowsChestsLooted;
    }

    public int getBarrowsChestRewards() {
        return barrowsChestRewards;
    }

    public int setCorruptVestaCharges(int index, int charges) {
        return this.corruptVestaCharges[index] = charges;
    }

    public int setCorruptStatiusCharges(int index, int charges) {
        return this.corruptStatiusCharges[index] = charges;
    }

    public int setCorruptZurielsCharges(int index, int charges) {
        return this.corruptZurielsCharges[index] = charges;
    }

    public int setCorruptMorrigansCharges(int index, int charges) {
        return this.corruptMorrigansCharges[index] = charges;
    }
}