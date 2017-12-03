
package se.kth.ict.id1212.minor.hw3.client.view;

class ThreadSafeStdOut {
    /**
     * Prints the specified output to <code>System.out</code>,
     *
     * @param output The output to print.
     */
    synchronized void print(String output) {
        System.out.print(output);
    }

    /**
     * Prints the specified output, plus a line break, to <code>System.out</code>,
     *
     * @param output The output to print.
     */
    synchronized void println(String output) {
        System.out.println(output);
    }
}