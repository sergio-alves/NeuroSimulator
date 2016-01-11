package ch.santosalves.neurosimulator.configuration

import com.thoughtworks.xstream.annotations.XStreamAlias

@XStreamAlias("Network")
class NeuronalNetworkConfiguration(n: Array[NeuronConfiguration], l: Array[LinkConfiguration]) {
  @XStreamAlias("Neurons")
  val neurons:Array[NeuronConfiguration] = n
  @XStreamAlias("Links")
  val links:Array[LinkConfiguration] = l
}