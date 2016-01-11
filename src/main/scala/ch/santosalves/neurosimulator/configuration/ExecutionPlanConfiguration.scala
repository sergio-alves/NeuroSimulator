package ch.santosalves.neurosimulator.configuration

import com.thoughtworks.xstream.annotations.XStreamAlias

@XStreamAlias("ExecutionPlan")
class ExecutionPlanConfiguration (s:Array[StepConfiguration]) {
  @XStreamAlias("Steps")
  var steps: Array[StepConfiguration] = s
}