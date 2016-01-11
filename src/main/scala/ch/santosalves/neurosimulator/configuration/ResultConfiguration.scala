package ch.santosalves.neurosimulator.configuration

import com.thoughtworks.xstream.annotations.XStreamAlias

@XStreamAlias("Result")
class ResultConfiguration(nUUID:String, o: Double) {
  @XStreamAlias("OutputNeuron")
  val neuronUUID = nUUID
  val expectedOutput = o 
}