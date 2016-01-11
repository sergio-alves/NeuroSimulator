package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.ExecutionStep
import ch.santosalves.neurosimulator.api.ExecutionStep

class SimulationStepImpl(
    t:Int, 
    i:List[NerveCellExcitationImpl], 
    o: List[ExpectedNeuronOutputImpl]) extends ExecutionStep{
  time = t
  inputs = i
  outputs = o
}