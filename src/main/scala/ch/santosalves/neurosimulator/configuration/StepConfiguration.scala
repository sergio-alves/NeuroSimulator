package ch.santosalves.neurosimulator.configuration

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute

@XStreamAlias("Step")
class StepConfiguration (t: Int, i: Array[ExcitationConfiguration], o: Array[ResultConfiguration]) {
  @XStreamAsAttribute
  val time = t  
  @XStreamAlias("ExcitationValues")
  val inputs = i    
  @XStreamAlias("ExpectedResults")
  val outputs = o
}