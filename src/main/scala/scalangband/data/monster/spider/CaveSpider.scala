package scalangband.data.monster.spider

import scalangband.bridge.rendering.TextColors.Purple
import scalangband.model.monster.action.{MeleeAttacksAction, MonsterActions, RandomMovementAction}
import scalangband.model.monster.attack.BiteAttack
import scalangband.model.monster.{MonsterFactory, MonsterFactoryFriendSpec, MonsterSpec, Spider}
import scalangband.model.util.{DiceRoll, Weighted}

object CaveSpider extends MonsterFactory {
  override val spec: MonsterSpec = MonsterSpec(
    name = "Cave Spider",
    archetype = Spider,
    depth = 2,
    speed = 30,
    health = DiceRoll("2d6"),
    hearing = 8,
    armorClass = 19,
    sleepiness = 80,
    experience = 7,
    actions = actions,
    friends = Seq(MonsterFactoryFriendSpec(100, DiceRoll("2d8"), this)),
    color = Purple
  )

  private def actions = MonsterActions(
    adjacent = Seq(Weighted(100, MeleeAttacksAction(new BiteAttack(DiceRoll("1d4"))))),
    otherwise = Seq(Weighted(100, RandomMovementAction))
  )
}
