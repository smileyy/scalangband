package scalangband.ui.keys

import scalangband.model.Game
import scalangband.model.debug.EnableDebugAction
import scalangband.model.location.*
import scalangband.model.player.action.*
import scalangband.ui.gamepanel.overlay.*

import scala.swing.event.{Key, KeyPressed}

object MainKeyHandler extends KeyHandler {
  override def handleKeyPressed(event: KeyPressed, game: Game): Either[Option[PlayerAction], GamePanelOverlay] =
    event match {
      case KeyPressed(_, Key.A, Key.Modifier.Control, _) => if (game.debug) Right(WizardOverlay) else Left(None)
      case KeyPressed(_, Key.W, Key.Modifier.Control, _) => Left(Some(EnableDebugAction))

      case KeyPressed(_, Key.C, Key.Modifier.Shift, _) => Right(new CharacterOverlay(game))
      case KeyPressed(_, Key.E, Key.Modifier.Shift, _) => Right(EatFoodOverlay(game))
      case KeyPressed(_, Key.Period, Key.Modifier.Shift, _) => Left(Some(GoDownStairsAction))
      case KeyPressed(_, Key.Comma, Key.Modifier.Shift, _)  => Left(Some(GoUpStairsAction))

      case KeyPressed(_, Key.Key1, _, _)  => Left(Some(PlayerMovementAction(DownLeftDirection)))
      case KeyPressed(_, Key.Key2, _, _)  => Left(Some(PlayerMovementAction(DownDirection)))
      case KeyPressed(_, Key.Key3, _, _)  => Left(Some(PlayerMovementAction(DownRightDirection)))
      case KeyPressed(_, Key.Key4, _, _)  => Left(Some(PlayerMovementAction(LeftDirection)))
      case KeyPressed(_, Key.Key5, _, _)  => Left(Some(PlayerPassAction))
      case KeyPressed(_, Key.Key6, _, _)  => Left(Some(PlayerMovementAction(RightDirection)))
      case KeyPressed(_, Key.Key7, _, _)  => Left(Some(PlayerMovementAction(UpLeftDirection)))
      case KeyPressed(_, Key.Key8, _, _)  => Left(Some(PlayerMovementAction(UpDirection)))
      case KeyPressed(_, Key.Key9, _, _)  => Left(Some(PlayerMovementAction(UpRightDirection)))
      case KeyPressed(_, Key.Up, _, _)    => Left(Some(PlayerMovementAction(UpDirection)))
      case KeyPressed(_, Key.Down, _, _)  => Left(Some(PlayerMovementAction(DownDirection)))
      case KeyPressed(_, Key.Left, _, _)  => Left(Some(PlayerMovementAction(LeftDirection)))
      case KeyPressed(_, Key.Right, _, _) => Left(Some(PlayerMovementAction(RightDirection)))

      case KeyPressed(_, Key.C, _, _) => Right(CloseOverlay)
      case KeyPressed(_, Key.D, _, _) => Right(DropItemInventoryOverlay(game))
      case KeyPressed(_, Key.E, _, _) => Right(new EquipmentOverlay(game))
      case KeyPressed(_, Key.G, _, _) => Left(Some(PickUpItemAction))
      case KeyPressed(_, Key.I, _, _) => Right(InventoryListOverlay(game))
      case KeyPressed(_, Key.O, _, _) => Right(OpenOverlay)
      case KeyPressed(_, Key.Q, _, _) => Right(QuaffPotionOverlay(game))
      case KeyPressed(_, Key.T, _, _) => Right(new TakeOffEquipmentOverlay(game))
      case KeyPressed(_, Key.W, _, _) => Right(WearEquipmentOverlay(game))

      case KeyPressed(_, _, _, _) => Left(None)
    }
}
