package com.chaos.world.content.dialogue;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.chaos.GameSettings;
import com.chaos.model.definitions.NpcDefinition;
import com.chaos.util.JsonLoader;
import com.chaos.world.content.BankPin;
import com.chaos.world.content.dialogue.impl.Tutorial;
import com.chaos.world.entity.impl.player.Player;

/**
 * Manages the loading and start of dialogues.
 * 
 * @author relex lawl
 */

public class DialogueManager {

	/**
	 * Contains all dialogues loaded from said file.
	 */
	public static Map<Integer, Dialogue> dialogues = new HashMap<Integer, Dialogue>();

	/**
	 * Parses the information from the dialogue file.
	 */
	public static JsonLoader parseDialogues() {

		return new JsonLoader() {
			@Override
			public void load(JsonObject reader, Gson builder) {

				final int id = reader.get("id").getAsInt();
				final DialogueType type = DialogueType.valueOf(reader.get("type").getAsString());
				final DialogueExpression anim = reader.has("anim")
						? DialogueExpression.valueOf(reader.get("anim").getAsString()) : null;
				final int lines = reader.get("lines").getAsInt();
				String[] dialogueLines = new String[lines];
				for (int i = 0; i < lines; i++) {
					dialogueLines[i] = reader.get("line" + (i + 1)).getAsString();
				}
				final int next = reader.get("next").getAsInt();
				final int npcId = reader.has("npcId") ? reader.get("npcId").getAsInt() : -1;

				Dialogue dialogue = new Dialogue() {
					@Override
					public int id() {
						return id;
					}

					@Override
					public DialogueType type() {
						return type;
					}

					@Override
					public DialogueExpression animation() {
						return anim;
					}

					@Override
					public String[] dialogue() {
						return dialogueLines;
					}

					@Override
					public int nextDialogueId() {
						return next;
					}

					@Override
					public int npcId() {
						return npcId;
					}

					@Override
					public String[] item() {
						return null;
					}
				};
				dialogues.put(id, dialogue);

			}

			@Override
			public String filePath() {
				return "./data/def/json/dialogues.json";
			}
		};

	}

	/**
	 * Starts a dialogue gotten from the dialogues map.
	 * 
	 * @param player
	 *            The player to dialogue with.
	 * @param id
	 *            The id of the dialogue to retrieve from dialogues map.
	 */
	public static void start(Player player, int id) {
		Dialogue dialogue = dialogues.get(id);
		start(player, dialogue);
	}

	/**
	 * Starts a dialogue.
	 * 
	 * @param player
	 *            The player to dialogue with.
	 * @param dialogue
	 *            The dialogue to show the player.
	 */
	public static void start(Player player, Dialogue dialogue) {
		player.setDialogue(dialogue);
		if (player.isBanking() || player.isShopping() || player.getInterfaceId() > 0 && player.getInterfaceId() != 50)
			player.getPacketSender().sendInterfaceRemoval();
		if (dialogue == null || dialogue.id() < 0) {
			player.getPacketSender().sendInterfaceRemoval();
		} else {
			showDialogue(player, dialogue);
			dialogue.specialAction();
		}
		if (player.getInterfaceId() != 50)
			player.setInterfaceId(50);
	}

	/**
	 * Handles the clicking of 'click here to continue', option1, option2 and so
	 * on.
	 * 
	 * @param player
	 *            The player who will continue the dialogue.
	 */
	public static void next(Player player) {
		if (player.getDialogue() == null) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Dialogue next = player.getDialogue().nextDialogue();
		if (next == null)
			next = dialogues.get(player.getDialogue().nextDialogueId());
		if ((next == null || next.id() < 0) && player.getDialogue().specialValue() < 500) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		if (player.getDialogue().specialValue() == 500 && !player.getBankPinAttributes().hasBankPin()) {
			BankPin.init(player, false);
		} else {
			if (next.id() == 7 && !player.getBankPinAttributes().hasBankPin() && next != null) {
				BankPin.init(player, false);
			} else {
				start(player, next);
			}
		}
	}

	/**
	 * Handles the clicking of 'click here to continue', option1, option2 and so
	 * on.
	 * 
	 * @param player
	 *            The player who will continue the dialogue.
	 */
	public static void tutorialDialogue(Player player) {
		if (player.getDialogue() == null) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Dialogue next = Tutorial.get(player, 15);
		if (next == null)
			next = dialogues.get(player.getDialogue().nextDialogueId());
		if (next == null || next.id() < 0) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		start(player, next);
	}

