package org.example;

import org.jcsp.lang.*;

public class Main {
    public static void main(String[] args) {

        // One Consumer, One Producer
//        System.out.println("Start system");
//        One2OneChannelInt channel = Channel.one2oneInt();
//
//        CSProcess[] process = {new Consumer(channel), new Producer(channel)};
//
//        Parallel program = new Parallel(process);
//        program.run();
        /*
        Bufor możę czekać zarówno na Konsumentów (requesty) jak i Producentów ( elementy, które wytworzą ).
        Trzeba zrobić, żeby było więcej Konsumentów i Producentów. Do tego trzeba użyć alternatyw dla Producentów i
        Konsumentów.
         */
//         Many Consumers, Many Producers
//        Any2AnyChannelInt channel = Channel.any2anyInt();
//        CSProcess[] processMany = {new Consumer(channel), new Consumer(channel), new Consumer(channel),
//                                    new Consumer(channel), new Consumer(channel), new Consumer(channel),
//                                    new Producer(channel), new Producer(channel), new Producer(channel),
//                                    new Producer(channel), new Producer(channel), new Producer(channel)};
//        Parallel programMany = new Parallel(processMany);
//        programMany.run();

        /*
        Bufor rozproszony pojedyczny (pojedyncza cześć). Wielu konsumentów i producentów.
        Na następny raz to musi chodzić. Wymyślić test, i parametry. Z przygotowania: semantyka CSP, czym jest
        alternatywa itd. W zadaniu na UPeLu napisane co trzeba robić. I trzeba load-balancing. Wady i zalety. To, że działa, to
        też zaleta. Do czego to się nadaję ? Można wykorzystać: Time-out'y.
         */

        /*
        Idea:
            Bufor posiada dwa stany: Stan zapełnienia i sprzedawania
            Stan zapełniania - wówczas próbuję on się napełnić. Robi to w taki sposób:
                1. Producent wysyła do bufera informację -> Wyprodukowane
                2. Bufor ustawia flagę na gotowy do odebrania i fokusuję się na danego producenta.
                3. Bufor dostaję porcję.
             Stan sprzedawania - to samo ale z konsumentem.
         */

        // Parameters
        int noProducers = 3;
        int noConsumers = 3;
        int noBuffers = 3;

        Buffer.maxBuffer = 6;
        Producer.maxRequest = 3;
        Consumer.maxRequest = 3;

        Producer producers[] = new Producer[noProducers];

        Consumer consumers[] = new Consumer[noConsumers];
        Buffer buffers[] = new Buffer[noBuffers];
        int noChannels = noProducers * noBuffers + noBuffers * noConsumers;
        One2OneChannelInt[] channels = new One2OneChannelInt[noChannels];
        One2OneChannelInt channelInt = Channel.one2oneInt();


        for(int i = 0; i < channels.length; i++){
            channels[i] = Channel.one2oneInt();
        }

        // Buffers channel
        for(int i = 0; i < noBuffers; i++){
            int index = 0;
            One2OneChannelInt[] producersChannel = new One2OneChannelInt[noProducers];
            One2OneChannelInt[] consumersChannel = new One2OneChannelInt[noConsumers];
            for (int j = i * noProducers; j < (i + 1) * noProducers ; j++){
                producersChannel[index] = channels[j];
                index++;
            }
            index = 0;
            for (int j = noProducers * noBuffers + i * noConsumers; j < noProducers * noBuffers + (i + 1) * noConsumers; j++){
                consumersChannel[index] = channels[j];
                index++;
            }
            buffers[i] = new Buffer(producersChannel, consumersChannel);
        }

        // Consumer Channel
        for(int i = 0; i < noConsumers; i++){
            One2OneChannelInt[] consumerChannel = new One2OneChannelInt[noBuffers];
            int index = 0;
            for(int j = noProducers * noBuffers + i; j < noChannels; j += noConsumers){
                consumerChannel[index] = channels[j];
                index++;
            }
            consumers[i] = new Consumer(consumerChannel);
        }

        // Producer Channel
        for(int i = 0; i < noProducers; i++){
            One2OneChannelInt[] producerChannel = new One2OneChannelInt[noBuffers];
            int index = 0;
            for(int j = i; j < noProducers * noBuffers; j += noProducers){
                producerChannel[index] = channels[j];
                index++;
            }
            producers[i] = new Producer(producerChannel);
        }

        CSProcess[] process = new CSProcess[noBuffers + noConsumers + noProducers];
        DataCollector dc = new DataCollector(buffers, 10000L);

        int index = 0;

        for (Buffer buffer: buffers){
            process[index] = buffer;
            index++;
        }

        for (Producer producer : producers){
            process[index] = producer;
            index++;
        }
        for (Consumer consumer: consumers){
            process[index] = consumer;
            index++;
        }

        dc.start();

        Parallel program = new Parallel(process);
        program.run();





    }
}