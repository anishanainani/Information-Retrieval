class Timer {

    private static int phase = 0;
    private static long startTime, endTime, elapsedTime;

    public static long timer() {
        if (phase == 0) {
            startTime = System.currentTimeMillis();
            phase = 1;
            return 0;
        } else {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            //System.out.println("\nTime: " + elapsedTime + " msec.");
            phase = 0;
            return elapsedTime;
        }
    }
}