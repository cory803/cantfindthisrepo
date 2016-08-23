package com.chaos.model.options.fiveoption;

import com.chaos.model.options.Option;
import com.chaos.world.entity.impl.player.Player;

public abstract class FiveOption extends Option {
	private final String firstOption;
	private final String secondOption;
	private final String thirdOption;
	private final String fourthOption;
	private final String fifthOption;

	protected FiveOption(String firstOption, String secondOption, String thirdOption, String fourthOption, String fifthOption) {
		this.firstOption = firstOption;
		this.secondOption = secondOption;
		this.thirdOption = thirdOption;
		this.fourthOption = fourthOption;
		this.fifthOption = fifthOption;
	}

	@Override
	protected final void display(Player player) {
		player.getDialog().sendOption5(firstOption, secondOption, thirdOption, fourthOption, fifthOption);
	}
}
