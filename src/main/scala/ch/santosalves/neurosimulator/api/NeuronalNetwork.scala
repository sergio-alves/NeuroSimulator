package ch.santosalves.neurosimulator.api

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamImplicit

/**
 * The base 
 * 
 * @author Sergio Alves
 */
trait NeuronalNetwork {  
  var neurons:Array[NerveCell] = _  
  var synapticLinks:Array[SynapticLink] = _
}