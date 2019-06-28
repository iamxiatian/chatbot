package xiatian.chatbot.store.rocksdb

import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

import com.google.common.collect.Lists
import org.rocksdb.{ColumnFamilyDescriptor, ColumnFamilyHandle, RocksDB}

import scala.xml.{Node, XML}

/**
  * 寻医问药库
  */
object MedicineDb extends BaseRocksDb("medicine db") {

  override val db = RocksDB.open(options, dbPath.getAbsolutePath, cfNames, cfHandlers)
  if (!dbPath.getParentFile.exists())
    dbPath.getParentFile.mkdirs()
  val dbPath = new File("./db/medicine")
  val cfNames = Lists.newArrayList[ColumnFamilyDescriptor](
    new ColumnFamilyDescriptor("default".getBytes(UTF_8)),
    new ColumnFamilyDescriptor("symptom".getBytes(UTF_8)),
    new ColumnFamilyDescriptor("disease".getBytes(UTF_8))
  )
  //药品列族
  private val medicineHandler: ColumnFamilyHandle = cfHandlers.get(0)
  //症状列族
  private val symptomHandler: ColumnFamilyHandle = cfHandlers.get(1)
  //疾病列族
  private val diseaseHandler: ColumnFamilyHandle = cfHandlers.get(2)

  def test(): Unit = {

  }

  /**
    * 构建医药数据库
    */
  def build(): Unit = {
    buildDrugs()
  }

  def buildDrugs(): Unit = {
    val doc = XML.loadFile(new java.io.File("./data/drugs.xml"))
    val records = (doc \\ "RECORD")
    records.foreach {
      record: Node =>
        val name = (record \ "Catalog").text
        val description = (record \ "Directory_description").text

        put(medicineHandler, name, description)
    }
  }

  def main(args: Array[String]): Unit = {
    //build()
    println(getYao("抗感染药物"))
    close()
  }

  def getYao(yao: String): Option[String] = getString(medicineHandler, yao)
}
