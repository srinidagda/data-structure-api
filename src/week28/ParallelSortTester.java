package week28;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class ParallelSortTester {
  public static void main(String[] args) {
    runSorter();
  }

  /**
   * Runs a nested for loop of tests that call ParallelMergeSorter
   * and then checks the array afterwards to ensure correct sorting
   */
  public static void runSorter() {
    int SIZE = 1000; // Initial length of array to sort
    int ROUNDS = 15;
    int availableThreads = (Runtime.getRuntime().availableProcessors()) * 2;
    Integer[] a;

    Comparator<Integer> comp = new Comparator<Integer>() {
      @Override
      public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
      }
    };

    System.out.printf("\nMax number of threads== %d \n\n",availableThreads);
    for (int i=1;i <=availableThreads; i*=2) {
      if(i==1) {
        System.out.printf("%d Thread:\n", i);
      } else {
        System.out.printf("%d Threads:\n", i);
      }

      for (int j=0, k=SIZE;j<ROUNDS;++j, k*=2) {
        a = createRandomArray(k);
        //Run the algorithm and the how long it takes to sort the elements
        long startTime = System.currentTimeMillis();
        ParallerMergeSorter.sort(a, comp, availableThreads);
        long endTime = System.currentTimeMillis();
        if (!isSorted(a, comp)) {
          throw  new RuntimeException("Not sorted afterward: " + Arrays.toString(a));
        }
        System.out.printf("%10d elements => %6d ms \n", k, endTime-startTime );
      }
      System.out.print("\n");
    }
  }

  /**
   * Returns true if the given array is in sorted ascending order.
   * @param a the array to examine
   * @param comp the comparator to compare array elements
   * @param <E>
   * @return true if the given array is sorted, false otherwise
   */
  public static <E> boolean isSorted(E[] a, Comparator<? super E> comp) {
    for (int i = 0; i < a.length -1; i++) {
      if(comp.compare(a[i], a[i+1]) >0) {
        System.out.println(a[i]+ " > " + a[i+1]);
        return false;
      }
    }
    return true;
  }

  /**
   * Randomly rearranges the elements of the given array
   * @param a
   * @param <E>
   */
  public static <E> void shffule(E[] a) {
    for (int i =0; i < a.length;i++) {
      //Move the element i to a random index int [i... length-1]
      int randomIndex = (int) (Math.random() * a.length - 1);
      swap(a, i, i+randomIndex);
    }
  }

  /**
   * Swap the values at two given indexes in the given array
   * @param a
   * @param i
   * @param j
   * @param <E>
   */
  public static final <E> void swap(E[] a, int i, int j) {
    if (i != j) {
      E temp = a[i];
      a[i] = a[j];
      a[j] = temp;
    }
  }

  /**
   * Creates an array of the given length, fills it with random
   * @param length
   * @return
   */
  public static Integer[] createRandomArray(int length) {
    Integer[] a = new Integer[length];
    Random rand = new Random(System.currentTimeMillis());
    for (int i=0;i<a.length; i++) {
      a[i] = rand.nextInt(1000000);
    }
    return a;
  }
 }
