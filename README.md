# parallel-asyncronous
This repo has the code for parallel and asynchronous programming in Java

## Threads

- Introduced in Java 1
- Threads are basically used to run background task
- Sample implementation: https://github.com/nameishari/parallel-asyncronous-programming-java/blob/main/src/main/java/com/async/thread/ProductServiceUsingThread.java

#### Limitations of Thread API

- We need to create the runnable, create the thread, run the thread and join the thread, in order to offload our tasks as background tasks
- Runnable does not take any input and does not produce any output. need to use variables inside of the runnable.
- Threads are a bit expensive: they have their own runtime-stack, memory, registers etc, (To solve this problem thread pool was created)

### Thread Pool

- Introduced in Java 5
- Thread pool is a group of threads created and readily available.
- For CPU intensive tasks, the thread pool size should be equal to number of cores in the machine
- For I/O intensive task (DB call or Rest api call), the thread pool size should be greater than the number of cores in the machine. Because the threads executed the task will be waiting for the most time.
- No need to manually create, start and join the threads.
- Achieving concurrency in the application.

### Executor Service

- Introduced in Java 5
- It is an Asynchronous task executing engine.
- It provides a way to asynchronously execute tasks and provide results in a much simpler way compared to threads.
- Different components: Thread pool, WorkQueue, Completion Queue.
- Whenever a code submits a task to executor service, the task is placed in the **WorkQueue**, as soon as the task is placed in the workqueue a **Future** object is returned. It holds a reference to the result that appears later.
- Once the task is available in the work queue, one of the thread from the thread pool will take the task, and execute the task and place the task in the **CompletionQueue**. Code uses **Future** to get the result.
- Sample implementation: https://github.com/nameishari/parallel-asyncronous-programming-java/blob/main/src/main/java/com/async/executorservice/ProductServiceUsingExecutorService.java

#### Limitations of Threadpool and ExecutorService

- ExecutorService is designed to block the thread, we are doing a get call on the **Future**, and this get call is going to block the caller thread.
- Future is designed to block the task.
- There is not a good way to combine multiple Futures.