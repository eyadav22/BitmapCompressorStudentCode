/******************************************************************************
 *  Compilation:  javac BitmapCompressor.java
 *  Execution:    java BitmapCompressor - < input.bin   (compress)
 *  Execution:    java BitmapCompressor + < input.bin   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   q32x48.bin
 *                q64x96.bin
 *                mystery.bin
 *
 *  Compress or expand binary input from standard input.
 *
 *  % java DumpBinary 0 < mystery.bin
 *  8000 bits
 *
 *  % java BitmapCompressor - < mystery.bin | java DumpBinary 0
 *  1240 bits
 ******************************************************************************/

/**
 *  The {@code BitmapCompressor} class provides static methods for compressing
 *  and expanding a binary bitmap input.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author Eisha Yadav
 */
public class BitmapCompressor {

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {

        // Do nothing if 0 bits present
        if (BinaryStdIn.isEmpty()) {
            BinaryStdOut.close();
            return;
        }
        // Read first bit (which is either a 0 or 1)
        boolean currentBit = BinaryStdIn.readBoolean();
        int runLength = 1;

        // Check if the bit is the same
        while (!BinaryStdIn.isEmpty()) {
            boolean bit = BinaryStdIn.readBoolean();
            if (bit == currentBit) {
                // If the same bit, continue the run
                runLength++;

                // If runLength exceeds 255 reset (1 byte max)
                if (runLength == 256) {
                    BinaryStdOut.write(runLength - 1);
                    runLength = 1;
                }
            } else {
                // Restart run with new bit value (has to be 0 or 1)
                BinaryStdOut.write(runLength);
                currentBit = bit;
                runLength = 1;
            }
        }

        // Write final run
        BinaryStdOut.write(runLength);
        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {
        // The first run corresponds to a run of 0s.
        boolean bit = false;
        while (!BinaryStdIn.isEmpty()) {
            int runLength = BinaryStdIn.readInt(8);
            for (int i = 0; i < runLength; i++) {
                BinaryStdOut.write(bit);
            }
            // Alternate bit value for next run
            // Taking advantage of the 0 or 1 nature.
            bit = !bit;
        }
        BinaryStdOut.close();
    }

    /**
     * When executed at the command-line, run {@code compress()} if the command-line
     * argument is "-" and {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}