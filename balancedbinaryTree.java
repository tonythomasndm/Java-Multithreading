import java.util.*;


class Node
{
    int data;
    Node left,right;
    public Node(int data)
    {
        this.data=data;
        right=null;
        left=null;
    }
    public void setLeft(Node left)
    {
        this.left=left;
    }
    public Node getLeft()
    {
        return this.left;
    }
    public void setRight(Node right)
    {
        this.right=right;
    }
    public Node getRight()
    {
        return this.right;
    }
}
class Searching<T> implements Runnable
{
    Node node;
    T key;
    private volatile boolean value;
    Searching(Node node, T key)
    {
        this.node=node;
        this.key=key;
        value=false;
    }
    @Override
    public void run() 
    {
        value=balancedbinaryTree.searchTree(node,(int)key);
    }
    public boolean getValue()
    {
        return value;
    }
}
class MakerTree<T> implements Runnable{
    private volatile Node node;
    ArrayList<Integer> arr;
    int index;
    public MakerTree(ArrayList<Integer> arr, int index)
    {
        this.arr=arr;
        this.index=index;
    }
    @Override
    public void run()
    {
        node=balancedbinaryTree.makeTree(arr, index);
    }
    public Node getNode()
    {
        return node;
    }
}
class HeightMeasure implements Runnable {
    Node node;
    private volatile int ht;
    public HeightMeasure(Node node)
    {
        this.node=node;
    }
    @Override
    public void run()
    {
        ht=balancedbinaryTree.heightMeasure(node);
    }
    public int getHt()
    {
        return ht;
    }
}
public class balancedbinaryTree
{
    static int max=(int)(Math.pow(10,9));
    static int min=0-max;
    static int n;
    static boolean searchTree(Node node,int key)
    {
        if(node==null)
        return false;
        if(node.data==key)
        return true;
        if(searchTree(node.getLeft(),key)||searchTree(node.getRight(),key))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    static Node makeTree(ArrayList<Integer> arr,int index)
    {
        Node node=null;
        if(index<arr.size())
        {
            node=new Node(arr.get(index));
            node.setLeft(makeTree(arr,2*index+1));
            node.setRight(makeTree(arr,2*index+2));
        }
        return node;
    }
    static int heightMeasure(Node node)
    {
        int i=0;
        while(node!=null)
        {
            node=node.left;
            i+=1;
        }
        return i;
    }
    public static int randomNum()
    {
        return (int)(Math.random()*(max-min)+min);
    }
    public static int randomKey(int size)
    {
        return (int)(Math.random()*(size-0)+0);
    }
    public static void main(String[] args) throws InterruptedException 
    {
        int sizes[]={10,1000,(int)Math.pow(10,6)};
        ArrayList<Integer> arr=new ArrayList<Integer>();
        for(int k=0;k<3;k++)
        {
            Node root=null;
            //Random Array Generation
            for(int i=0;i<sizes[k];i++)
            {
                arr.add(randomNum());
            }
            n=sizes[k];

            //making tree
            long start=System.nanoTime();
            root=new Node(arr.get(0));
            root.setLeft(makeTree(arr,1));
            root.setRight(makeTree(arr,2));
            long stop=System.nanoTime();
            float timelapsed=(stop-start)/1000000000f;
            System.out.println("\nFor an array size : "+n);
            System.out.println("WITHOUT THREADS:");
            System.out.println("Time taken to construct the tree : "+timelapsed+" s");

            //measuring height
            start=System.nanoTime();
            int ht=heightMeasure(root);
            System.out.println("Height of the Tree : "+ht);
            stop=System.nanoTime();
            timelapsed=(stop-start)/1000000000f;
            System.out.println("Time taken to measure height of the tree : "+timelapsed+" s");

            //searching time
            start=System.nanoTime();
            int key=randomKey(n);
            if (searchTree(root,arr.get(key)))
            {
                System.out.println("Element Found!!");
            }
            else{
                System.out.println("Element not found!!");
            }
            stop=System.nanoTime();
            timelapsed=(stop-start)/1000000000f;
            System.out.print("Time Taken to search an element : ");
            System.out.println(timelapsed+" s");
            
            //With 2 Threads
            //Making Tree
            System.out.println("\nWITH 2 THREADS");
            start=System.nanoTime();
            root=null;
            root=new Node(arr.get(0));
            MakerTree objM1= new MakerTree(arr, 1);
            MakerTree objM2= new MakerTree(arr, 2);
            Thread thread1=new Thread(objM1);
            Thread thread2=new Thread(objM2);
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            root.setLeft(objM1.getNode());
            root.setRight(objM2.getNode());
            stop=System.nanoTime();
            timelapsed=(stop-start)/1000000000f;
            System.out.println("Time taken to construct the tree : "+timelapsed+" s");

            //height of tree
            start=System.nanoTime();
            HeightMeasure objH1= new HeightMeasure(root.left);
            HeightMeasure objH2= new HeightMeasure(root.right);
            thread1=new Thread(objH1);
            thread2=new Thread(objH2);
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            ht=Math.max(objH1.getHt(),objH2.getHt())+1;
            System.out.println("Height of the tree : "+ht);
            stop=System.nanoTime();
            timelapsed=(stop-start)/1000000000f;
            System.out.println("Time taken to measure height of the tree : "+timelapsed+" s");

            //searching an element
            start=System.nanoTime();
            if(root.data!=arr.get(key))
            {
                Searching objS1=new Searching(root.left, arr.get(key));
                Searching objS2=new Searching(root.right, arr.get(key));
                thread1=new Thread(objS1);
                thread2=new Thread(objS2);
                thread1.start();
                thread2.start();
                thread1.join();
                thread2.join();
                if(objS1.getValue() || objS2.getValue())
                {
                    System.out.println("Element Found!!");
                }
                else
                {
                    System.out.println("Element Not Found!!");
                }
            }
            else
            {
                System.out.println("Element Found!!");
            }
            stop=System.nanoTime();
            System.out.print("Time Taken to search an element : ");
            timelapsed=(stop-start)/1000000000f;
            System.out.println(timelapsed+" s");


            //WITH 4 THREADS
            System.out.println("\nWITH 4 THREADS");

            //Making Tree
            start=System.nanoTime();
            root=null;
            root=new Node(arr.get(0));
            root.setLeft(new Node(arr.get(1)));
            root.setRight(new Node(arr.get(2)));
            objM1= new MakerTree(arr, 3);
            objM2= new MakerTree(arr, 4);
            MakerTree objM3= new MakerTree(arr, 5);
            MakerTree objM4= new MakerTree(arr, 6);
            thread1=new Thread(objM1);
            thread2=new Thread(objM2);
            Thread thread3=new Thread(objM3);
            Thread thread4=new Thread(objM4);
            thread1.start();
            thread2.start();
            thread3.start();
            thread4.start();
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            root.left.setLeft(objM1.getNode());
            root.left.setRight(objM2.getNode());
            root.right.setLeft(objM3.getNode());
            root.right.setRight(objM4.getNode());
            stop=System.nanoTime();
            timelapsed=(stop-start)/1000000000f;
            System.out.println("Time taken to construct the tree : "+timelapsed+" s");

            //measuring height
            start=System.nanoTime();
            objH1= new HeightMeasure(root.left.left);
            objH2= new HeightMeasure(root.left.right);
            HeightMeasure objH3= new HeightMeasure(root.right.left);
            HeightMeasure objH4= new HeightMeasure(root.right.right);
            thread1=new Thread(objH1);
            thread2=new Thread(objH2);
            thread3=new Thread(objH3);
            thread4=new Thread(objH4);
            thread1.start();
            thread2.start();
            thread3.start();
            thread4.start();
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            ht=Math.max(Math.max(objH1.getHt(),objH2.getHt())+1,Math.max(objH3.getHt(),objH4.getHt())+1)+1;
            System.out.println("Height of the tree : "+ht);
            stop=System.nanoTime();
            timelapsed=(stop-start)/1000000000f;
            System.out.println("Time taken to measure height of the tree : "+timelapsed+" s");

            //searching an element
            start=System.nanoTime();
            if(root.data!=arr.get(key) && root.left.data!=arr.get(key)&& root.right.data!=arr.get(key))
            {
                Searching objS1=new Searching(root.left.left, arr.get(key));
                Searching objS2=new Searching(root.left.right, arr.get(key));
                Searching objS3=new Searching(root.right.left, arr.get(key));
                Searching objS4=new Searching(root.right.right, arr.get(key));

                thread1=new Thread(objS1);
                thread2=new Thread(objS2);
                thread3=new Thread(objS3);
                thread4=new Thread(objS4);
                thread1.start();
                thread2.start();
                thread3.start();
                thread4.start();
                thread1.join();
                thread2.join();
                thread3.join();
                thread4.join();
                if(objS1.getValue() || objS2.getValue() || objS3.getValue() || objS4.getValue())
                {
                    System.out.println("Element Found!!");
                }
                else
                {
                    System.out.println("Element Not Found!!");
                }
            }
            else{
                System.out.println("Element Found");
            }
            stop=System.nanoTime();
            System.out.print("Time Taken to search an element : ");
            timelapsed=(stop-start)/1000000000f;
            System.out.println(timelapsed+" s");
            arr.clear();
        }
    }
}