package ch.santosalves.neurons.ui

import java.awt.datatransfer.Transferable
import java.awt.datatransfer.DataFlavor
import javax.swing.JComponent

class ImageSelection (component: JComponent) extends Transferable() {
  // Method descriptor #13 ()[Ljava/awt/datatransfer/DataFlavor;
  override def getTransferDataFlavors : Array[DataFlavor] = Array(DataFlavor.imageFlavor)
  // Method descriptor #14 (Ljava/awt/datatransfer/DataFlavor;)Z
  override def isDataFlavorSupported(arg0: DataFlavor): Boolean = arg0 == DataFlavor.imageFlavor        
  // Method descriptor #15 (Ljava/awt/datatransfer/DataFlavor;)Ljava/lang/Object;
  override def getTransferData(arg0:DataFlavor) : Object = component;  
}