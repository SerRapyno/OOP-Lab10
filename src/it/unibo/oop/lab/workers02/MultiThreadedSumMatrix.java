package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {
    private final int nthread;

    public MultiThreadedSumMatrix(final int nthread) {
        super();
        this.nthread = nthread;
    }

    private static class Worker extends Thread {
        private final List<Double> matrixAsList;
        private final int startpos;
        private final int nelem;
        private long result;

        Worker(final List<Double> matrixAsList, final int startpos, final int nelem) {
            super();
            this.matrixAsList = matrixAsList;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        public void run() {
            System.out.println("Working from position " + this.startpos + " to position " + (this.startpos + this.nelem - 1));
            for (int i = this.startpos; i < this.matrixAsList.size() && i < this.startpos + nelem; i++) {
                this.result += this.matrixAsList.get(i);
            }
        }

        public long getResult() {
            return this.result;
        }

    }

    @Override
    public double sum(final double[][] matrix) {

        final List<Double> matrixAsList = new ArrayList<>();
        for (final double[] row : matrix) {
            for (final double value : row) {
                matrixAsList.add(value);
            }
        }
        double sum = 0;
        final int matrixLength = matrix.length * matrix[0].length;
        final int size = matrixLength % nthread + matrixLength / nthread;

        final List<Worker> workers = new ArrayList<>(nthread);

        for (int start = 0; start < matrixLength; start += size) {
            workers.add(new Worker(matrixAsList, start, size));
        }

        for (final Worker w: workers) {
            w.start();
        }

        for (final Worker worker : workers) {
            try {
                worker.join();
                sum = sum + worker.getResult();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("final sum: " + sum);
        return sum;
    }

}
