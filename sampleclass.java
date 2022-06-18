public class sampleclass {

    public void authorize(int k){
        if (k%15==0)
            System.out.println("FizzBuzz");
        else if (k%5==0)
            System.out.println("Buzz");
        else if (k%3==0)
            System.out.println("Fizz");
        else
            System.out.println(k);
    }

    public void fizzBuzz(int n){
        for (int i=1; i<=n; i++)
            authorize(i);
    }
}

