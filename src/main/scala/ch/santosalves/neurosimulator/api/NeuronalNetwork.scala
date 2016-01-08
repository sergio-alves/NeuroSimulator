package ch.santosalves.neurosimulator.api

import com.thoughtworks.xstream.annotations.XStreamAlias

/**
 * The base 
 * 
 * @author Sergio Alves
 */
@XStreamAlias("Network")
trait NeuronalNetwork {
  @XStreamAlias("Neurons")  
  var neurons:Array[NerveCell] = _
  
  @XStreamAlias("SynapticLinks")
  var synapticLinks:Array[SynapticLink] = _
}