	/**
	 * Configures the dialogue's type and shows the dialogue interface and sets
	 * its child id's.
	 * 
	 * @param player
	 *            The player to show dialogue for.
	 * @param dialogue
	 *            The dialogue to show.
	 */
	private static void showDialogue(Player player, Dialogue dialogue) {
		String[] lines = dialogue.dialogue();
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].equals("Try again in 0 minutes.")) {
				if (player.gambler_id == 1) {
					lines[i] = "Try again in " + GameSettings.gambler_timer_1 / 2 / 60 + " minutes.";
				}
				if (player.gambler_id == 2) {
					lines[i] = "Try again in " + GameSettings.gambler_timer_2 / 2 / 60 + " minutes.";
				}
			}
			if (lines[i].contains("Zulrah's scales to your Toxic staff (uncharged)")) {
				int amount_of_scales = player.getInventory().getAmount(21080);
				if (amount_of_scales + player.getToxicStaffCharges() > 11000) {
					amount_of_scales = 11000 - player.getToxicStaffCharges();
				}
				lines[i] = "Add " + amount_of_scales + " Zulrah's scales to your Toxic staff (uncharged)";
			}
			if (lines[i].contains("Set my yell tag (500M)")) {
				int value = 500000000;
				if (player.getDonorRights() == 1) {
					value = 500;
				} else if (player.getDonorRights() == 2) {
					value = 400;
				} else if (player.getDonorRights() == 3) {
					value = 300;
				} else if (player.getDonorRights() == 4) {
					value = 200;
				} else if (player.getDonorRights() == 5) {
					value = 100;
				}
				lines[i] = "Set my yell tag (" + value + "M)";
			}
		}
		switch (dialogue.type()) {
		case NPC_STATEMENT:
			int startDialogueChildId = NPC_DIALOGUE_ID[lines.length - 1];
			int headChildId = startDialogueChildId - 2;
			player.getPacketSender().sendNpcHeadOnInterface(dialogue.npcId(), headChildId);
			player.getPacketSender().sendInterfaceAnimation(headChildId, dialogue.animation().getAnimation());
			player.getPacketSender().sendString(startDialogueChildId - 1, NpcDefinition.forId(dialogue.npcId()) != null ? NpcDefinition.forId(dialogue.npcId()).getName().replaceAll("_", " ") : "");
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
			break;
		case PLAYER_STATEMENT:
			startDialogueChildId = PLAYER_DIALOGUE_ID[lines.length - 1];
			headChildId = startDialogueChildId - 2;
			player.getPacketSender().sendPlayerHeadOnInterface(headChildId);
			player.getPacketSender().sendInterfaceAnimation(headChildId, dialogue.animation().getAnimation());
			player.getPacketSender().sendString(startDialogueChildId - 1, player.getUsername());
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
			break;
		case ITEM_STATEMENT:
			startDialogueChildId = NPC_DIALOGUE_ID[lines.length - 1];
			headChildId = startDialogueChildId - 2;
			player.getPacketSender().sendInterfaceModel(headChildId, Integer.valueOf(dialogue.item()[0]),
					Integer.valueOf(dialogue.item()[1]));
			player.getPacketSender().sendString(startDialogueChildId - 1, dialogue.item()[2]);
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
			break;
		case STATEMENT:
			sendStatement(player, dialogue.dialogue()[0]);
			break;
		case OPTION:
			int firstChildId = OPTION_DIALOGUE_ID[lines.length - 1];
			player.getPacketSender().sendString(firstChildId - 1, "Choose an option");
			for (int i = 0; i < lines.length; i++) {
				player.getPacketSender().sendString(firstChildId + i, lines[i]);
			}
			player.getPacketSender().sendChatboxInterface(firstChildId - 2);
			break;
		}
		if (player.getInterfaceId() <= 0)
			player.setInterfaceId(100);
	}

	public static void sendStatement(Player p, String statement) {
		p.getPacketSender().sendString(357, statement);
		p.getPacketSender().sendString(358, "Click here to continue");
		p.getPacketSender().sendChatboxInterface(356);
	}

	/**
	 * Gets an empty id for a dialogue.
	 * 
	 * @return An empty index from the map or the map's size itself.
	 */
	public static int getDefaultId() {
		int id = dialogues.size();
		for (int i = 0; i < dialogues.size(); i++) {
			if (dialogues.get(i) == null) {
				id = i;
				break;
			}
		}
		return id;
	}

	/**
	 * Retrieves the dialogues map.
	 * 
	 * @return dialogues.
	 */
	public static Map<Integer, Dialogue> getDialogues() {
		return dialogues;
	}

	/**
	 * This array contains the child id where the dialogue statement starts for
	 * npc and item dialogues.
	 */
	private static final int[] NPC_DIALOGUE_ID = { 4885, 4890, 4896, 4903 };

	/**
	 * This array contains the child id where the dialogue statement starts for
	 * player dialogues.
	 */
	private static final int[] PLAYER_DIALOGUE_ID = { 971, 976, 982, 989 };

	/**
	 * This array contains the child id where the dialogue statement starts for
	 * option dialogues.
	 */
	private static final int[] OPTION_DIALOGUE_ID = { 13760, 2461, 2471, 2482, 2494, };
}