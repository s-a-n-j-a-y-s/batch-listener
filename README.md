# Kafka Batch Listener

A boilerplate Spring Boot project demonstrating how to consume Kafka messages in **batches** using Spring Kafka. Built with **Java 21** and **Spring Boot 3.5.3**, this template helps you get started quickly with high-throughput Kafka consumers.

---

## Why Batch Consumption?
Efficient message processing is critical in systems that operate under strict throughput or latency constraints. This project uses Kafka batch listeners to maximize performance while minimizing infrastructure complexity.

### ✅ Advantages of High Throughput with Limited Partitions
### Better Resource Efficiency
Achieve high message throughput without requiring a large number of Kafka partitions. This reduces memory and thread overhead on both Kafka brokers and consumers.

### Reduced Consumer Lag
Batching allows consumers to process multiple messages in a single poll cycle, reducing lag (i.e., delay between message arrival and processing) even under high message rates.

### Improved Parallelism Control
Concurrency can be fine-tuned independently from partition count. You can process messages in parallel using thread pools, without relying solely on Kafka partition-based scaling.

### Lower Operational Cost
Fewer partitions and consumers mean reduced network I/O, lower broker load, and simpler scaling logic—resulting in cost savings for both infrastructure and engineering.

### Optimized Acknowledgment Strategy
Manual or batch acknowledgment ensures offsets are committed only after successful batch processing, reducing the risk of message loss or duplication.

---

## Features

- ✅ Kafka batch message consumption
-  ⚡ High throughput extraction with limited partition
- ⚙️ Configurable concurrency, batch size, poll timeout
- 🔁 Manual and automatic offset management
- 🧪 Ready-to-extend message processing logic
- 📊 Logging and observability hooks
- 🧱 Modular and extensible architecture

---

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Kafka**
- **Maven**

## ▶️ Running the App
- Start Kafka and Zookeeper (e.g., via Docker or local install)
- Configure bootstrap-servers in application.yml 
- Build and run the app:
- ```mvn spring-boot:run```

## ⚙️Testing
You can test with a local Kafka instance using kafka-console-producer:

```bin/kafka-console-producer.sh   --bootstrap-server <kafka_broker_host>   --topic batch-listener-topic   --property "parse.key=true"   --property "key.separator=:"```

