package ch.santosalves.neurosimulator.configuration

import com.thoughtworks.xstream.annotations.XStreamAlias

@XStreamAlias("Excitation")
class ExcitationConfiguration (nUUID:String, w: Double) {  
  @XStreamAlias("InputNeuron")
  val neuronUUID = nUUID
  val weight = w  
}