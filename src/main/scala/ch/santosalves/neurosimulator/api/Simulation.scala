package ch.santosalves.neurosimulator.api

/**
 *
 * @author Sergio Alves
 */
trait Simulation {
  var neuronalNetwork: NeuronalNetwork = _
  var executionPlan: ExecutionPlan = _
}