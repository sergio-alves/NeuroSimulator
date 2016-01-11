package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.NerveCell
import ch.santosalves.neurosimulator.api.ExpectedSimulationOuput

class ExpectedNeuronOutputImpl(n:NerveCell, o: Double) extends ExpectedSimulationOuput{
  neuron = n
  output = o
}