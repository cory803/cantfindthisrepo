package org.scripts.kotlin.core.engine

import com.chaos.world.content.EvilTrees
import com.chaos.world.content.ShootingStar
import com.chaos.world.content.minigames.impl.FightPit
import com.chaos.world.content.minigames.impl.PestControl

/**
 * Created by Dave on 05/07/2016.
 */

object SequenceControl {

    /**
     * Here we start tasks that are sequenced.
     */

    @JvmStatic fun sequenceStarter() {
        FightPit.sequence()
        PestControl.sequence()
        ShootingStar.sequence()
        EvilTrees.sequence()
        System.out.println("Chaos has successfully started up World sequences.")
    }
}