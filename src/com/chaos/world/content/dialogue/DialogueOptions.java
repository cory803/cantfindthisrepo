package com.chaos.world.content.dialogue;

import com.chaos.model.*;
import com.chaos.model.definitions.ItemDefinition;
import com.chaos.world.content.*;
import com.chaos.world.entity.impl.player.Player;

public class DialogueOptions {

	/**
	 * 5k Vengeance
	 */

	public static int DEATH_RUNE = 560;
	public static int DEATH_RUNE_PRICE = ItemDefinition.forId(DEATH_RUNE).getValue() * 5000;
	public static int EARTH_RUNE = 557;
	public static int EARTH_RUNE_PRICE = ItemDefinition.forId(EARTH_RUNE).getValue() * 5000;
	public static int ASTRAL_RUNE = 9075;
	public static int ASTRAL_RUNE_PRICE = ItemDefinition.forId(ASTRAL_RUNE).getValue() * 5000;

	/**
	 * 5k Barrage
	 */

	public static int WATER_RUNE = 555;
	public static int WATER_RUNE_PRICE = ItemDefinition.forId(WATER_RUNE).getValue() * 5000;
	public static int BLOOD_RUNE = 565;
	public static int BLOOD_RUNE_PRICE = ItemDefinition.forId(BLOOD_RUNE).getValue() * 5000;

	/**
	 * 10k Elemental Runes
	 */

	public static int AIR_RUNE = 556;
	public static int AIR_RUNE_PRICE = ItemDefinition.forId(AIR_RUNE).getValue() * 10000;
	public static int FIRE_RUNE = 554;
	public static int FIRE_RUNE_PRICE = ItemDefinition.forId(FIRE_RUNE).getValue() * 10000;
	public static int EARTH_RUNE_PRICE2 = ItemDefinition.forId(EARTH_RUNE).getValue() * 10000;
	public static int WATER_RUNE_PRICE2 = ItemDefinition.forId(WATER_RUNE).getValue() * 10000;

	/**
	 * God Books
	 */

	public static int GUTHIX = 3844;
	public static int SARADOMIN = 3840;
	public static int ZAMORAK = 3842;
	public static int ARMADYL = 19615;
	public static int BANDOS = 19613;
	public static int ZAROS = 19617;

	// Last id used = 78

	public static void handle(Player player, int id) {
		if (player.getRights() == PlayerRights.DEVELOPER) {
			player.getPacketSender()
					.sendMessage("Dialogue button id: " + id + ", action id: " + player.getDialogueActionId())
					.sendConsoleMessage("Dialogue button id: " + id + ", action id: " + player.getDialogueActionId());
		}
		if (Effigies.handleEffigyAction(player, id)) {
			return;
		}
		if (id == FIRST_OPTION_OF_FIVE) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == SECOND_OPTION_OF_FIVE) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == THIRD_OPTION_OF_FIVE) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == FOURTH_OPTION_OF_FIVE) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == FIFTH_OPTION_OF_FIVE) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == FIRST_OPTION_OF_FOUR) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == SECOND_OPTION_OF_FOUR) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == THIRD_OPTION_OF_FOUR) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == FOURTH_OPTION_OF_FOUR) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == FIRST_OPTION_OF_TWO) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == SECOND_OPTION_OF_TWO) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == SECOND_OPTION_OF_THREE) {
			switch (player.getDialogueActionId()) {

			}
		} else if (id == THIRD_OPTION_OF_THREE) {
			switch (player.getDialogueActionId()) {

			}
		}
	}

	public static int FIRST_OPTION_OF_FIVE = 2494;
	public static int SECOND_OPTION_OF_FIVE = 2495;
	public static int THIRD_OPTION_OF_FIVE = 2496;
	public static int FOURTH_OPTION_OF_FIVE = 2497;
	public static int FIFTH_OPTION_OF_FIVE = 2498;

	public static int FIRST_OPTION_OF_FOUR = 2482;
	public static int SECOND_OPTION_OF_FOUR = 2483;
	public static int THIRD_OPTION_OF_FOUR = 2484;
	public static int FOURTH_OPTION_OF_FOUR = 2485;

	public static int FIRST_OPTION_OF_THREE = 2471;
	public static int SECOND_OPTION_OF_THREE = 2472;
	public static int THIRD_OPTION_OF_THREE = 2473;

	public static int FIRST_OPTION_OF_TWO = 2461;
	public static int SECOND_OPTION_OF_TWO = 2462;

}
