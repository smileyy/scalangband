package scalangband.model.level.generation.monster

import scalangband.model.level.generation.roomandhallway.DungeonLevelCanvas

import scala.util.Random

trait MonsterGeneration {
  def addMonsters(random: Random, canvas: DungeonLevelCanvas, depth: Int): Unit
}

object RandomMonsterGeneration extends MonsterGeneration {
  override def addMonsters(random: Random, canvas: DungeonLevelCanvas, depth: Int): Unit = {
    val numberOfMonsters = random.nextInt(100) match {
      case x if x < 25 => 0
      case x if x < 75 => 1
      case x if x < 95 => 2
      case x if x < 100 => 3
    }

    canvas.addMonster(depth)
  }
}
