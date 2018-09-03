package com.zj.webflux;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/8/25
 * Time: 15:32
 * Description: MAIN
 */
public class Forkjoin {

    public class MyRecursiveTask extends RecursiveTask<Long> {

        private static final long serialVersionUID = -3838564875609187428L;

        private long workLoad = 0;

        MyRecursiveTask(long workLoad) {
            this.workLoad = workLoad;
        }

        protected Long compute() {

            if (this.workLoad > 16) {
                System.out.println("Splitting workLoad : " + this.workLoad);

                List<MyRecursiveTask> subtasks = Lists.newArrayList();
                subtasks.addAll(createSubtasks());

                for(MyRecursiveTask subtask : subtasks){
                    subtask.fork();
                }
//                subtasks.forEach(ForkJoinTask::fork);
                Long result = 0L;
                for (MyRecursiveTask subtask : subtasks) {
                    result += subtask.join();
                }
                return result;
//            subtasks.stream().collect(Collectors.summingLong(e->e.join()));

            } else {
//                System.out.println("Doing workLoad myself: " + this.workLoad);
                return workLoad * 3;
            }
        }

        private List<MyRecursiveTask> createSubtasks() {
            List<MyRecursiveTask> subtasks = Lists.newArrayList();

            MyRecursiveTask subtask1 = new MyRecursiveTask(this.workLoad / 2);
            MyRecursiveTask subtask2 = new MyRecursiveTask(this.workLoad / 2);

            subtasks.add(subtask1);
            subtasks.add(subtask2);

            return subtasks;
        }
    }

    @Test
    public void task() {
        MyRecursiveTask myRecursiveTask = new MyRecursiveTask(128);
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        long mergedResult = forkJoinPool.invoke(myRecursiveTask);

        System.out.println("mergedResult = " + mergedResult);

//        Instrumentation instr = AgentGetter.getInstrumentation();
    }

}
