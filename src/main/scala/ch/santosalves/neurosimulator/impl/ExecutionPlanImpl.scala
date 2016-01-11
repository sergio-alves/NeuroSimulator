package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.ExecutionPlan
import com.thoughtworks.xstream.annotations.XStreamAlias

@XStreamAlias("ExecutionPlanImpl")
class ExecutionPlanImpl (l: List[SimulationStepImpl]) extends ExecutionPlan{
  steps = l  
}