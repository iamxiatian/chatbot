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

  val termFieldType = new FieldType()
  termFieldType.setIndexOptions(IndexOptions.DOCS)
  termFieldType.setStored(true)
  termFieldType.setStoreTermVectors(true)
  termFieldType.setTokenized(false)

  def index(faq: Faq): Unit = {
    val doc = new Document()

    val queryField = new Field("q", faq.termString(), queryFieldType)
    doc.add(queryField)

    doc.add(new StoredField("question", faq.question))
    doc.add(new StoredField("answer", faq.answer))
    doc.add(new Field("answerType", faq.answerType, termFieldType))
    doc.add(new Field("domain", faq.domain, termFieldType))

    writer.addDocument(doc)
  }

  def close(): Unit = {
    writer.close()
  }
}

object FaqIndexer {
  def index(faqs: Seq[Faq]): Unit = {
    val indexer = new FaqIndexer(File("./data/faq/index").path)
    faqs.foreach(faq => indexer.index(faq))
    indexer.close()
    FaqBot.refresh()
  }

  def main(args: Array[String]): Unit = {
    case class Config(faqFile: Option[java.io.File] = None)

    val parser = new scopt.OptionParser[Config]("bin/faq-indexer") {
      opt[java.io.File]('f', "faqFile")
        .action((x, c) => c.copy(faqFile = Some(x)))
        .text("file to be added.")

      note("\n xiatian, xia@ruc.edu.cn.")
    }

    parser.parse(args, Config()) match {
      case Some(config) =>
        if (config.faqFile.nonEmpty) {
          if (config.faqFile.get.isFile && config.faqFile.get.exists) {
            println(s"parse faq: ${config.faqFile.get.getCanonicalPath}")
            print("... ")
            val faqs = Faq.load(config.faqFile.get.getCanonicalPath)
            index(faqs)
            println("DONE.")
          } else {
            println("faq training file does not exist or not a file")
            println(s"  ==> faqFile:  ${config.faqFile.get.getCanonicalPath}")
          }
        } else {
          println(parser.usage)
        }
      case None =>
        println("not valid parameters.")
        println(parser.usage)
    }
  }


}