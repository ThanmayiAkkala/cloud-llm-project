//package com.thanu.llm
//
//import org.apache.hadoop.conf.Configuration
//import org.apache.hadoop.fs.Path
//import org.apache.hadoop.io.{IntWritable, Text}
//import org.apache.hadoop.mapreduce.Job
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
//
//object LLMEncoderDriver {
//  def main(args: Array[String]): Unit = {
//    if (args.length != 2) {
//      System.err.println("Usage: LLMEncoderDriver <input path> <output path>")
//      System.exit(-1)
//    }
//
//    val conf: Configuration = new Configuration()
//    val job: Job = Job.getInstance(conf, "Token Count")
//
//    job.setJarByClass(this.getClass)
//    job.setMapperClass(classOf[TokenCountMapper])
//    job.setCombinerClass(classOf[TokenCountReducer]) // Optional combiner
//    job.setReducerClass(classOf[TokenCountReducer])
//
//    job.setOutputKeyClass(classOf[Text])
//    job.setOutputValueClass(classOf[IntWritable])
//
//    FileInputFormat.addInputPath(job, new Path(args(0))) // Input path
//    FileOutputFormat.setOutputPath(job, new Path(args(1))) // Output path
//
//    System.exit(if (job.waitForCompletion(true)) 0 else 1)
//  }
//}
package com.thanu.llm

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat

object LLMEncoderDriver {
  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println("Usage: Word2VecDriver <input path> <output path>")
      System.exit(-1)
    }

    // Set up Hadoop configuration and create a new job
    val conf = new Configuration()
    val job = Job.getInstance(conf, "Word2Vec Token Embedding")
    job.setJarByClass(LLMEncoderDriver.getClass)

    // Set input and output paths
    FileInputFormat.addInputPath(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))

    // Set the Mapper and Reducer classes
    job.setMapperClass(classOf[TokenCountMapper])
    job.setReducerClass(classOf[TokenCountReducer])

    // Set the output key and value types
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[Text])

    // Specify input and output format
    job.setInputFormatClass(classOf[TextInputFormat])
    job.setOutputFormatClass(classOf[TextOutputFormat[Text, Text]])

    // Submit the job and wait for it to complete
    System.exit(if (job.waitForCompletion(true)) 0 else 1)
  }
}
