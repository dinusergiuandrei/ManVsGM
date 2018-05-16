package utils;

public class TimeKeeper {
    Long startTime = System.currentTimeMillis();

    public Long tic(){
        startTime = System.currentTimeMillis();
        return startTime;
    }

    public Long tic(String message){
        System.out.println(message);
        startTime = System.currentTimeMillis();
        return startTime;
    }

    public Long toc(){
        Long time = System.currentTimeMillis() - startTime;
        System.out.println(time / 1000.0 + " seconds");
        return time;
    }

    public Long toc(String message){
        Long time = System.currentTimeMillis() - startTime;
        System.out.println(message + " ( " + time / 1000.0 + " seconds )");
        return time;
    }
}
