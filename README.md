# Traffic simulation in Java 

The main method will run a traffic simulation of one of the intersections in Uppsala

# The simulations output will be of the following format.
* ------------- Time = 1 ----------------------

(R)<[W, W, W, W, W, W, W, W]><[W, S, W, W, S, W, W, W, S, S]>

(R)<[S, ., ., ., ., ., ., .]> Q [W, S, W, S]

* ------------- Time = 2 ----------------------
(R)<[W, W, W, W, W, W, W, W]><[W, S, W, W, S, W, W, W, S, S]>

(G)<[S, ., ., ., ., ., ., .]> Q [W, S, W, S]

The first line corresponds to the trafic lights beeing either (G)-green or (R)-red.

The letters inside the array gives the heading of the vehicle, either S-South or W-West.

The Q-queue array is where cars wait if both arrays are full.
