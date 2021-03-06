package com.github.jleskovar.btcrpc.examples

import com.github.jleskovar.btcrpc.BitcoinRpcClient
import com.github.jleskovar.btcrpc.BitcoinRpcClientFactory

/**
 * Created by james on 17/12/17.
 */
fun main(args: Array<String>) {

//    val address = "n2hcSwULcFVpR7XZD6o83A7Ckvtohvb43t"
//    val privateKey = "cN88TB4Frqcb9S5hb889Y2HqrY3Wq5XSBwHTkWgxD48dCb9ippG8"

    val webSocketClient = BitcoinRpcClientFactory.createWsClient("james", "james", "localhost", 18334, true)
    val httpClient = BitcoinRpcClientFactory.createClient("james", "james", "localhost", 18334, true)

    // Ensure enough blocks before starting
    val numberOfBlocks = 100
    val diff = numberOfBlocks - httpClient.getBlockCount()
    if (diff > 0) {
        println("Generating $diff blocks...")
        httpClient.btcdGenerate(numberOfBlocks)
    }

    doPerfTestBlock("http", httpClient, numberOfBlocks)

    webSocketClient.connect()
    doPerfTestBlock("ws", webSocketClient, numberOfBlocks)
    webSocketClient.disconnect()
}

fun doPerfTestBlock(clientName: String, client: BitcoinRpcClient, blockCount: Int) {
    println("Starting retrieval of first $blockCount blocks using $clientName client..")
    val startTime = System.currentTimeMillis()
    for (i in 0..blockCount) {
        client.getBlockHash(i)
    }
    val duration = System.currentTimeMillis() - startTime
    println("$clientName client took $duration ms (${((blockCount.toFloat() / duration) * 1000).toInt()} requests/sec)")
}
