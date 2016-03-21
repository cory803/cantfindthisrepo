package com.ikov.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;


/**
 * Represents a player's privilege rights.
 * @author Gabriel Hannason
 */

public enum PlayerRights {

	/*
	 * A regular member of the server.
	 */
	PLAYER(-1, null, 1, 1),
	/*
	 * A moderator who has more privilege than other regular members and donators.
	 */
	MODERATOR(-1, "<col=20B2AA><shad=0>", 1, 1),

	/*
	 * The second-highest-privileged member of the server.
	 */
	ADMINISTRATOR(-1, "<col=FFFF64><shad=0>", 1, 1),

	/*
	 * The highest-privileged member of the server
	 */
	OWNER(-1, "<col=B40404>", 1, 1),
	
	/*
	 * A member who has the ability to help people better.
	 */
	SUPPORT(-1, "<col=FF0000><shad=0>", 1, 1),
	
	/*
	 * A member who is a YouTuber.
	 */
	YOUTUBER(-1, "<col=EE0101><shad=891E19>", 1, 1),
	
	/*
	 * A member who is a Global Moderator.
	 */
	GLOBAL_MOD(-1, "<col=EE0101><shad=891E19>", 1, 1),
	
	/*
	 * A member of staff who is a Community Manager
	 */
	COMMUNITY_MANAGER(-1, "<col=EE0101><shad=891E19>", 1, 1);

	PlayerRights(int yellDelaySeconds, String yellHexColorPrefix, double loyaltyPointsGainModifier, double experienceGainModifier) {
		this.yellDelay = yellDelaySeconds;
		this.yellHexColorPrefix = yellHexColorPrefix;
		this.loyaltyPointsGainModifier = loyaltyPointsGainModifier;
		this.experienceGainModifier = experienceGainModifier;
	}

	private static final ImmutableSet<PlayerRights> YT = Sets.immutableEnumSet(YOUTUBER);
	private static final ImmutableSet<PlayerRights> STAFF = Sets.immutableEnumSet(SUPPORT, MODERATOR, ADMINISTRATOR, OWNER, GLOBAL_MOD, COMMUNITY_MANAGER);
	private static final ImmutableSet<PlayerRights> CC = Sets.immutableEnumSet(OWNER);	
	
	/*
	 * The yell delay for the rank
	 * The amount of seconds the player with the specified rank must wait before sending another yell message.
	 */
	private int yellDelay;
	private String yellHexColorPrefix;
	private double loyaltyPointsGainModifier;
	private double experienceGainModifier;
	
	public int getYellDelay() {
		return yellDelay;
	}
	
	/*
	 * The player's yell message prefix.
	 * Color and shadowing.
	 */
	
	public String getYellPrefix() {
		return yellHexColorPrefix;
	}
	
	/**
	 * The amount of loyalty points the rank gain per 4 seconds
	 */
	public double getLoyaltyPointsGainModifier() {
		return loyaltyPointsGainModifier;
	}
	
	public double getExperienceGainModifier() {
		return experienceGainModifier;
	}

	public boolean isStaff() {
		return STAFF.contains(this);
	}
	
	public boolean isYouTuber() {
		return YT.contains(this);
	}
	
	public boolean ownerInCC() {
		return CC.contains(this);
	}
	
	/**
	 * Gets the rank for a certain id.
	 * 
	 * @param id	The id (ordinal()) of the rank.
	 * @return		rights.
	 */
	public static PlayerRights forId(int id) {
		for (PlayerRights rights : PlayerRights.values()) {
			if (rights.ordinal() == id) {
				return rights;
			}
		}
		return null;
	}
}
