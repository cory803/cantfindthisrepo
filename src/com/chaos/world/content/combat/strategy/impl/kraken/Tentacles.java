package com.chaos.world.content.combat.strategy.impl.kraken;

import com.chaos.world.content.*;
import com.chaos.world.content.Kraken;
import com.chaos.world.content.combat.CombatContainer;
import com.chaos.world.content.combat.CombatType;
import com.chaos.world.content.combat.magic.CombatSpells;
import com.chaos.world.content.combat.strategy.CombatStrategy;
import com.chaos.world.entity.impl.Character;
import com.chaos.world.entity.impl.npc.NPC;
import com.chaos.world.entity.impl.player.Player;

public class Tentacles implements CombatStrategy {
	
	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		NPC tentacle = (NPC)entity;
		Player player = (Player)victim;
		if(player.getKraken().getKrakenStage() == Kraken.KrakenStage.DEFEATED) {
			return null;
		}
		tentacle.prepareSpell(CombatSpells.TENTACLE_STRIKE.getSpell(), victim);
		return new CombatContainer(entity, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {	
		return false;
	}

	@Override
	public int attackDelay(Character entity) {
		return 3;
	}

	@Override
	public int attackDistance(Character entity) {
		return 30;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}