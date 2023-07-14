import java.util.*;

class SortingOdd<T> implements Runnable{
    ArrayList<T> arr;
    int n;
    public SortingOdd(ArrayList<T> arr, int n)
    {
        this.arr=arr;
        this.n=n;
    }
    @Override
    public void run()
    {
        oddEvenSort.oddSort((ArrayList<Float>)arr, n);
    }
}
class SortingEven<T> implements Runnable{
    ArrayList<T> arr;
    int n;
    public SortingEven(ArrayList<T> arr, int n)
    {
        this.arr=arr;
        this.n=n;
    }
    @Override
    public void run()
    {
        oddEvenSort.evenSort((ArrayList<Float>)arr, n);
    }
}

public class oddEvenSort
{
    
    public static float randomCGPAGen()
    {
        float x=(float)(Math.random()*(10.00-0.00)+0.00)*1000;
        float y=(int)x;
        x=y/1000;
        return x;
    }
    public static void oddSort(ArrayList<Float> arr, int n) 
    {
        float temp;
        for(int i=1;i<=n-2;i+=2)
        {
            if(arr.get(i)<arr.get(i+1))
            {
                temp=arr.get(i);
                arr.set(i,arr.get(i+1));
                arr.set(i+1,temp);
            }
        }
    }
    public static void evenSort(ArrayList<Float> arr,int n)
    {
        float temp;
        for(int i=0;i<=n-2;i+=2)
        {
            if(arr.get(i)<arr.get(i+1))
            {
                temp=arr.get(i);
                arr.set(i,arr.get(i+1));
                arr.set(i+1,temp);
            }
        }
    }
    public static void printArray(ArrayList<Float> arr)
    {
        for (int i = 0; i < arr.size(); i++) {
            System.out.print(arr.get(i)+" \\ ");
        }
    }
    public static void main(String[] args) throws InterruptedException 
    {
        int sizes[]={1,10,100,1000,10000};
        ArrayList<Float> arr1=new ArrayList<Float>();
        ArrayList<Float> arr2=new ArrayList<Float>();
        int n;
        for(int k=0;k<5;k++)
        {
            n=sizes[k];
            float x;
            //generating random floating point values
            for(int i=0;i<n;i++)
            {
                x=randomCGPAGen();
                arr1.add(x);
                arr2.add(x);
            }

            System.out.println("\nFor array of size "+sizes[k]+" :");

            //Sorting simply without threads
            long start=System.nanoTime();
            for(int i=1;i<=n;i++)
            {
                if(i%2==1)
                {
                    oddSort(arr1, n);
                }
                else
                {
                    evenSort(arr1, n);
                }
            }
            long stop=System.nanoTime();
            float timelapsed=(stop-start)/1000000000f;
            System.out.println("Time required to sort the array without Threads : "+timelapsed+" s");

            //Sorting with 2 threads
            start=System.nanoTime();
            
            SortingOdd<Float> odd=new SortingOdd<Float>(arr2,n);
            SortingEven<Float> even=new SortingEven<Float>(arr2,n);
            Thread threads[]=new Thread[n];
            for(int i=0;i<n;i++)
            {
                if(i%2==1)
                {
                    threads[i]=new Thread(even);
                }
                else
                {
                    threads[i]=new Thread(odd);
                }
            }
            // for(int i=0;i<n;i++)
            // {
            //     if(i%2==1)
            //     {
            //         threads[i].start();
            //         threads[i].join();
            //     }
            //     else
            //     {
            //         threads[i].start();
            //         threads[i].join();
            //     }
            // }
            for(int i=0;i<n;i+=2)
            {
                if(i+1<n)
                {
                    threads[i].start();
                    threads[i+1].start();
                    threads[i].join();
                    threads[i+1].join();
                }
                else
                {
                    threads[i].start();
                    threads[i].join();
                }
            }
            stop=System.nanoTime();
            timelapsed=(stop-start)/1000000000f;
            System.out.println("Time required to sort the array with Threads : "+timelapsed+" s");
            arr1.clear();
            arr2.clear();
        }
        }
    }
