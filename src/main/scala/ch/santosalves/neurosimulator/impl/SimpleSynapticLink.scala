package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.SynapticLink
import ch.santosalves.neurosimulator.api.NerveCell

class SimpleSynapticLink (n: String, n1: NerveCell, n2: NerveCell, weight:Double) extends SynapticLink {  
  name = n
  source = n1
  target = n2
  n1-->(n2, weight)
}