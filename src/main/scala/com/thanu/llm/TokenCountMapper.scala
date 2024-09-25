//package com.thanu.llm
//
//import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
//import org.apache.hadoop.mapreduce.Mapper
//
//class TokenCountMapper extends Mapper[LongWritable, Text, Text, IntWritable] {
//  val one = new IntWritable(1)
//  val word = new Text()
//
//  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, IntWritable]#Context): Unit = {
//    // Split each line into words (tokens)
//    val tokens = value.toString.split("\\s+")
//    for (token <- tokens) {
//      word.set(token)
//      context.write(word, one) // Emit (token, 1)
//    }
//  }
//}
package com.thanu.llm

import com.typesafe.config.ConfigFactory
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import org.deeplearning4j.models.word2vec.Word2Vec
import scala.collection.JavaConverters._
import jtokkit.tokenizer.BPETokenizer // Assume this is the right package for BPE

class TokenCountMapper extends Mapper[LongWritable, Text, Text, Text] {

  // Load the configuration
  val config = ConfigFactory.load()

  // Load Word2Vec model from the path specified in the config file
  val word2VecModelPath = config.getString("word2vec.model.path")
  val word2Vec: Word2Vec = Word2Vec.load(new java.io.File(word2VecModelPath))

  // Initialize BPE Tokenizer with paths from config
  val bpeCodesPath = config.getString("bpe.codes.file.path")
  val bpeVocabPath = config.getString("bpe.vocab.file.path")
  val bpeTokenizer = new BPETokenizer(bpeCodesPath, bpeVocabPath)

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    // Tokenize the text using BPE
    val tokens = bpeTokenizer.tokenize(value.toString).asScala.filter(_.nonEmpty)

    // For each token, get the embedding and write it to context
    tokens.foreach { token =>
      if (word2Vec.hasWord(token)) {
        val embedding = word2Vec.getWordVector(token)
        val embeddingString = embedding.mkString(",")
        context.write(new Text(token), new Text(embeddingString))
      }
    }
  }
}

