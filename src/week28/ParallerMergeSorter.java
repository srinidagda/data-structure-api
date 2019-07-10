package week28;

import java.util.Comparator;

/**
 * This class carries out the range to  merge sort algo in parallel
 *
 */
public class ParallerMergeSorter extends Thread {
  /**
   * Sorts any array, using the merge sort algorithm
   *
   * @param a array to sort
   * @param comp the comparator to compare array elements
   */
  public static <E> void sort (E[] a, Comparator<? super E> comp, int threads) {
    parallelMergeSort(a, 0, a.length-1, comp, threads);
  }

  /**
   * Sort a range of array, using the merge sort algorithm
   * @param a
   * @param from
   * @param to
   * @param comp
   * @param <E>
   */
  private static <E> void mergeSort(E[] a, int from, int to,
                                    Comparator<? super E> comp) {
    if (from == to) {
      return;
    } else {
      if ((to - from) >0) {
        int mid = (from + to)/2;
        mergeSort(a, from, mid, comp);
        mergeSort(a, mid+1, to, comp);
        merge(a, from, mid, to, comp);
      }
    }
  }

  /**
   * Takes an array and merge sorts it in parallel if there
   * are multiple threads
   * @param a is an array to sort
   * @param from from the first value to sort
   * @param to is the last value to sort
   * @param comp is the comparator that check two numbers
   * @param threads available threads is how many threads there are to utilize
   * @param <E>
   */
  private static <E> void parallelMergeSort(E[] a, int from, int to, Comparator<? super E> comp, int threads) {
    if ((to -from) > 0) {
      if (threads <= 1) {
        mergeSort(a, from, to, comp);
      } else {
        int mid = to/2;
        Thread firstHalf = new Thread() {
          @Override
          public void run() {
            parallelMergeSort(a, from, mid, comp, threads-1);
          }
        };
        Thread secondHalf = new Thread() {
          @Override
          public void run() {
            parallelMergeSort(a, mid+1, to, comp,threads -1);
          }
        };
        firstHalf.start();
        secondHalf.start();
        try {
          firstHalf.join();
          secondHalf.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        merge(a, from, mid, to, comp);
      }
    }
  }

  /**
   *
   * @param a
   * @param from
   * @param mid
   * @param to
   * @param comp
   * @param <E>
   */
  private static <E> void merge(E[] a, int from, int mid, int to, Comparator<? super E> comp) {
    int n = to - from + 1;
    //Size of the range to be merged

    //Merge both halves into a temporary array b
    Object[] b = new Object[n];

    int i1 = from;
    int i2 = mid + 1;
    int j = 0;
    while (i1 <= mid && i2 <= to) {
      if (comp.compare(a[i1], a[i2]) <0 ){
        b[j] = a[i1];
        i1++;
      } else {
        b[j] = a[i2];
        i2++;
      }
      j++;
    }
    while (i1 <= mid) {
      b[j] = a[i1];
      i1++;
      j++;
    }
    while (i2 <= to) {
      b[j] = a[i2];
      i2++;
      j++;
    }
    for (j =0; j<n;j++) {
      a[from +j] = (E) b[j];
    }
  }
}
