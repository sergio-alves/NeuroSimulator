package ch.santosalves.neurosimulator.impl

import ch.santosalves.neurosimulator.api.NeuronalNetwork
import ch.santosalves.neurosimulator.api.SynapticLink
import ch.santosalves.neurosimulator.api.NerveCell
import com.thoughtworks.xstream.annotations.XStreamAlias

/**
 * Simple Neuronal Network implementation
 * 
 * @author Sergio Alves
 */
class NeuronalNetworkImpl (_neurons: Array[NerveCell], _links: Array[SynapticLink]) extends NeuronalNetwork {
  neurons = _neurons
  synapticLinks = _links
}