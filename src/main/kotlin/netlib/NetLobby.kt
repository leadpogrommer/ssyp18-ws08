package netlib

import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import java.util.Arrays.asList
import java.util.concurrent.locks.ReentrantLock

class NetLobby(private val gameName: String,
               private val isHost: Boolean,
               private val nick: String,
               ip: String,
               private val net: Network,
               private val players: ArrayList<NetPlayer>,
               private val playersLock: ReentrantLock) :
        Thread("Lobby") {
    private val cons = createConsumer(ip, nick)
    private val prod = createProducer(ip)
    private val adm = NetAdmin(ip)
    private val topicName = "-LOBBY-$gameName"
    private var isGameReady = false
    private var exit = false
    var hostExited = false

    override fun run() {
        cons.assign(asList(TopicPartition(topicName, PartitionID.LOBBY.ordinal)))
        cons.seekToEnd(asList(TopicPartition(topicName, PartitionID.LOBBY.ordinal)))
        if (isHost) {
            if (!adm.lisTopics().contains(topicName)) adm.createTopic(topicName, 3)
            prod.send(ProducerRecord(topicName, PartitionID.LOBBY.ordinal, "newGame", gameName)).get()
        }
        adm.createTopic("-PLAYER-$nick", 1)
        prod.send(ProducerRecord(topicName, PartitionID.LOBBY.ordinal, "player", nick)).get()
        waitPlayers()
        if(exit or hostExited)return
        net.setGameStarted()
        if (!isHost) net.startGame()
    }

    private fun waitPlayers() {
        val part = TopicPartition(topicName, 0)
        cons.seekToEnd(asList(part))
        var records = cons.poll(10)
        var offset: Long = cons.endOffsets(asList(part))[part]!! - 1
        while (if (records.iterator().hasNext()) (records.iterator().next().key() != "newGame") else true) {
            cons.seek(part, offset)
            records = cons.poll(10)
            if (records.isEmpty) continue
            //println("$offset")
            offset--
        }
        cons.seek(part, ++offset)
        while (!isGameReady or exit) {
            records = cons.poll(100)
            for (r in records) {
                when(r.key()) {
                    "player" -> {
                        playersLock.lock()
                        players.add(NetPlayer(r.value(), true, false))
                        playersLock.unlock()
                    }
                    "leave" -> {
                        if(r.value() == players[0].nick){
                            println("olol")
                            hostExited = true
                            return
                        }
                        players.remove(NetPlayer(r.value(), true, false))
                    }
                    "state" -> if (r.value() == "ready")isGameReady = true

                }
            }
        }
        if (isHost) players[0].isHost = true
    }

    fun leave(){
        exit = true
    }
}
