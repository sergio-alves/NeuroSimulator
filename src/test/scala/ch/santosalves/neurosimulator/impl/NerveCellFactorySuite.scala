package ch.santosalves.neurosimulator.impl

import org.scalatest.Assertions
import org.junit.Test
import ch.santosalves.neurosimulator.api.NerveCell
import ch.santosalves.neurosimulator.impl.NerveCellTypes.NerveCellType
import ch.santosalves.neurosimulator.impl.NerveCellTypes.McCullochAndPitts

class NerveCellFactorySuite extends Assertions {
    
  @Test def testFactoryProducesMcCullochAndPittsNeuron() {
    val neuron = NerveCellFactory.createNerveCell(NerveCellTypes.McCullochAndPitts, "N1", 3.1)
    
    assert(neuron.name === "N1")
    assert(neuron.threshold === 3.1)
    assert(neuron.isInstanceOf[McCullochPittsNeuron] === true)    
  }  
}