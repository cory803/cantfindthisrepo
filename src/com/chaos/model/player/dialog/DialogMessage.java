package com.chaos.model.player.dialog;

import com.chaos.model.options.Option;

public final class DialogMessage {

	public DialogMessage(DialogType type, String ...lines) {
		this.type = type;
		this.lines = lines;
	}

	public DialogMessage(Option option) {
		this.type = DialogType.OPTION;
		this.option = option;
	}

	/**
	 * Returns the lines of the dialog message.
	 * @return
	 */
	public String[] getLines() {
		return this.lines;
	}

	/**
	 * Returns the {@link DialogType} of the message.
	 * @return
	 */
	public DialogType getType() {
		return this.type;
	}

	/**
	 * Returns the option for the message.
	 * @return
	 */
	public Option getOption() {
		return this.option;
	}

	private Option option;
	private String[] lines;
	private DialogType type;

}