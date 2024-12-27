package scalangband.model.monster

import scalangband.model.item.*
import scalangband.model.util.Weighted

import scala.util.Random

trait MonsterInventoryGenerator {
  def generateItem(random: Random, armory: Armory): Option[Item]
}

class ProbabilisticInventoryGenerator(probability: Int, generator: MonsterInventoryGenerator)
    extends MonsterInventoryGenerator {
  override def generateItem(random: Random, armory: Armory): Option[Item] = {
    if (random.nextInt(100) < probability) generator.generateItem(random, armory) else None
  }
}

class WeightedInventoryGenerator(generators: Seq[Weighted[MonsterInventoryGenerator]])
    extends MonsterInventoryGenerator {
  override def generateItem(random: Random, armory: Armory): Option[Item] = {
    Weighted.selectFrom(random, generators).generateItem(random, armory)
  }
}

class FixedInventoryGenerator(factory: ItemFactory, random: Random = new Random(), quality: ItemQuality = NormalQuality)
    extends MonsterInventoryGenerator {
  override def generateItem(random: Random, armory: Armory): Option[Item] = {
    Some(factory(random, quality))
  }
}

class ArmoryInventoryGenerator(depth: Int) extends MonsterInventoryGenerator {
  override def generateItem(random: Random, armory: Armory): Option[Item] = {
    Some(armory.generateItem(random, depth))
  }
}

class ArchetypeArmoryInventoryGenerator(archetype: ItemArchetype, depth: Int) extends MonsterInventoryGenerator {
  override def generateItem(random: Random, armory: Armory): Option[Item] = {
    Some(armory.generateItem(random, archetype, depth))
  }
}
