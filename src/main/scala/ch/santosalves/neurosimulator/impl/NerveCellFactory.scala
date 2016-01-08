package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.NerveCell
import ch.santosalves.neurons.impl.McCullochPittsNeuron

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
    wished match {
      case NerveCellTypes.McCullochAndPitts => new McCullochPittsNeuron(name, threshold)
    }
  }
}