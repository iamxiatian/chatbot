package xiatian.chatbot.ability.faq

import java.nio.file.Path

import better.files.File
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document._
import org.apache.lucene.index.{IndexOptions, IndexWriter, IndexWriterConfig}
import org.apache.lucene.store.FSDirectory

class FaqIndexer(indexDir: Path) {
  val analyzer = new StandardAnalyzer()

  val config = new IndexWriterConfig(analyzer)
  config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND)

  val directory = FSDirectory.open(indexDir)

  indexDir.toFile.mkdirs()
  val writer = new IndexWriter(directory, config)


  val queryFieldType = new FieldType()
  queryFieldType.setIndexOptions(IndexOptions.DOCS)
  queryFieldType.setStored(false)
  queryFieldType.setStoreTermVectors(false)
  queryFieldType.setTokenized(true)

  def index(faq: Faq): Unit = {
    val doc = new Document()

    val queryField = new Field("q", faq.termString(), queryFieldType)
    doc.add(queryField)

    doc.add(new StoredField("question", faq.question))
    doc.add(new StoredField("answer", faq.answer))
    doc.add(new StoredField("answerType", faq.answerType))
    doc.add(new StoredField("domain", faq.domain))

    writer.addDocument(doc)
  }

  def close(): Unit = {
    writer.close()
  }
}

object FaqIndexer {
  def main(args: Array[String]): Unit = {
    val faqs = Faq.load("./data/faq/fagui.txt")
    val indexer = new FaqIndexer(File("./data/faq/index").path)
    faqs.foreach(faq => indexer.index(faq))
    indexer.close()
    println("DONE.")
  }
}