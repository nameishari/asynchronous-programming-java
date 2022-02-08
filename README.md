# parallel-asyncronous
This repo has the code for parallel and asynchronous programming in Java

## Threads

- Introduced in Java 1
- Threads are basically used to run background task
- Sample implementation: https://github.com/nameishari/parallel-asyncronous-programming-java/blob/main/src/main/java/com/async/thread/ProductServiceUsingThread.java

## Limitations of Thread API

- We need to create the runnable, create the thread, run the thread and join the thread, in order to offload our tasks as background tasks
- Runnable does not take any input and does not produce any output. need to use variables inside of the runnable.
- Threads are a bit expensive: they have their own runtime-stack, memory, registers etc, (To solve this problem thread pool was created)