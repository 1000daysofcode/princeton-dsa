/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private int[][] colorArray;
    private int curWidth, curHeight;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Picture is null");

        curWidth = picture.width();
        curHeight = picture.height();

        colorArray = new int[curWidth][curHeight];
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                colorArray[i][j] = picture.getRGB(i, j);
            }
        }
    }

    private boolean isBorder(int i, int j) {
        if (i == 0 || i == curWidth - 1 || j == 0 || j == curHeight - 1) {
            return true;
        }
        return false;
    }


    // current picture
    public Picture picture() {
        Picture pic = new Picture(curWidth, curHeight);
        for (int i = 0; i < curWidth; i++) {
            for (int j = 0; j < curHeight; j++) {
                pic.setRGB(i, j, colorArray[i][j]);
            }
        }
        // return copiedPic;
        return pic;
    }

    // width of current picture
    public int width() {
        // return copiedPic.width();
        return curWidth;
    }

    // height of current picture
    public int height() {
        // return copiedPic.height();
        return curHeight;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x <= -1 || x >= curWidth || y <= -1 || y >= curHeight)
            throw new IllegalArgumentException("Out of pic's range");

        double[][] energies = makeEnArray();

        return energies[x][y];
    }

    private double[][] makeEnArray() {
        double[][] energies = new double[curWidth][curHeight];
        for (int i = 0; i <= curWidth - 1; i++) {
            for (int j = 0; j <= curHeight - 1; j++) {
                // calculate energy and store in corresponding indices
                if (isBorder(i, j)) energies[i][j] = 1000;
                else energies[i][j] = getEnergy(i, j);
            }
        }
        return energies;
    }

    private double getEnergy(int i, int j) {
        double eY = calculateYX(i, j, true);
        double eX = calculateYX(i, j, false);
        double energy = Math.sqrt(eY + eX);
        return energy;
    }

    private double calculateYX(int x, int y, boolean vertical) {
        Color topLeft, bottomRight;

        if (vertical) {
            topLeft = new Color(colorArray[x][y - 1]);
            bottomRight = new Color(colorArray[x][y + 1]);
        }
        else {
            topLeft = new Color(colorArray[x - 1][y]);
            bottomRight = new Color(colorArray[x + 1][y]);
        }
        double red = Math.pow(Math.abs(topLeft.getRed() - bottomRight.getRed()), 2);
        double blue = Math.pow(Math.abs(topLeft.getBlue() - bottomRight.getBlue()), 2);
        double green = Math.pow(Math.abs(topLeft.getGreen() - bottomRight.getGreen()), 2);

        double YX = (red + blue + green);
        return YX;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    private void transpose() {
        int[][] tempP = new int[curHeight][curWidth];
        double[][] tempE = new double[curHeight][curWidth];
        for (int i = 0; i < tempE.length; i++) {
            for (int j = 0; j < tempE[0].length; j++) {
                tempP[i][j] = colorArray[j][i];
            }
        }
        colorArray = tempP;
        // energies = tempE;
        int tempHeight = curHeight;
        curHeight = curWidth;
        curWidth = tempHeight;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = getSeam(makeEnArray());
        return seam;
    }

    private int[] getSeam(double[][] en) {
        double[][] distTo = new double[curWidth][curHeight];
        int[][] parents = new int[curWidth][curHeight];
        int w = curWidth, h = curHeight;
        int parentIndex;

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                if (j == 0) distTo[i][j] = en[i][j];
                else if (curWidth < 2) {
                    parents[i][j] = 0;
                    distTo[i][j] = en[i][j] + distTo[i][j - 1];
                }
                else if (rightMost(i)) {
                    parentIndex = rightChoose2(distTo[1][j - 1], distTo[0][j - 1]);
                    parents[i][j] = parentIndex;
                    distTo[i][j] = en[i][j] + distTo[parentIndex][j - 1];
                }
                else if (leftMost(i)) {
                    parentIndex = leftChoose2(distTo[curWidth - 1][j - 1],
                                              distTo[curWidth - 2][j - 1]);
                    parents[i][j] = parentIndex;
                    distTo[i][j] = en[i][j] + distTo[parentIndex][j - 1];
                }
                else {
                    parentIndex = choose3(i, distTo[i - 1][j - 1], distTo[i][j - 1],
                                          distTo[i + 1][j - 1]);
                    parents[i][j] = parentIndex;
                    distTo[i][j] = en[i][j] + distTo[parentIndex][j - 1];
                }
            }
        }

        double distance = Double.POSITIVE_INFINITY;
        int[] seam = new int[h];
        int minIndex = Integer.MAX_VALUE;

        for (int i = 0; i < w; i++) {
            double newDist = distTo[i][h - 1];
            if (newDist <= distance) {
                distance = distTo[i][h - 1];
                minIndex = i;
            }
        }

        if (curWidth > 1 && curHeight > 1) {
            seam[h - 1] = minIndex;

            for (int i = h - 2; i > 0; i--) {
                seam[i] = parents[seam[i + 1]][i + 1];
            }

            seam[0] = seam[1];
        }
        return seam;
    }

    private boolean rightMost(int index) {
        return index == 0;
    }

    private boolean leftMost(int index) {
        return index == curWidth - 1;
    }

    private int leftChoose2(double rightBase, double leftBase) {
        int lastIndex = curWidth - 1;

        int choice = leftBase < rightBase ? lastIndex - 1 : lastIndex;

        return choice;
    }

    private int rightChoose2(double rightBase, double leftBase) {
        int choice = leftBase < rightBase ? 0 : 1;

        return choice;
    }

    private int choose3(int index, double rightBase, double midBase, double leftBase) {
        int choice = index;

        if (leftBase < rightBase) {
            if (leftBase < midBase) choice = index + 1;
            else if (leftBase == midBase) choice = index + 1;
            else choice += 0;
        }
        else if (rightBase < leftBase) {
            if (rightBase < midBase) choice = index - 1;
            else if (rightBase == midBase) choice = index - 1;
            else choice += 0;
        }
        else if (leftBase == rightBase) {
            if (midBase < leftBase || midBase == leftBase) choice += 0;
            else choice = index + 1;
        }

        return choice;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (curHeight <= 1) throw new IllegalArgumentException("Pic 1 pixel in height");
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Null seam");
        if (curWidth <= 1) throw new IllegalArgumentException("Pic 1 pixel w/h");
        if (seam.length != curHeight) throw new IllegalArgumentException("Seam incorrect length");
        for (int j = 0; j < seam.length; j++) {
            if (seam[j] < 0 || seam[j] > curWidth - 1)
                throw new IllegalArgumentException("Invalid seam");
            if (j > 0) if (seam[j] - seam[j - 1] < -1 || seam[j] - seam[j - 1] > 1)
                throw new IllegalArgumentException("Invalid seam");
            for (int i = seam[j]; i < curWidth - 1; i++) {
                colorArray[i][j] = colorArray[i + 1][j];
                // energies[i][j] = energies[i + 1][j];
            }
        }
        curWidth--;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        // Unused
    }
}
