package ch.santosalves.neurosimulator.ui

import javax.swing.JPanel
import java.awt.Graphics
import java.awt.dnd.DropTargetListener
import java.awt.dnd.DropTargetDragEvent
import org.apache.logging.log4j.LogManager
import java.awt.dnd.DropTargetEvent
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DropTargetDropEvent
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget

class PlaygroundPanel extends JPanel with DropTargetListener {
  val logger = LogManager.getLogger(classOf[PlaygroundPanel])

  init

  def init = {
    setDropTarget(new DropTarget(this, this))
  }

  override def paint(g: Graphics) = {
    super.paint(g)
  }

  override def dragEnter(arg0: DropTargetDragEvent) = {
    logger.debug("[PlayGroundPanel] Drop enter")
  }

  override def dragOver(arg0: DropTargetDragEvent) = {
    logger.debug("[PlayGroundPanel] Drop over")
  }

  override def dragExit(arg0: DropTargetEvent) = {
    logger.debug("[PlayGroundPanel] Drop exit")
  }

  override def dropActionChanged(arg0: DropTargetDragEvent) = {
    logger.debug("[PlayGroundPanel] Drag action changed")
  }

  override def drop(arg0: DropTargetDropEvent) = {
    //val transferable = arg0.getTransferable
    logger.debug("[PlayGroundPanel] Dropped.")
    
    arg0.acceptDrop(DnDConstants.ACTION_MOVE)
    arg0.getDropTargetContext.dropComplete(true)

    /*
    if(arg0.getTransferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
        arg0.acceptDrop(DnDConstants.ACTION_MOVE)
        arg0.getDropTargetContext.dropComplete(true)
        
        arg0.getSource.isInstanceOf[Neuron] match {
          case true => arg0.getSource.asInstanceOf[Neuron].setLocation(arg0.getLocation)
          case false => 
        }
    } */
  }
}