package com.javafst.operations;

import com.javafst.Arc;
import com.javafst.Fst;
import com.javafst.State;
import com.javafst.semiring.Semiring;
import com.javafst.utils.Pair;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;


/**
 * N-shortest paths operation.
 * 
 * <p>See: M. Mohri, M. Riley,
 * "An Efficient Algorithm for the n-best-strings problem", Proceedings of the
 * International Conference on Spoken Language Processing 2002 (ICSLP '02).
 * 
 * <p>See: M. Mohri,
 * "Semiring Framework and Algorithms for Shortest-Distance Problems", Journal
 * of Automata, Languages and Combinatorics, 7(3), pages. 321-350, 2002.
 * 
 */
public class NShortestPaths {

  /**
   * Calculates the shortest distances from each state to the final.
   * 
   * @param fst      The fst to calculate the shortest distances.
   * @return         The array containing the shortest distances.
   */
  public static float[] shortestDistance(final Fst fst) {

    final Fst reversed = Reverse.get(fst);

    final float[] d = new float[reversed.getNumStates()];
    final float[] r = new float[reversed.getNumStates()];

    final Semiring semiring = reversed.getSemiring();

    Arrays.fill(d, semiring.zero());
    Arrays.fill(r, semiring.zero());

    final LinkedHashSet<State> queue = new LinkedHashSet<State>();

    queue.add(reversed.getStart());

    d[reversed.getStart().getId()] = semiring.one();
    r[reversed.getStart().getId()] = semiring.one();

    while (!queue.isEmpty()) {
      final State q = queue.iterator().next();
      queue.remove(q);

      final float rnew = r[q.getId()];
      r[q.getId()] = semiring.zero();

      for (final Arc a : q.arcs()) {
        final State nextState = a.getNextState();
        final float dnext = d[a.getNextState().getId()];
        final float dnextnew = semiring.plus(dnext,
            semiring.times(rnew, a.getWeight()));
        if (dnext != dnextnew) {
          d[a.getNextState().getId()] = dnextnew;
          r[a.getNextState().getId()] = 
              semiring.plus(r[a
                              .getNextState().getId()],semiring.times(rnew,
                                  a.getWeight()));
          if (!queue.contains(nextState)) {
            queue.add(nextState);
          }
        }
      }
    }
    return d;
  }

  /**
   * Calculates the n-best shortest path from the initial to the final state.
   * 
   * @param fst          The fst to calculate the nbest shortest paths.
   * @param n            Number of best paths to return.
   * @param determinize  If true the input fst will be determinized prior the.
   *                     operation.
   * @return             An fst containing the n-best shortest paths.
   */
  public static Fst get(final Fst fst, final int n, final boolean determinize) {
    if (fst == null) {
      return null;
    }

    if (fst.getSemiring() == null) {
      return null;
    }
    Fst fstdet = fst;
    if (determinize) {
      fstdet = Determinize.get(fst);
    }
    final Semiring semiring = fstdet.getSemiring();
    final Fst res = new Fst(semiring);
    res.setIsyms(fstdet.getIsyms());
    res.setOsyms(fstdet.getOsyms());

    final float[] d = shortestDistance(fstdet);

    ExtendFinal.apply(fstdet);

    final int[] r = new int[fstdet.getNumStates()];

    final PriorityQueue<Pair<State, Float>> queue = new PriorityQueue<Pair<State, Float>>(
        10, new Comparator<Pair<State, Float>>() {

          @Override
          public int compare(Pair<State, Float> o1,
              Pair<State, Float> o2) {
            final float previous = o1.getRight();
            final float d1 = d[o1.getLeft().getId()];

            final float next = o2.getRight();
            final float d2 = d[o2.getLeft().getId()];

            final float a1 = semiring.times(next, d2);
            final float a2 = semiring.times(previous, d1);

            if (semiring.naturalLess(a1, a2)) {
              return 1;
            }

            if (a1 == a2) {
              return 0;
            }
            return -1;
          }
        });

    final HashMap<Pair<State, Float>, Pair<State, Float>> previous = 
        new HashMap<Pair<State, Float>, Pair<State, Float>>(fst.getNumStates());
    final HashMap<Pair<State, Float>, State> stateMap = new HashMap<Pair<State, Float>, State>(
        fst.getNumStates());

    final State start = fstdet.getStart();
    final Pair<State, Float> item = new Pair<State, Float>(start, semiring.one());
    queue.add(item);
    previous.put(item, null);

    while (!queue.isEmpty()) {
      final Pair<State, Float> pair = queue.remove();
      final State p = pair.getLeft();
      final Float c = pair.getRight();

      final State s = new State(p.getFinalWeight());
      res.addState(s);
      stateMap.put(pair, s);
      if (previous.get(pair) == null) {
        // this is the start state
        res.setStart(s);
      } else {
        // add the incoming arc from previous to current
        final State previouState = stateMap.get(previous.get(pair));
        final State previousOldState = previous.get(pair).getLeft();
        for (final Arc a : previousOldState.arcs()) {
          if (a.getNextState().equals(p)) {
            previouState.addArc(new Arc(a.getIlabel(), a
                .getOlabel(), a.getWeight(), s));
          }
        }
      }

      final Integer stateIndex = p.getId();
      r[stateIndex]++;

      if ((r[stateIndex] == n) && (p.getFinalWeight() != semiring.zero())) {
        break;
      }

      if (r[stateIndex] <= n) {
        for (final Arc a : p.arcs()) {
          final float cnew = semiring.times(c, a.getWeight());
          final Pair<State, Float> next = new Pair<State, Float>(
              a.getNextState(), cnew);
          previous.put(next, pair);
          queue.add(next);
        }
      }
    }
    return res;
  }
}
