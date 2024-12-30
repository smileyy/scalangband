package scalangband.data.monster.snake

import scalangband.bridge.rendering.TextColors.White
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.{BiteAttack, CrushAttack}
import scalangband.model.monster.{MonsterFactory, MonsterSpec, Snake}
import scalangband.model.util.{DiceRoll, Weighted}

object LargeWhiteSnake extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Large White Snake",
    archetype = Snake,
    depth = 1,
    health = DiceRoll("3d6"),
    speed = 10,
    armorClass = 36,
    experience = 2,
    sleepiness = 99,
    actions = actions,
    color = White
  )
  
  private def actions = MonsterActions(
    adjacent = Seq(
      Weighted(50, RandomMovementAction), 
      Weighted(50, MeleeAttacksAction(Seq(new BiteAttack(DiceRoll("1d2")), new CrushAttack(DiceRoll("1d2")))))
    ),
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )
}
