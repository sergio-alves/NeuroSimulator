package ch.santosalves.neurosimulator.api

trait ExecutionStep {
  var time:Int = _
  var inputs: List[NerveCellExcitation] = _
  var outputs: List[ExpectedSimulationOuput] = _
}