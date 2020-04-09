package com.darwinbox.leaves.Accural;

import com.aventstack.extentreports.model.Test;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class planful  extends TestBase {


    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        /*System.setProperty("webdriver.chrome.driver", "C:/Users/sande/Desktop/yeten/Automation_Project/tests/src/main/resources/"+"drivers/windows/chromedriver.exe");

        ChromeOptions option = new ChromeOptions();
        option.addArguments("start-maximized");

        WebDriver driver= new ChromeDriver(option);
        String url ="https://insiders.morningstar.com/trading/board-of-directors.action?t=MSFT&region=usa&culture=en-US";
        List<JSONObject> directorDetails=getDirectorDetails(url,driver);*/

    /*    int[] arr = new int[]{8,9,0};
        int[] decremenetedArray=decrementByOne(arr);
*/
      /*  String input = "1??0?101";
        char[] str = input.toCharArray();
        replaceBinary(str, 0);*/
        //print(new char[]{'a,b});

        //char[] arr= new char[]{'a','b','c'};
        //printCombinations(arr,arr.length,arr.length,"");

        String n = scanner.nextLine();

        Boolean ind=true;
        for(long i=2;i<Integer.parseInt(n)/2;i++){
            if(Integer.parseInt(n)%i==0 && Integer.parseInt(n)!=i)
            {
                ind=false;
                break;
            }
            else{
                ind=true;
            }
        }

        if(ind==true)
        {
            System.out.println("prime");
        }
        else{
            System.out.println("not prime");
        }
        scanner.close();
    }


    public static void  printCombinations(char[] arr,int length,int elemetToAdd,String combination){



        int elementsToAdd=length;




        for(char element:arr)
        {
            combination=combination+element;
            elementsToAdd = elementsToAdd-1;
            if(elementsToAdd>0)
            printCombinations(arr,length,elemetToAdd,combination);
            else
                System.out.println(combination);

        }

    }
    
    public static int[] decrementByOne(int[] arr)
    {

        int toSub=1;

        for(int index= arr.length-1;index>-1;index--)
        {
            if(arr[index] - toSub < 0)
            {
                arr[index] = 10 -toSub;
                continue;

            }
            else {
                arr[index] = arr[index] - toSub;
                 break;
            }
        }


        return  arr;
    }

    public static void replaceBinary(char[] str,int index)
    {
        if (index == str.length)
        {
            System.out.println(str);
            return ;
        }

        if (str[index] == '?')
        {

            str[index] = '0';
            replaceBinary(str, index + 1);


            str[index] = '1';
            replaceBinary(str, index + 1);


            str[index] = '?';
        }
        else
            replaceBinary(str, index + 1);
    }


    public static List<JSONObject> getDirectorDetails(String url,WebDriver driver)
    {
        List<JSONObject> directorDeatails= new ArrayList<>();

        String directorName=null;
        String directorAge=null;
        String directorDetails=null;

        driver.get(url);

        List<WebElement> rows=driver.findElements(By.xpath("//*[@id='insiderList']/div"));

        for(WebElement row : rows) {
            List<WebElement> columns=row.findElements(By.className("colx1"));
            for (WebElement coulmn : columns) {
                JSONObject jsonObject = new JSONObject();

                directorName=coulmn.findElement(By.tagName("h2")).getText().split(",")[0].trim();

                try {
                    directorAge = coulmn.findElement(By.tagName("h2")).getText().split(",")[1].trim();
                }
                catch (Exception e)
                {
                    directorAge ="Not Defined In UI";
                }

                directorDetails =coulmn.findElement(By.tagName("span")).getText().trim().replace("\n"," ");

                jsonObject.put("Director Name", directorName);
                jsonObject.put("DirectorAge", directorAge);
                jsonObject.put("Director Details", directorDetails);

                directorDeatails.add(jsonObject);
            }
        }

        return directorDeatails;
    }

}
