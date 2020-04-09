package com.darwinbox.leaves.Accural;

import com.darwinbox.framework.uiautomation.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class sas  extends TestBase {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        String s="dBH";
        char c='a';
        Character.isLowerCase(c);


//        driver.manage().window();
//        driver.manage().addCookie();
//        driver.manage().timeouts().implicitlyWait()
//        driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);
//
//        WebDriverWait driverWait= new WebDriverWait(driver,500);
//        driverWait.until(x->x.findElement(By.cssSelector("")));
//        driverWait.until(ExpectedConditions.elementToBeClickable())
//        driverWait.pollingEvery()
//
//                Action a = new
//
//        //int[] a=new int[5]{1,2,3,4,5}
//        int[] a= new int[]{1,2,3,4,5};
//        for (int numb:leftrotate(a,4))
//        {
//        System.out.println(numb);
//        }
//
//      //int[][] arr = new int[6][6];
//
//        for (int i = 0; i < 6; i++) {
//            String[] arrRowItems = scanner.nextLine().split(" ");
//            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
//
//            for (int j = 0; j < 6; j++) {
//                int arrItem = Integer.parseInt(arrRowItems[j]);
//                arr[i][j] = arrItem;
//            }
//        }
//
//        int[] arr= new int[]{2,3,4,1,5};
//        //System.out.println(hourglas(arr));
//        System.out.println(minimumSwaps(arr));


        // insertNodeAtTail(null,20);

         String s1="sas";
                 int x=15-s1.length();


                 swapTwoStrngs("Love","You");
    }


    public static void swapTwoStrngs(String s1,String s2){

        //s1="Love";
        //s2="you";

        s1=s1+s2;
        s2=s1.substring(0,(s1.length()-s2.length()));

        s1= s1.replace(s2,"");

        System.out.println(s1);
        System.out.println(s2);

    }




    static void printLinkedList(LinkedList head) {

        for (Object e:
             head) {

        System.out.println(e.toString());
        }

    }

    static SinglyLinkedListNode insertNodeAtTail(SinglyLinkedListNode head, int data) {

        //Iterator iterator= head;

        //Collections.so


       // head =SinglyLinkedListNode.class;

        head.data=data;
        head.next=null;


        return head;

    }



    static Integer hourglas(int[][] arr)
    {
        //considering a 6,6 arry

        List<Integer> numbers= new ArrayList<>();
        for(int row=1;row<5;row++)
        {
            for(int column=1;column<5;column++)
            {
                numbers.add(gethourglassValue(arr,row,column));
            }
        }

        return numbers.stream().mapToInt(number -> number).max().getAsInt();


    }

    static int gethourglassValue(int[][] arr,int row,int column)
    {

        return arr[row-1][column-1]+arr[row-1][column]+arr[row-1][column+1]+
                arr[row][column]+
            arr[row+1][column-1]+arr[row+1][column]+arr[row+1][column+1];
    }

    static int minimumSwaps(int[] arr) {


        int swap=0;
        boolean visited[]=new boolean[arr.length];

        for(int i=0;i<arr.length;i++){
            int j=i,cycle=0;

            while(!visited[j]){
                visited[j]=true;
                j=arr[j]-1;
                cycle++;
            }

            if(cycle!=0)
                swap+=cycle-1;
        }
        return swap;


    }


    static int[] leftrotate(int[] a,int  rotationNumber)
    {

        for(int i=0;i<rotationNumber;i++)
        {
            int temp = a[0];
            for(int j=0;j<a.length-1;j++)
            {
                a[j]=a[j+1];
            }

            a[a.length-1] = temp;

        }

        return  a;
    }

    class SinglyLinkedListNode {
        int data;
          SinglyLinkedListNode next;
      }

}
