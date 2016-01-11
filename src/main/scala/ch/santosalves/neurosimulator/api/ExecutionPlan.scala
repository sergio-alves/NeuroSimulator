package ch.santosalves.neurosimulator.api

trait ExecutionPlan {
  var steps: List[ExecutionStep] = _
}