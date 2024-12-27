package scalangband.data.monster.feline

import scalangband.bridge.rendering.TextColors.LightUmber
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.ClawAttack
import scalangband.model.monster.{Feline, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object WildCat extends MonsterFactory {
  override def spec: MonsterSpec = MonsterSpec(
    name = "Wild Cat",
    archetype = Feline,
    depth = 2,
    health = DiceRoll("3d5"),
    speed = 40,
    armorClass = 14,
    experience = 8,
    sleepiness = 0,
    bashesDoors = true,
    actions = actions,
    color = LightUmber
  )

  def actions = MonsterActions(
    adjacent = Seq(
      Weighted(100, new MeleeAttacksAction(Seq(new ClawAttack(DiceRoll("1d3")), new ClawAttack(DiceRoll("1d3")))))
    ),
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )
}