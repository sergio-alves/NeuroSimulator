package ch.santosalves.neurosimulator.ui

import javax.swing.JFrame
import javax.swing.WindowConstants
import java.awt.Dimension
import javax.swing.JComponent
import java.awt.Graphics
import java.awt.LayoutManager
import java.awt.GridBagLayout
import scala.collection.immutable.ListSet
import scala.collection.immutable.ListMap
import org.apache.logging.log4j.LogManager
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.Component
import javax.swing.BorderFactory
import java.awt.Color
import ch.santosalves.neurosimulator.impl.NerveCellTypes.McCullochAndPitts
import ch.santosalves.neurosimulator.configuration.ConfigurationLoader
import scala.collection.mutable.HashMap
import ch.santosalves.neurosimulator.api.NerveCell
import ch.santosalves.neurosimulator.api.SynapticLink
import java.util.UUID
import ch.santosalves.neurosimulator.impl.SynapticLinkImpl
import ch.santosalves.neurosimulator.impl.NeuronalNetworkImpl
import ch.santosalves.neurosimulator.impl.ExecutionPlanImpl
import ch.santosalves.neurosimulator.impl.SimulationImpl
import ch.santosalves.neurosimulator.impl.McCullochPittsNeuron
import ch.santosalves.neurosimulator.impl.SimulationStepImpl
import ch.santosalves.neurosimulator.impl.NerveCellExcitationImpl
import ch.santosalves.neurosimulator.impl.ExpectedNeuronOutputImpl
import javax.swing.JPanel
import javax.swing.JButton
import ch.santosalves.neurosimulator.configuration.SimulationConfiguration
import javax.swing.JOptionPane
import javax.swing.JDialog

class NeuronalNetworkSimulatorFrame() extends JFrame {
  val logger = LogManager.getLogger(classOf[NeuronalNetworkSimulatorFrame])

  init()

  def init() = {
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setPreferredSize(new Dimension(960, 640))

    initComponents

    this.pack()
  }

  /* load a model from file and render it */
  var conf: SimulationConfiguration = _
  var counter: Int = 0;
  var neurons: HashMap[String, NerveCell] = _
  var uiNeurons: HashMap[String, Neuron] = _
  var links: HashMap[String, SynapticLink] = _
  var uiLinks: HashMap[String, NeuronLink] = _
  var jp:JPanel = _
  var p:PlaygroundPanel = _
  var btNext:JButton = _
  
  def initComponents = {
    getContentPane.setLayout(new GridBagLayout());

    p = new PlaygroundPanel()
    p.setLayout(null)
    p.setPreferredSize(new Dimension(600, 200))
    //p.setBorder(BorderFactory.createLineBorder(Color.black))

    var gridBagConstraints = new java.awt.GridBagConstraints()
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST
    gridBagConstraints.gridx = 0
    gridBagConstraints.gridy = 0
    gridBagConstraints.weightx = 1.0
    gridBagConstraints.weighty = 1.0

    getContentPane().add(p, gridBagConstraints)

    jp = new JPanel
    jp.setPreferredSize(new Dimension(600, 100))
    jp.setLayout(null)

    btNext = new JButton("Next")
    btNext.setBounds(10, 10, 100, 25)
    jp.add(btNext)

    btNext.addMouseListener(new MouseAdapter() {
      
      override def mouseClicked(e: MouseEvent) = {
        if (counter < conf.executionPlan.steps.size) {
          conf.executionPlan.steps(counter).inputs.foreach {e => neurons(e.neuronUUID).triggerInput(e.weight)}
          logger.info(s"Executed Step ${counter+1}/${conf.executionPlan.steps.size}")
          counter += 1
          
          p.repaint()          
          
        } else {
          JOptionPane.showMessageDialog(null, "No more steps to execute")
        }
      }
    })

    gridBagConstraints = new java.awt.GridBagConstraints()
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL
    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST
    gridBagConstraints.gridx = 0
    gridBagConstraints.gridy = 1
    gridBagConstraints.weightx = 1
    getContentPane().add(jp, gridBagConstraints)

    conf = ConfigurationLoader.loadFromXmlFile("configuration.xml")
    neurons = new HashMap[String, NerveCell]()
    uiNeurons = new HashMap[String, Neuron]()
    links = new HashMap[String, SynapticLink]()
    uiLinks = new HashMap[String, NeuronLink]()
    //loaded neurons
    conf.neuronalNetwork.neurons.zipWithIndex.foreach {
      case (n, i) =>
        //create a copy from configuration
        val neuron = new McCullochPittsNeuron(UUID.fromString(n.uuid), n.name, n.threshold, n.description)
        neurons.put(n.uuid, neuron)        
        val uiNeuron = new Neuron(neuron)
        uiNeurons.put(n.uuid, uiNeuron)
        uiNeuron.setBounds(n.location.x, n.location.y, uiNeuron.Dimensions.Width, uiNeuron.Dimensions.Height)        
        p.add(uiNeuron)
        
        uiNeuron.addMouseListener(new MouseAdapter() {
          override def mouseClicked(arg0: MouseEvent) = {
            neurons(arg0.getSource.asInstanceOf[Neuron].neuron.uuid.toString()).triggerInput(2.0)
            p.repaint()
          }
        })
    }

    //loading links
    conf.neuronalNetwork.links.zipWithIndex.foreach {
      case (l, i) =>
        links(l.uuid) = new SynapticLinkImpl(UUID.fromString(l.uuid), l.name, neurons(l.inputNeuronId), neurons(l.outputNeuronId), l.weight)
        uiLinks(l.uuid) = new NeuronLink(l.name, uiNeurons(l.inputNeuronId), uiNeurons(l.outputNeuronId), l.weight)
        p.add(uiLinks(l.uuid))
        uiLinks(l.uuid).updateBounds
        uiNeurons(l.inputNeuronId).neuron.addObserver(uiLinks(l.uuid))
    }

  }

  override def paint(g: Graphics) = {
    super.paint(g)
  }
}