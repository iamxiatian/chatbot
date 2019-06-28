package xiatian.chatbot.store.rocksdb

import java.nio.charset.StandardCharsets.UTF_8

import com.google.common.collect.Lists
import org.rocksdb.{ColumnFamilyHandle, DBOptions, RocksDB}

abstract class BaseRocksDb(dbName: String) extends Db {
  RocksDB.loadLibrary()

  protected val options = new DBOptions().setCreateIfMissing(true)
    .setMaxBackgroundCompactions(10)
    .setCreateMissingColumnFamilies(true)

  //val cfNames: java.util.ArrayList[ColumnFamilyDescriptor] = ???

  //存放cfNames列族处理的Handle，在打开RocksDB时，会填充该值
  protected val cfHandlers = Lists.newArrayList[ColumnFamilyHandle]

  override def open() = {
    val num = db.getLongProperty("rocksdb.estimate-num-keys")
    println(s"$dbName contains $num keys")
  }

  def db: RocksDB = ???

  def put(handler: ColumnFamilyHandle, key: String, value: String) = {
    db.put(handler, key.getBytes(UTF_8), value.getBytes(UTF_8))
  }

  def getString(handler: ColumnFamilyHandle, key: String): Option[String] = {
    val value = db.get(handler, key.getBytes(UTF_8))
    if (value == null) None
    else Option(new String(value, UTF_8))
  }

  override def close(): Unit = {
    print(s"==> close $dbName ... ")
    cfHandlers.forEach(h => h.close())
    db.close()
    println("DONE.")
  }
}
