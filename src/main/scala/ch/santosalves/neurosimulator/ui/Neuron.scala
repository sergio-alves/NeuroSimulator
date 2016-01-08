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

/**
 * A graphic neuron representation
 */
class Neuron (nct: NerveCellType, name:String, threshold: Double ) extends JComponent{
  val neuron: NerveCell = NerveCellFactory.createNerveCell(nct,name,threshold)
  
  object Dimensions {
    val Width = 32
    val Height = 32
  }
  
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
}