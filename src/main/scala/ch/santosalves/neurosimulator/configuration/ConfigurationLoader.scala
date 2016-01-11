package ch.santosalves.neurosimulator.configuration

import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlID
import javax.xml.bind.annotation.XmlIDREF
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import javax.xml.bind.annotation.XmlList
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.thoughtworks.xstream.annotations.XStreamImplicit
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.File
import com.thoughtworks.xstream.annotations.XStreamAliasType
import java.util.UUID

object ConfigurationLoader {
  def toXmlFile(fname: String, c: SimulationConfiguration): Unit = {
    val v = new XStream(new DomDriver())
    v.toXML(c, new FileOutputStream(fname))
  }

  def loadFromXmlFile(fname: String): SimulationConfiguration = {
    val xstream = new XStream(new DomDriver())
    xstream.autodetectAnnotations(true)
    return xstream.fromXML(new File(fname)).asInstanceOf[SimulationConfiguration]
  }

  def printDefaultConfiguration(): Unit = {
    val xstream = new XStream(new DomDriver("UTF-8")) //XStreamConversions()
    var ln = Array(
      new NeuronConfiguration("N1", 1.0, new LocationConfiguration(100, 100)),
      new NeuronConfiguration("N2", 1.0, new LocationConfiguration(100, 300)),
      new NeuronConfiguration("N3", 1.0, new LocationConfiguration(200, 200)))
    xstream.setMode(XStream.XPATH_ABSOLUTE_REFERENCES)
    var ll = Array(new LinkConfiguration("L1", ln(0).uuid, ln(2).uuid, 2.0), new LinkConfiguration("L2", ln(1).uuid, ln(2).uuid, 2.0))
    var nn = new NeuronalNetworkConfiguration(ln, ll)

    var steps = Array(
      new StepConfiguration(0, Array(new ExcitationConfiguration(ln(0).uuid.toString(), 1.1), new ExcitationConfiguration(ln(1).uuid.toString(), 2.0)), Array(new ResultConfiguration(ln(2).uuid.toString(), 1.0))))

    var ep = new ExecutionPlanConfiguration(steps)
    var s = new SimulationConfiguration(nn, ep)

    val fw = new FileWriter(new File("configuration.xml"))
    xstream.toXML(s, fw)

    xstream.autodetectAnnotations(true)
    println("xlm : \r\n" + xstream.toXML(s))
  }
}


















