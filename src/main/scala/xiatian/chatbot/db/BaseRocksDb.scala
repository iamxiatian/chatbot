package xiatian.chatbot.db

import com.google.common.collect.Lists
import org.rocksdb.{ColumnFamilyDescriptor, ColumnFamilyHandle, DBOptions, RocksDB}

abstract class BaseRocksDb(dbName: String) extends Db {
  RocksDB.loadLibrary()

  protected val options = new DBOptions().setCreateIfMissing(true)
    .setMaxBackgroundCompactions(10)
    .setCreateMissingColumnFamilies(true)

  val cfNames: java.util.ArrayList[ColumnFamilyDescriptor] = ???

  //存放cfNames列族处理的Handle，在打开RocksDB时，会填充该值
  protected val cfHandlers = Lists.newArrayList[ColumnFamilyHandle]

  val db: RocksDB = ???


  override def open() = {
    val num = db.getLongProperty("rocksdb.estimate-num-keys")
    println(s"$dbName contains $num keys")
  }

  override def close(): Unit = {
    print(s"==> close $dbName ... ")
    cfHandlers.forEach(h => h.close())
    db.close()
    println("DONE.")
  }
}
