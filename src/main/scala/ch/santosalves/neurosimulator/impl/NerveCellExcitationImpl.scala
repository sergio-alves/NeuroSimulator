package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.NerveCell
import ch.santosalves.neurosimulator.api.NerveCellExcitation

/**
 *
 * @author Sergio Alves 
 */
class NerveCellExcitationImpl (n:NerveCell, w: Double) extends NerveCellExcitation{
  neuron = n
  weight = w
}