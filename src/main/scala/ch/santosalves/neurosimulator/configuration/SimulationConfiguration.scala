package ch.santosalves.neurosimulator.configuration

import com.thoughtworks.xstream.annotations.XStreamAliasType

@XStreamAliasType("Simulation")
class SimulationConfiguration(nn:NeuronalNetworkConfiguration, ep: ExecutionPlanConfiguration) {
  val neuronalNetwork = nn  
  val executionPlan = ep
}