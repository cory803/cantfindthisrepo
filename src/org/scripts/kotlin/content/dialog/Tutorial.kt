package org.scripts.kotlin.content.dialog

import com.runelive.model.options.Option
import com.runelive.model.options.fiveoption.FiveOption
import com.runelive.model.options.fouroption.FourOption
import com.runelive.model.options.threeoption.ThreeOption
import com.runelive.model.options.twooption.TwoOption
import com.runelive.model.player.GameMode
import com.runelive.model.player.dialog.Dialog
import com.runelive.model.player.dialog.DialogMessage
import com.runelive.world.content.BankPin
import com.runelive.world.entity.impl.player.Player

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/18/2016.

 * @author Seba
 */
class Tutorial(player: Player) : Dialog(player) {

    internal var gameMode: GameMode? = null
    internal var dialog: Dialog = this

    init {
        setEndState(27)
        player.npcClickId = 945
    }

    override fun getMessage(): DialogMessage? {
        when (state) {
            0 -> return Dialog.createNpc("Welcome to @red@RuneLive@bla@ adventurer! Can I help you in any way?")
            1 -> return Dialog.createPlayer("Yes, please! How can I get started?")
            2 -> return Dialog.createNpc("Im glad you asked! The first thing we need to do is get you setup on a game mode.")
            3 -> return Dialog.createNpc("Here at RuneLive we have several game modes for you to choose from. We have Sir, Lord, Legend, Extreme, Realism, and Ironman")
            4 -> return Dialog.createPlayer("That is a lot to choose from! Where do I start?")
            5 -> return Dialog.createNpc("That a wonderful question let me help you out.  What mode would you like to learn about?")
            6 -> return Dialog.createOption(object : FiveOption(
                    "Learn about Realism mode",
                    "Learn about Extreme mode",
                    "Learn about Legend mode",
                    "Learn about Lord mode",
                    "Next Page ->") {
                override fun execute(player: Player, option: Option.OptionType) {
                    when (option) {
                        Option.OptionType.OPTION_1_OF_5 -> {
                            state = 8
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_2_OF_5 -> {
                            state = 9
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_3_OF_5 -> {
                            state = 10
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_4_OF_5 -> {
                            state = 11
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_5_OF_5 -> {
                            state = 7
                            player.dialog.sendDialog(dialog)
                        }
                    }
                }
            })
            7 -> return Dialog.createOption(object : FourOption(
                    "Learn about Sir mode",
                    "Learn about Ironman mode",
                    "I have choose a game mode!",
                    "<- Back") {
                override fun execute(player: Player, option: Option.OptionType) {
                    when (option) {
                        Option.OptionType.OPTION_1_OF_4 -> {
                            state = 12
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_2_OF_4 -> {
                            state = 13
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_3_OF_4 -> {
                            state = 14
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_4_OF_4 -> {
                            state = 6
                            player.dialog.sendDialog(dialog)
                        }
                    }
                }
            })
            8 -> {
                state = 5
                return Dialog.createNpc("Realism is the most prestigious game mode, on Realism you will be playing on 5x exp rates, you also get 15x drop rate, 40% prayer drain rates, and recover spec every 5 seconds!")
            }
            9 -> {
                state = 5
                return Dialog.createNpc("Extreme mode is the second hardest mode here at RuneLive. You will be playing on 15x exp rates. You also get 12x drop rate, 50% prayer drain rates, and recover spec every 8 seconds!")
            }
            10 -> {
                state = 5
                return Dialog.createNpc("On Legend mode you will be playing on 35x exp rates. You also get 10x drop rate, 60% prayer drain rates, and recover spec every 10 seconds!")
            }
            11 -> {
                state = 5
                return Dialog.createNpc("On Lord mode you will be playing on 80x exp rates. You also get 8x drop rate, 75% prayer drain rates, and recover spec every 15 seconds!")
            }
            12 -> {
                state = 6
                return Dialog.createNpc("On Sir mode you will be playing on 125x exp rates. You also get 5x drop rate, and recover spec every 20 seconds!")
            }
            13 -> {
                state = 6
                return Dialog.createNpc("On Ironman you will not be able to use shops or trade with other players. You be on 25x exp rates, 12x drops, 55% prayer drain, and recover spec every 8 seconds!")
            }
            14 -> return Dialog.createNpc("So you think you are ready? Okay then what game mode would you like to play on?")
            15 -> return Dialog.createOption(object : FiveOption(
                    "Realism Mode",
                    "Extreme Mode",
                    "Legend Mode",
                    "Lord Mode",
                    "Next Page ->") {
                override fun execute(player: Player, option: Option.OptionType) {
                    when (option) {
                        Option.OptionType.OPTION_1_OF_5 -> {
                            gameMode = GameMode.REALISM
                            state = 17
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_2_OF_5 -> {
                            gameMode = GameMode.EXTREME
                            state = 17
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_3_OF_5 -> {
                            gameMode = GameMode.LEGEND
                            state = 17
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_4_OF_5 -> {
                            gameMode = GameMode.LORD
                            state = 17
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_5_OF_5 -> {
                            state = 16
                            player.dialog.sendDialog(dialog)
                        }
                    }
                }
            })
            16 -> return Dialog.createOption(object : ThreeOption(
                    "Sir Mode",
                    "Ironman mode",
                    "<- Back") {
                override fun execute(player: Player, option: Option.OptionType) {
                    when (option) {
                        Option.OptionType.OPTION_1_OF_3 -> {
                            gameMode = GameMode.SIR
                            state = 17
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_2_OF_3 -> {
                            gameMode = GameMode._IRONMAN
                            state = 17
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_3_OF_3 -> {
                            state = 15
                            player.dialog.sendDialog(dialog)
                        }
                    }
                }
            })
            17 -> return Dialog.createPlayer("I want to choose " + gameMode!!.modeName + "!")
            18 -> return Dialog.createNpc(gameMode!!.modeName + " is the perfect game mode! However I want to want to make sure that is what you want.")
            19 -> return Dialog.createOption(object : TwoOption(
                    "Yes I would like to play on " + gameMode!!.modeName,
                    "No, I want to choose another game mode!") {
                override fun execute(player: Player, option: Option.OptionType) {
                    when (option) {
                        Option.OptionType.OPTION_1_OF_2 -> {
                            state = 20
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_2_OF_2 -> {
                            state = 15
                            player.dialog.sendDialog(dialog)
                        }
                    }
                }
            })
            20 -> {
                player.gameModeAssistant.gameMode = gameMode
                return Dialog.createNpc("Perfect, you are now a " + gameMode!!.modeName + "! Would you like me to show you around RuneLive?")
            }
            21 -> return Dialog.createOption(object : TwoOption("Yes, can you please show me around RuneLive?", "I think that I can manage from here, thanks!") {
                override fun execute(player: Player, option: Option.OptionType) {
                    when (option) {
                        Option.OptionType.OPTION_1_OF_2 -> {
                            state = 24
                            player.dialog.sendDialog(dialog)
                        }
                        Option.OptionType.OPTION_2_OF_2 -> {
                            state = 22
                            player.dialog.sendDialog(dialog)
                        }
                    }
                }
            })
            22 -> return Dialog.createPlayer("I think that I can manage from here, thanks!")
            23 -> {
                player.setContinueSkipTutorial(true)
                state = 26
                return Dialog.createNpc("Alright, sounds good, however we must first set you up with a bank pin to make sure your account is safe from mischievous players.")
            }
            24 -> return Dialog.createPlayer("Yes, can you please show me around RuneLive?")
            25 -> {
                player.setContinueTutorial(true)
                return Dialog.createNpc("Of course I can! Lets make sure you are all setup to proceed on your new journey!")
            }
            26 -> return Dialog.createNpc("First thing is we are going to set you up with a bank pin to make sure your account is safe from mischievous players.")
            27 -> {
                BankPin.init(player, false)
                return null
            }
        }

        return null
    }
}
