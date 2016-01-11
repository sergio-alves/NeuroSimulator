package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.NerveCell
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import java.util.UUID

/**
 * A implementation of NerveCell as a McCulloch & Pitts Neuron
 *
 * @author Sergio Alves
 */
@XStreamAlias("McCullochPittsNeuron")
class McCullochPittsNeuron(neuronUuid: UUID, neuronName: String, neuronThreshold: Double, desc: String) extends NerveCell {
  def this(neuronUuid: UUID, neuronName: String, neuronThreshold: Double) {
    this(neuronUuid, neuronName, neuronThreshold, "")
  }

  def this(nn: String, nt: Double) = {
    this(UUID.randomUUID(), nn, nt)
  }

  description = desc
  uuid = neuronUuid
  name = neuronName
  threshold = neuronThreshold

  /**
   * Defines the NerveCell trigger function => the output value
   */
  def triggerFunction(): Double = if (sum >= threshold) 1.0 else 0.0

  override def toString(): String = "[" + name + "]-[" + threshold + "]-[" + sum + "]"
}