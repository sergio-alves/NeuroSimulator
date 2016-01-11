package ch.santosalves.neurosimulator.ui

import javax.swing.JComponent
import java.awt.Graphics
import javax.swing.BorderFactory
import java.awt.Color
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import org.apache.logging.log4j.LogManager
import javax.swing.JPanel
import java.awt.Graphics2D
import java.util.Observable
import ch.santosalves.neurosimulator.api.ObservableObject
import ch.santosalves.neurosimulator.api.OutputTrigged
import ch.santosalves.neurosimulator.api.NerveCell
import java.util.Observer
import java.awt.BasicStroke

/**
 * represents a connection between two neurons.
 */
class NeuronLink(n: String, n1: Neuron, n2: Neuron, weight: Double) extends JPanel with Observer{
  val logger = LogManager.getLogger(classOf[NeuronLink]);

  val name = this.n
  val startNeuron = n1
  val endNeuron = n2
  var minX = 0
  var maxX = 0
  var minY = 0
  var maxY = 0
  // do some initialization
  init

  n1.addComponentListener(new ComponentAdapter() {
    override def componentMoved(arg0: ComponentEvent) = {
      updateBounds
    }
  })

  n2.addComponentListener(new ComponentAdapter() {
    override def componentMoved(arg0: ComponentEvent) = {
      updateBounds
    }
  })

  def init = {
    setBackground(Color.WHITE)
    n1.neuron --> (n2.neuron, weight)
    logger.debug("[" + n + "] (N->N) => (" + n1.name + " -> " + n2.name + ")")
  }

    
  var wireColor= Color.black
  def update(arg0: Observable, arg1:Any) = {
    arg1 match {
      case OutputTrigged(v) => wireColor = new Color(0, (v*255).intValue(), 0)
      case _ => 
    }
  }
  
  class BoundingBox(mx: Int, my: Int, Mx: Int, My: Int, q: Int) {
    def minX = mx
    def minY = my
    def maxX = Mx
    def maxY = My
    def quadrant = q
    def getWidth = maxX - minX
    def getHeight = maxY - minY
    def reMap(dx: Int, dy: Int): BoundingBox = new BoundingBox(minX + dx, minY + dy, maxX + dx, maxY + dy, quadrant)
  }
  
  var bbox: BoundingBox = _

  /**
   * updates bounds.
   */
  def updateBounds = {
    val _b = (n1.getX <= n2.getX) match {
      case true => (n1.getY <= n2.getY) match {
        case true  => new BoundingBox(n1.getX, n1.getY, n2.getX, n2.getY, 1)
        case false => new BoundingBox(n1.getX, n2.getY, n2.getX, n1.getY, 4)
      }
      case false => (n1.getY <= n2.getY) match {
        case true  => new BoundingBox(n2.getX, n1.getY, n1.getX, n2.getY, 2)
        case false => new BoundingBox(n2.getX, n2.getY, n1.getX, n1.getY, 3)
      }
    }
    
    bbox = _b.reMap(16, 16)
    setBounds(bbox.minX, bbox.minY, bbox.getWidth, bbox.getHeight)
    //logger.info("[" + name + "](x1,y1)->(x2,y2) => (" + bbox.minX + "," + bbox.minY + ")->(" + bbox.maxX + ", " + bbox.maxY + ")")
  }

  override def paint(g: Graphics) = {
    //super.paint(g)      
    g.setXORMode(Color.WHITE)
    
    updateBounds
    
   //if (name.equals("L11"))
     //setBorder(BorderFactory.createLineBorder(Color.black))

    //logger.debug("(x1,y1)->(x2,y2) => (" + n1.getX + "," + n1.getY + ")->(" + n2.getX + ", " + n2.getY + ")")
    
    val c = g.getColor
    g.setColor(wireColor)      
    g.asInstanceOf[Graphics2D].setStroke(new BasicStroke(2))
    bbox.quadrant match {
      case 1 => g.drawLine(0, 0, bbox.getWidth, bbox.getHeight)
      case 3 => g.drawLine(0, 0, bbox.getWidth, bbox.getHeight)
      case 2 => g.drawLine(0, bbox.getHeight, bbox.getWidth, 0)
      case 4 => g.drawLine(0, bbox.getHeight, bbox.getWidth, 0)
    }
    
    g.setColor(c)
  }
}