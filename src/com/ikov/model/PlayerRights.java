package com.ikov.model;

import java.util.Arrays;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.ikov.util.Misc;

public enum PlayerRights {

	PLAYER(),
	MODERATOR(),
	ADMINISTRATOR(MODERATOR),
	OWNER(ADMINISTRATOR, MODERATOR),
	SUPPORT(),
	YOUTUBER(),
	GLOBAL_MOD(MODERATOR),
	COMMUNITY_MANAGER(),
	WIKI_EDITOR(),
	WIKI_MANAGER(),
	STAFF_MANAGER();

	PlayerRights(PlayerRights...inherited) {
		this.inherited = inherited;
	}
	
	public static final ImmutableSet<PlayerRights> STAFF = Sets.immutableEnumSet(SUPPORT, MODERATOR, ADMINISTRATOR, OWNER, GLOBAL_MOD, COMMUNITY_MANAGER, STAFF_MANAGER);
	
	public boolean isStaff() {
		return STAFF.contains(this);
	}
	
	private final PlayerRights[] inherited;
	
	public boolean inherits(PlayerRights rights) {
		return equals(rights) || Arrays.asList(inherited).contains(rights) || rights.equals(PLAYER) || equals(MODERATOR) && rights.equals(SUPPORT);
	}
	
	public String getFormatedName() {
		return Misc.toTitleCase(toString());
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
