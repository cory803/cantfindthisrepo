package org.scripts.kotlin.content.dialog

import com.chaos.model.input.impl.ChangePassword
import com.chaos.model.input.impl.SetEmail
import com.chaos.model.options.fiveoption.FiveOption
import com.chaos.model.options.twooption.TwoOption
import com.chaos.model.player.GameMode
import com.chaos.model.player.dialog.Dialog
import com.chaos.model.player.dialog.DialogHandler
import com.chaos.model.player.dialog.DialogMessage
import com.chaos.world.content.BankPin
import com.chaos.world.entity.impl.player.Player
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
            0 -> return createNpc(DialogHandler.CALM, "Hello there, " + player.username + ", I can un note or note your items for you, just use them on me or any bank booth.")
        }
        return null
    }
}
