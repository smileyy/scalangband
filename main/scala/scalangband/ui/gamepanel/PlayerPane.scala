package scalangband.ui.gamepanel

import scalangband.bridge.rendering.TextColors.{Green, Turquoise, White}
import scalangband.model.Game
import scalangband.model.player.{Player, Stat, Stats}

import scala.swing.{Color, Font, Graphics2D}

class PlayerPane(game: Game, font: Font) {
  val player: Player = game.player

  def paint(g: Graphics2D, x: Int, y: Int): Unit = {
    val lineHeight = g.getFontMetrics(font).getHeight
    val charWidth = g.getFontMetrics(font).charWidth(' ')

    g.setColor(White)
    g.drawString(playerNameString, x, y + lineHeight)

    g.setColor(Turquoise)
    g.drawString(player.race.name, x, y + lineHeight * 2)
    g.drawString(player.cls.name, x, y + lineHeight * 3)

    new LevelField().paint(player, g, font, 0, 4, PlayerPane.WidthInChars)
    new XpField().paint(player, g, font, 0, 5, PlayerPane.WidthInChars)
    new MoneyField().paint(player, g, font, 0, 6, PlayerPane.WidthInChars)

    new StatField("STR", stats => stats.str).paint(player, g, font, 0, 8, PlayerPane.WidthInChars)
    new StatField("INT", stats => stats.intg).paint(player, g, font, 0, 9, PlayerPane.WidthInChars)
    new StatField("WIS", stats => stats.wis).paint(player, g, font, 0, 10, PlayerPane.WidthInChars)
    new StatField("DEX", stats => stats.dex).paint(player, g, font, 0, 11, PlayerPane.WidthInChars)
    new StatField("CON", stats => stats.con).paint(player, g, font, 0, 12, PlayerPane.WidthInChars)

    new ArmorClassField().paint(player, g, font, 0, 14, PlayerPane.WidthInChars)
    new HealthField().paint(player, g, font, 0, 15, PlayerPane.WidthInChars)

    g.setColor(White)
    g.drawString(levelDepthString, 0, lineHeight * 20)
  }

  private def levelDepthString = {
    if (game.level.depth == 0) {
      "Town"
    } else {
      val depth = game.level.depth
      val firstPart = s"${depth * 50}'"
      val secondPart = if (depth < 10) s"(L $depth)" else s"(L$depth)"
      s"$firstPart   $secondPart"
    }
  }

  private def playerNameString = {
    if (player.name.length < PlayerPane.WidthInChars) {
      player.name
    } else {
      player.name.substring(0, PlayerPane.WidthInChars - 1)
    }
  }

}
object PlayerPane {
  def WidthInChars: Int = 12
}

trait LabeledField {
  def paint(player: Player, g: Graphics2D, font: Font, x: Int, y: Int, width: Int): Unit = {
    val charWidth = g.getFontMetrics(font).charWidth(' ')
    val charHeight = g.getFontMetrics(font).getHeight

    val value = getValue(player)
    val valueOffset = width - value.length

    g.setColor(labelColor)
    g.drawString(label, x * charWidth, (y + 1) * charHeight)
    g.setColor(valueColor)
    g.drawString(value, (x + valueOffset) * charWidth, (y + 1) * charHeight)
  }

  def label: String
  def getValue(player: Player): String
  def labelColor: Color = White
  def valueColor: Color = Green
}

class LevelField extends LabeledField {
  override def label: String = "Level"
  override def getValue(player: Player): String = player.level.toString
}

class XpField extends LabeledField {
  override def label: String = "EXP"
  override def getValue(player: Player): String = player.experience.current.toString
}

class MoneyField extends LabeledField {
  override def label: String = "AU"
  override def getValue(player: Player): String = player.money.toString
}

class StatField(statName: String, getStat: Stats => Stat[?]) extends LabeledField {
  override def label: String = s"$statName:"
  override def getValue(player: Player): String = getStat(player.stats).toString
}

class ArmorClassField extends LabeledField {
  override def label: String = "AC"
  override def getValue(player: Player): String = player.armorClass.toString
}

class HealthField extends LabeledField {
  override def label: String = "HP"
  override def getValue(player: Player): String = s"${player.health}/ ${player.maxHealth}"
}