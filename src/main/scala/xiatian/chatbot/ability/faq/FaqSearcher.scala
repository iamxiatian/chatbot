package xiatian.chatbot.ability.faq

import java.nio.file.Path

import better.files.File
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index._
import org.apache.lucene.search._
import org.apache.lucene.store.FSDirectory


class FaqSearcher(indexDir: Path = File("./data/faq/index").path) {
  val analyzer = new StandardAnalyzer()
  val config = new IndexWriterConfig(analyzer)
  config.setOpenMode(IndexWriterConfig.OpenMode.APPEND)

  private val reader: IndexReader = DirectoryReader.open(FSDirectory.open(indexDir))

  private val searcher: IndexSearcher = new IndexSearcher(reader)

  /**
    * @param q                检索条件
    * @param domainFieldValue 隶属的领域，多个领域之间用逗号分割，例如: general,medicine
    * @param topN
    * @return
    */
  def search(q: String,
             domainFieldValue: Option[String],
             topN: Int = 5): Seq[Faq] = {
    val terms: Seq[TermQuery] = Faq.termArray(q).map {
      term =>
        new TermQuery(new Term("q", term))
    }

    val builder: BooleanQuery.Builder = new BooleanQuery.Builder()

    terms.foreach {
      term =>
        builder.add(term, BooleanClause.Occur.SHOULD)
    }

    //val query: BooleanQuery = builder.build()
    val domains: Set[String] = domainFieldValue.toList.flatMap {
      text =>
        text.split(",").map(_.trim).filter(_.nonEmpty)
    }.toSet

    val query: BooleanQuery = if (domains.isEmpty || domains.contains("all")) {
      builder.build()
    } else {
      val domainBuilder: BooleanQuery.Builder = new BooleanQuery.Builder()
      domains.foreach {
        d =>
          domainBuilder.add(
            new TermQuery(new Term("domain", d)),
            BooleanClause.Occur.SHOULD)
      }
      val domainQuery: BooleanQuery = domainBuilder.build()

      //val domainClause = new TermQuery(new Term("domain", domain.get))
      new BooleanQuery.Builder()
        .add(domainQuery, BooleanClause.Occur.MUST)
        .add(builder.build(), BooleanClause.Occur.SHOULD)
        .build()
    }

    println(query.toString())

    val hits: Array[ScoreDoc] = searcher.search(query, topN).scoreDocs
    hits.map {
      hit =>
        val doc = searcher.doc(hit.doc)

        //print(s"${hit.score}\t")
        Faq(doc.get("question"),
          doc.get("answer"),
          doc.get("answerType"),
          doc.get("domain"))
    }


  }

  def close() = {
    reader.close()
  }
}

object FaqSearcher {
  def main(args: Array[String]): Unit = {
    val searcher = new FaqSearcher()
    val faqs = searcher.search("请问保密工作的基本方针是什么?", Some(""))
    faqs.foreach(println)
    searcher.close()
  }
}