# parallel-asynchronous
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

### Fork/Join Framework

- Introduced in Java 7
- It is an extension to ExecutorService.
- Fork/Join Framework is designed to achieve **Data Parallelism**
- ExecutorService is designed to achieve **Task Based Parallelism**
- **Data Parallelism** is a concept here a given **Task** is recursively split into **SubTasks** until it reaches it least possible size and execute those tasks in parallel
- Has ForkJoin Pool to support Data Parallelism

#### ForkJoin Pool
- It contains shared work queue and worker threads.
- **Shared work queue**: To which clients submit the task.
- **Worker threads**: can have multiple threads (equal to number of cores in the machine - 1). each thread will have a **Double Ended Work Queue (deck)** 
- Each and every thread continuously poll **Shared Work Queue** for new tasks.
- If a Task can be divided into SubTasks then the Task is divided and placed in the Worker thread queue.
- **WorkStealing:** All threads looks for tasks in the other threads as well. so work is shared among threads equally. This is the reason we have Double Ended Work Queue.
- Sample implementation: https://github.com/nameishari/parallel-asyncronous-programming-java/blob/main/src/main/java/com/async/forkjoin/ForkJoinUsingRecursion.java


#### ForkJoin Task
- ForkJoin Task represents part of data and its computation.
- Two tasks we can use mostly: RecursiveTask (which returns a value) and RecursiveAction(which does not return a value)

#### Limitations of ForkJoin
- Too much of complexity involved when writing the code.
- **parallel streams** can be used to achieve the samething.

### parallel streams internals
- Three stages:
- **Split** the data into chunks.
- **Execute** the data chunks
- **Combine** the result.
- **Split**: if the collection is list, then the collection is split into chunks of elements into size 1. This is done by **SplitIterator**. For arraylist there is a class called **ArrayListSplitIterator**
- **Execute**:The splitted tasks are executed in a **CommonForkJoinPool**
- Split and execute happens in parallel
- **Combine:** eg: .collect(Collectors.toList())
- parallel streams does not guarantee faster performance. It needs to perform additions steps compared to sequential (split, execute and combine). If splitting and combining is easy then performance will be better.
- For linked lists the performance might be slower, as linked list is difficult to split.
- The final output order of the parallel streamed collection depends on: **The type of collection** and **SplitIterator implementation of the collection**
- Arraylist uses Ordered splititerator implementation while Set uses unorderd splititerator implementation.
- If the prallel stream operation involve boxing and unboxing then the performance will be poor than sequential stream.

#### CommonForkJoinPool
- Used by ParallelStreams and cannot switch to something else like ThreadPools
- Used by CompletableFuture and has options to use user defined thread pool.
- If you use parallel stream in an app, the CommonForkJoinPool is shared by the whole process of the app.
- By default the parallelism (no of worker threads) is set to: no of cores - 1. But can be changes using environment variable: -Djava.util.concurrent.ForkJoinPool.common.parallelism=100. But this will change the configuration of CommonForkJoinPool in the whole app. 
