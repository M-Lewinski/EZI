package com.lab1;

import com.beust.jcommander.JCommander;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    public static void main(String[] args){
        CommandParameters command = new CommandParameters();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(command)
                .build();
        jCommander.setProgramName("EZI");
        try
        {
            jCommander.parse(args);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            jCommander.usage();
            return;
        }
        if (command.help){
            jCommander.usage();
            return;
        }
        VectorSpace vectorSpace = new VectorSpace(command);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            try {
                System.out.printf("\nEnter querry\n");
                String querryString = br.readLine();
                List<Ranking> rankingList = vectorSpace.querryExecution(querryString);
                for (Ranking ranking: rankingList) {
                    System.out.printf("%f : %s\n",ranking.simIdf,ranking.document.title);
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Couldn't read querry from console");
                System.exit(1);
            }
        }

    }

}
