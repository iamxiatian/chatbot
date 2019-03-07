package xiatian.chatbot.db

import java.io.File
import java.nio.charset.StandardCharsets

import com.google.common.collect.Lists
import org.rocksdb.{ColumnFamilyDescriptor, ColumnFamilyHandle, RocksDB}

/**
  * 寻医问药库
  */
object MedicineDb extends BaseRocksDb("medicine db") {

  import StandardCharsets.UTF_8

  val dbPath = new File("./db/medicine")
  if (!dbPath.getParentFile.exists())
    dbPath.getParentFile.mkdirs()

  override val cfNames = Lists.newArrayList[ColumnFamilyDescriptor](
    new ColumnFamilyDescriptor("default".getBytes(UTF_8)),
    new ColumnFamilyDescriptor("symptom".getBytes(UTF_8)),
    new ColumnFamilyDescriptor("disease".getBytes(UTF_8))
  )

  override val db = RocksDB.open(options, dbPath.getAbsolutePath, cfNames, cfHandlers)

  //药品列族
  private val medicineHandler: ColumnFamilyHandle = cfHandlers.get(0)
  //症状列族
  private val symptomHandler: ColumnFamilyHandle = cfHandlers.get(1)
  //疾病列族
  private val diseaseHandler: ColumnFamilyHandle = cfHandlers.get(2)

  /**
    * 构建医药数据库
    */
  def build(): Unit = {

  }

  def main(args: Array[String]): Unit = {
    build()
  }
}
