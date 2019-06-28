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

  def search(q: String, topN: Int = 5): Seq[Faq] = {
    val terms: Seq[TermQuery] = Faq.termArray(q).map {
      term =>
        new TermQuery(new Term("q", term))
    }

    val builder: BooleanQuery.Builder = new BooleanQuery.Builder()

    terms foreach {
      term =>
        builder.add(term, BooleanClause.Occur.SHOULD)
    }

    val query: BooleanQuery = builder.build()
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
    searcher.search("请问保密工作的基本方针是什么?")
    searcher.close()
  }
}