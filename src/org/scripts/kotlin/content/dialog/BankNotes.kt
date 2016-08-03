package org.scripts.kotlin.content.dialog

import com.runelive.model.input.impl.ChangePassword
import com.runelive.model.input.impl.SetEmail
import com.runelive.model.options.fiveoption.FiveOption
import com.runelive.model.options.twooption.TwoOption
import com.runelive.model.player.GameMode
import com.runelive.model.player.dialog.Dialog
import com.runelive.model.player.dialog.DialogMessage
import com.runelive.world.content.BankPin
import com.runelive.world.entity.impl.player.Player
/*
 *
 * @author Jonny
 */
class BankNotes(player: Player) : Dialog(player) {

    internal var dialog: Dialog = this

    init {
        setEndState(14)
    }

    override fun getMessage(): DialogMessage? {
        when (state) {
            0 -> return createNpc("Hello there, " + player.username + ", I can un note or note your items for you, just use them on me or any bank booth.")
        }
        return null
    }
}
