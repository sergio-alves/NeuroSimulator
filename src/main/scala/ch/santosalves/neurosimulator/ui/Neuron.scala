package ch.santosalves.neurosimulator.ui

import ch.santosalves.neurosimulator.api.NerveCell
import ch.santosalves.neurosimulator.impl.NerveCellTypes.NerveCellType
import ch.santosalves.neurosimulator.impl.NerveCellFactory
import javax.swing.JComponent
import java.awt.Graphics
import java.awt.Font
import java.awt.Color
import java.awt.Graphics2D
import java.awt.BasicStroke
import java.awt.dnd.DragGestureListener
import java.awt.dnd.DragSourceListener
import java.awt.dnd.DropTarget
import java.awt.dnd.DragGestureEvent
import java.awt.dnd.DragSourceDragEvent
import java.awt.dnd.DragSourceEvent
import java.awt.dnd.DragSourceDropEvent
import java.awt.Point
import javax.swing.SwingUtilities
import java.util.UUID
import org.apache.logging.log4j.LogManager
import java.awt.dnd.DragSource
import java.awt.dnd.DnDConstants
import ch.santosalves.neurons.ui.ImageSelection
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.Observer
import java.util.Observable


/**
 * A graphic neuron representation
 */
class Neuron (nct: NerveCellType,uuid:String, _name:String, _threshold: Double, description:String ) extends JComponent with DragGestureListener with DragSourceListener{
  def this(_nct: NerveCellType, _name:String, _threshold: Double) = {
    this(_nct, UUID.randomUUID().toString(), _name, _threshold, "A graphical instance of McCulloch and Pitts Neuron") 
  }

  val logger = LogManager.getLogger(classOf[Neuron])
  
  val neuron: NerveCell = NerveCellFactory.createNerveCell(nct, uuid, _name, _threshold, description)
  
  object Dimensions {
    val Width = 32
    val Height = 32
  }
  
  var name = _name
  var threshold = _threshold
  
  var componentBorderColor = Color.black
  var fontSizeFixed = false
  var fontSize = 0
  
  /**
   * setBounds override
   */
  override def setBounds(x:Int, y:Int, width:Int, height:Int) = {
    super.setBounds(x, y, width, height)
    fontSizeFixed = false
  }
  
  addMouseListener(new MouseAdapter {
    override def mouseExited(arg0: MouseEvent) = {
      componentBorderColor = Color.black
      repaint()
    }
    override def mouseEntered(arg0: MouseEvent) = {
      componentBorderColor = Color.yellow
      repaint()
    }
  })
  
  /**
   * calculates the better font size for the current neuron depending on component
   * size. Only do it when component updates its bounds
   */
  private def autoFontSize(g: Graphics, fs: Int): Int = {
    g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fs))
    
    if (g.getFontMetrics().stringWidth(name) < (Dimensions.Width - 8) &&
      g.getFontMetrics().getHeight < (Dimensions.Height - 8)) {
      return autoFontSize(g, fs + 1)
    } else {
      fontSizeFixed = true
      return fs - 1;
    }
  }

  /**
   * overrides the paint method of JComponent
   */
  override def paint(g:Graphics) = {    
    super.paint(g)
    if (!fontSizeFixed) {
      fontSize = autoFontSize(g, 0)
    }
    
    //draws and fills the circle
    g.asInstanceOf[Graphics2D].setStroke(new BasicStroke(2));
    g.setColor(Color.red);
    g.fillOval(0, 0, Dimensions.Width - 4, Dimensions.Height - 4);
    
    g.setColor(componentBorderColor);
    g.drawOval(0, 0, Dimensions.Width - 4, Dimensions.Height - 4);
        
    //draws the neuron name inside
    val rec = g.getFont.getStringBounds(name, g.getFontMetrics.getFontRenderContext)
    val w = (Dimensions.Width / 2 - rec.getCenterX)
    var h = (Dimensions.Height / 2 - rec.getCenterY)

    g.drawChars(name.toCharArray(), 0, name.size, w.intValue(), h.intValue());
  }
  
  
  
val dragSource = DragSource.getDefaultDragSource
  
  /*
   * Init stuff
   */
  dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this)  
  setDropTarget(new DropTarget(this.getParent, this.getParent.asInstanceOf[PlaygroundPanel]))

  
  /**
   * On gesture recognition
   */
  override def dragGestureRecognized(arg0: DragGestureEvent) = {
    logger.trace("0[" + name + "] Drag enter")
    //Create an image of the current Neuron for moving
    val dragImage = this.createImage(getWidth, getHeight)
    paint(dragImage.getGraphics)   
    
    //Start draging
    dragSource.startDrag (arg0, DragSource.DefaultMoveDrop, dragImage, arg0.getDragOrigin, new ImageSelection(this), this)
  }

  override def dragEnter(x$1: DragSourceDragEvent) =  logger.trace("1[" + name + "] Drag enter")
  override def dragOver(arg0: DragSourceDragEvent)= logger.trace("[" + name + "] Drag over")
  override def dragExit(arg0: DragSourceEvent) = logger.trace("[" + name + "] Drag exit")
  override def dropActionChanged(arg0: DragSourceDragEvent) = logger.trace("[" + name + "] Drag action changed")

  /**
   * Drop end => update component position in panel
   */
  override def dragDropEnd(arg0: DragSourceDropEvent) = {
    logger.trace("[" + name + "] Drag action end")
    
    //Move the component to the new place
    var p = arg0.getLocation 
    var p2 = arg0.getDragSourceContext.getTrigger.getDragOrigin
        
    logger.debug("Drop location point (x,y)->" + p.toString() )
    logger.debug("Origin point (x,y)->" + p2.toString())
    
    SwingUtilities.convertPointFromScreen(p, this.getParent)
    p = new Point(p.x-p2.x, p.y-p2.y)    
    setLocation(p)
  }  
  
}