package com.company;
import  org.json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;

import java.util.*;

public class Main {

    public static String requestURL(String url) throws Exception{
        // Set URL
        URLConnection connection = new URL(url).openConnection();
        // Set property - avoid 403 (CORS)
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        // Creat connection
        connection.connect();
        // Create a buffer of the input
        BufferedReader buffer  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        // Convert the response into a single string
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = buffer.readLine()) != null) {
            stringBuilder.append(line);
        }
        // return the response
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws Exception {

        Scanner searchTerm = new Scanner(System.in);

        System.out.println("Type in character's number");
        int charNumber = searchTerm.nextInt();

        String url = "https://anapioficeandfire.com/api/characters/" + charNumber;

        JSONObject jo = new JSONObject(requestURL(url));
        System.out.println("Basic information about character " + charNumber + ":");
        System.out.println("Name: " + jo.get("name"));
        System.out.println("Gender: " + jo.get("gender"));
        System.out.println("Culture: " + jo.get("culture"));
        JSONArray theTitles = new JSONArray(jo.get("titles").toString());
        System.out.print("Title: ");

        // Print titles
        for (int i = 0; i < theTitles.length(); i++) {
            System.out.println(theTitles.get(i));
        }
        // New input for allegiances
        System.out.println("Show people of allegiances? (y=1/n=0)");
        Scanner question = new Scanner(System.in);
        int allegiances = question.nextInt();

        // If user input == 1 show houses of allegiances
        if(allegiances == 1){
            System.out.println("Houses sworn to "+ jo.get("name") + ": ");
            JSONArray theAllegiances = new JSONArray(jo.get("allegiances").toString());

            // Loop allegiance array
            for (int i = 0; i < theAllegiances.length(); i++) {
                String url2 = theAllegiances.get(i).toString();
                JSONObject snow = new JSONObject(requestURL(url2));
                System.out.println(snow.get("name"));
            }
        }else{
            System.out.println("Declined. " + "Typed: " + allegiances);
        }

        // Bantam books filter

        for (int j=1;j<=12;j++){


            String bookUrl = "https://www.anapioficeandfire.com/api/books/" +(j);
            JSONObject books = new JSONObject(requestURL(bookUrl));

            String bantam = books.get("publisher").toString();


            if(bantam.equals("Bantam Books")){
                System.out.print("Book number " + (j) + " " + books.get("name") + " - ");
                System.out.println("Publisher: " + books.get("publisher"));

                //-------------------------------
                //String[][] arrStr = new String[12][];
                //arrStr[0][j] = books.get("name").toString();
                //System.out.println(arrStr[0][j]);

                // povChar - every point og view character in books published by Bantam
                System.out.println("------------------");
                JSONArray povChar = new JSONArray(books.get("povCharacters").toString());

                // Loop through each povCharacter for each book and print their name
                for (int i = 0; i < povChar.length(); i++) {


                    String povUrl = povChar.get(i).toString();
                    JSONObject pov = new JSONObject(requestURL(povUrl));
                    System.out.println(pov.get("name"));

                }
            }
        }
    }
}
