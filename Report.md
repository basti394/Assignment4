Assignment 4
------------

# Team Members
Sebastian Jung
Linus Ihringer
# GitHub link to your (forked) repository (if submitting through GitHub)

...
https://github.com/basti394/Assignment4#

# Task 2

1. Why did message D have to be buffered and can we now always guarantee that all clients
   display the same message order?
>  Message D had to be buffered because its vector timestamp showed that it casually depends on at least one earlier message 
> that has not yet arrived or has not yet been delivered to the client
> With casual delivery, a message from sender i may only be displayed the i entry is exactly one larger than the receiver local entry,
> and all the other entries are less than or equal to the corresponding local entries.
> This means that different clients may have different messages buffered at different times.
> 
> For casuallichy related messages, we can guarantee that all clients display the same message order.
> However, for concurrent messages, different clients may display them in different orders.

2. Note that the chat application uses UDP. What could be an issue with this design choice—and
   how would you fix it?
> UDP is unrealiable, meaning that messages can be duplicated, lost or arrive out of order.
> That is risky for casual ordering, becuause the application assumed that every packet will arrive.
> 
> The best solution would be to use TCP instead of UDP, because TCP solves these issues by itself.
> If we want to stick with UDP, we would have to implement our own reliability mechanism.
> For example that each receiver sends an acknowledgment for each received message, If the sender does not receive an acknowledgment within a certain time frame, it resends the message.
   
# Task 3

1. What is potential causality in Distributed Systems, and how can you model it? Why
   “potential causality” and not just “causality”?
>Potential causality describes the relaionship between events where one event can potentially influence another event in a distributed system.
> It is modeled using the "happened-before" relation introduced by Lamport.
> if two events are in the same process, their local order is casual.
> If an event is the sending of a message, and another event is the receipt of that message, then the sending event happened before the receipt event.
> 
> Why "potential causality" and not just "causality"?
> Because we can only say influence was possible, not that it actually happened. (We cannot observe everything in a distributed system.)

2. If you look at your implementation of Task 2.1, can you think of one limitation of Vector Clocks? How would you overcome the limitation?
>
> One clear limitation is scalability. As the number of processes increases, the size of the vector clock also increases linearly.
> Every message carries a vector of lenghth n, and the update and comparison operations also take O(n) time.
> This can lead to significant overhead in terms of both bandwidth and processing time, especially in large-scale distributed systems.
> To overcome this limitation, we could consider using alternative logical clock mechanisms that are more scalable, such as
> Interval Tree Clocks or Version Vectors, which can provide similar causality tracking with reduced overhead.
> Another approach could be to implement a hierarchical vector clock system, where processes are grouped into clusters, and each cluster maintains its own vector clock.
> This would reduce the size of the vector clocks that need to be exchanged between processes in different clusters.

3. Figure 4 shows an example of enforcing causal communication using Vector Clocks. You can find a detailed explanation of this example and the broadcast algorithm being used in
   the Distributed Systems book by van Steen and Tannenbaum (see Chapter 5.2.2, page 270). Would you achieve the same result if you used the same broadcast algorithm but replaced
   Vector Clocks with Lamport Clocks? If not, why not? Explain briefly. 
>
> No, we would not achieve the same result if we replaced Vector Clocks with Lamport Clocks.
> Lamport Clocks only provide a single timestamp per event. They can produce an order that respects happened-before relationships,
> but they cannot detect wether two events are concurrent.
> For causal delivery the receiver needs exactely that information, it needs to know which message other messages need to be delivered first.
> Only Vector Clocks can provide that information, because they maintain a vector of timestamps, one for each process in the system.
> With Lamport Clocks, a client might display a message too early even if it causally depends on another message that has not yet arrived.
