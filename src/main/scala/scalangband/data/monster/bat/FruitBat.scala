package scalangband.data.monster.bat

import scalangband.bridge.rendering.TextColors.Orange
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.BiteAttack
import scalangband.model.monster.{Bat, MonsterFactory, MonsterSpec}
import scalangband.model.util.{DiceRoll, Weighted}

object FruitBat extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Fruit Bat",
    archetype = Bat,
    depth = 1,
    health = DiceRoll("2d5"),
    speed = 30,
    armorClass = 3,
    experience = 1,
    sleepiness = 10,
    actions = actions,
    color = Orange
  )
  
  private def actions = new MonsterActions(
    adjacent = Seq(
      Weighted(75, MeleeAttacksAction(new BiteAttack(DiceRoll("1d1")))),
      Weighted(25, RandomMovementAction)
    ), 
    otherwise = Seq(Weighted(100, RandomMovementAction)))
}
