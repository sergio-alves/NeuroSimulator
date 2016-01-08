package ch.santosalves.neurons.impl

import ch.santosalves.neurosimulator.api.NerveCell

/**
 * A implementation of NerveCell as a McCulloch & Pitts Neuron
 * 
 * @author Sergio Alves
 */
class McCullochPittsNeuron(neuronName: String, neuronThreshold: Double) extends NerveCell {
  name = neuronName
  threshold = neuronThreshold
  
  /**
   * Defines the NerveCell trigger function => the output value
   */
  def triggerFunction(): Double = {
    logger.info("["+name+"] in triggerFunction with sum("+sum+") >= threshold("+threshold+")=" + (if(sum >= threshold) 1.0 else 0.0))
    if(sum >= threshold) 1.0 else 0.0
  }
  
  override def toString(): String = "["+name+"]-["+threshold+"]-["+sum+"]"
}