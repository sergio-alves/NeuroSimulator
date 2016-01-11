package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.NerveCell
import java.util.UUID


/**
 * A factory to create different instances of nerve cells
 *
 * @author Sergio Alves
 */
object NerveCellFactory {

  /**
   * Creates a new instance of nerve cell
   *
   * @param wished The wished type of nerve cell to build
   * @param name The Cell name
   * @param threshold The cell threshold
   */
  def createNerveCell(wished: NerveCellTypes.NerveCellType, name: String, threshold: Double): NerveCell = {
    createNerveCell(wished, UUID.randomUUID().toString(), name, threshold, "")
  }

  def createNerveCell(wished: NerveCellTypes.NerveCellType, uuid: String, name: String, threshold: Double, description:String): NerveCell = {
    wished match {
      case NerveCellTypes.McCullochAndPitts => new McCullochPittsNeuron(UUID.fromString(uuid) ,name, threshold, description)
    }
  }
}