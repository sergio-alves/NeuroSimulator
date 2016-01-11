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
import java.awt.Color

class PlaygroundPanel extends JPanel with DropTargetListener {
  val logger = LogManager.getLogger(classOf[PlaygroundPanel])

  setDropTarget(new DropTarget(this, this))  
  
  override def dragEnter(arg0: DropTargetDragEvent) = logger.debug("[PlayGroundPanel] Drop enter")
  override def dragOver(arg0: DropTargetDragEvent) = logger.debug("[PlayGroundPanel] Drop over")
  override def dragExit(arg0: DropTargetEvent) = logger.debug("[PlayGroundPanel] Drop exit")
  override def dropActionChanged(arg0: DropTargetDragEvent) = logger.debug("[PlayGroundPanel] Drag action changed")
  override def drop(arg0: DropTargetDropEvent) = {
    logger.debug("[PlayGroundPanel] Dropped.")
    arg0.acceptDrop(DnDConstants.ACTION_MOVE)
    arg0.getDropTargetContext.dropComplete(true)
  }
}